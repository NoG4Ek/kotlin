// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.util.indexing.snapshot;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.ShutDownTracker;
import com.intellij.openapi.vfs.newvfs.persistent.FSRecords;
import com.intellij.openapi.vfs.newvfs.persistent.FlushingDaemon;
import com.intellij.openapi.vfs.newvfs.persistent.PersistentFSImpl;
import com.intellij.util.hash.ContentHashEnumerator;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.FileContentImpl;
import com.intellij.util.indexing.IndexInfrastructure;
import com.intellij.util.indexing.flavor.FileIndexingFlavorProvider;
import com.intellij.util.indexing.flavor.HashBuilder;
import com.intellij.util.io.DigestUtil;
import com.intellij.util.io.IOUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApiStatus.Internal
public class IndexedHashesSupport {
  // TODO replace with sha-256
  private static final HashFunction INDEXED_FILE_CONTENT_HASHER = Hashing.sha1();

  private static volatile ContentHashEnumerator ourTextContentHashes;

  public static int getVersion() {
    return 2;
  }

  public static void initContentHashesEnumerator() throws IOException {
    if (ourTextContentHashes != null) return;
    //noinspection SynchronizeOnThis
    synchronized (IndexedHashesSupport.class) {
      if (ourTextContentHashes != null) return;
      final File hashEnumeratorFile = new File(IndexInfrastructure.getPersistentIndexRoot(), "textContentHashes");
      try {
        ContentHashEnumerator hashEnumerator = new ContentHashEnumerator(hashEnumeratorFile.toPath());
        FlushingDaemon.everyFiveSeconds(IndexedHashesSupport::flushContentHashes);
        ShutDownTracker.getInstance().registerShutdownTask(IndexedHashesSupport::flushContentHashes);
        ourTextContentHashes = hashEnumerator;
      }
      catch (IOException ex) {
        IOUtil.deleteAllFilesStartingWith(hashEnumeratorFile);
        throw ex;
      }
    }
  }

  public static void flushContentHashes() {
    if (ourTextContentHashes != null && ourTextContentHashes.isDirty()) ourTextContentHashes.force();
  }

  static int enumerateHash(byte @NotNull [] digest) throws IOException {
    return ourTextContentHashes.enumerate(digest);
  }

  public static byte @NotNull [] getOrInitIndexedHash(@NotNull FileContentImpl content) {
    byte[] hash = content.getHash();
    if (hash == null) {
      hash = calculateIndexedHashForFileContent(content);
      content.setHashes(hash);
    }
    return hash;
  }

  private static byte @NotNull [] calculateIndexedHashForFileContent(@NotNull FileContentImpl content) {
    Hasher hasher = INDEXED_FILE_CONTENT_HASHER.newHasher();

    byte[] contentHash = PersistentFSImpl.getContentHashIfStored(content.getFile());
    if (contentHash == null) {
      contentHash = DigestUtil.calculateContentHash(FSRecords.CONTENT_HASH_DIGEST, ((FileContent)content).getContent());
      // todo store content hash in FS
    }
    hasher.putBytes(contentHash);

    if (!content.getFileTypeWithoutSubstitution().isBinary()) {
      hasher.putString(content.getCharset().name(), StandardCharsets.UTF_8);
    }

    FileType fileType = content.getFileType();
    hasher.putString(fileType.getName(), StandardCharsets.UTF_8);

    @Nullable
    FileIndexingFlavorProvider<?> provider = FileIndexingFlavorProvider.INSTANCE.forFileType(fileType);
    if (provider != null) {
      buildFlavorHash(content, provider, new HashBuilder() {
        @Override
        public @NotNull HashBuilder putInt(int val) {
          hasher.putInt(val);
          return this;
        }

        @Override
        public @NotNull HashBuilder putBoolean(boolean val) {
          hasher.putBoolean(val);
          return this;
        }

        @Override
        public @NotNull HashBuilder putString(@NotNull CharSequence charSequence) {
          hasher.putString(charSequence, StandardCharsets.UTF_8);
          return this;
        }
      });
    }

    return hasher.hash().asBytes();
  }

  private static <F> void buildFlavorHash(@NotNull FileContent content,
                                          @NotNull FileIndexingFlavorProvider<F> flavorProvider,
                                          @NotNull HashBuilder hashBuilder) {
    F flavor = flavorProvider.getFlavor(content);
    hashBuilder.putString(flavorProvider.getId());
    hashBuilder.putInt(flavorProvider.getVersion());
    if (flavor != null) {
      flavorProvider.buildHash(flavor, hashBuilder);
    }
  }
}