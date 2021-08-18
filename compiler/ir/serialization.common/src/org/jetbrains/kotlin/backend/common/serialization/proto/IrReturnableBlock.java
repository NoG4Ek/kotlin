// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/ir/serialization.common/src/KotlinIr.proto

package org.jetbrains.kotlin.backend.common.serialization.proto;

/**
 * Protobuf type {@code org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock}
 */
public final class IrReturnableBlock extends
    org.jetbrains.kotlin.protobuf.GeneratedMessageLite implements
    // @@protoc_insertion_point(message_implements:org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock)
    IrReturnableBlockOrBuilder {
  // Use IrReturnableBlock.newBuilder() to construct.
  private IrReturnableBlock(org.jetbrains.kotlin.protobuf.GeneratedMessageLite.Builder builder) {
    super(builder);
    this.unknownFields = builder.getUnknownFields();
  }
  private IrReturnableBlock(boolean noInit) { this.unknownFields = org.jetbrains.kotlin.protobuf.ByteString.EMPTY;}

  private static final IrReturnableBlock defaultInstance;
  public static IrReturnableBlock getDefaultInstance() {
    return defaultInstance;
  }

  public IrReturnableBlock getDefaultInstanceForType() {
    return defaultInstance;
  }

  private final org.jetbrains.kotlin.protobuf.ByteString unknownFields;
  private IrReturnableBlock(
      org.jetbrains.kotlin.protobuf.CodedInputStream input,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
    initFields();
    int mutable_bitField0_ = 0;
    org.jetbrains.kotlin.protobuf.ByteString.Output unknownFieldsOutput =
        org.jetbrains.kotlin.protobuf.ByteString.newOutput();
    org.jetbrains.kotlin.protobuf.CodedOutputStream unknownFieldsCodedOutput =
        org.jetbrains.kotlin.protobuf.CodedOutputStream.newInstance(
            unknownFieldsOutput, 1);
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownField(input, unknownFieldsCodedOutput,
                                   extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
              statement_ = new java.util.ArrayList<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement>();
              mutable_bitField0_ |= 0x00000001;
            }
            statement_.add(input.readMessage(org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement.PARSER, extensionRegistry));
            break;
          }
          case 16: {
            bitField0_ |= 0x00000001;
            originName_ = input.readInt32();
            break;
          }
          case 24: {
            bitField0_ |= 0x00000002;
            inlineFunctionSymbol_ = input.readInt64();
            break;
          }
        }
      }
    } catch (org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException(
          e.getMessage()).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
        statement_ = java.util.Collections.unmodifiableList(statement_);
      }
      try {
        unknownFieldsCodedOutput.flush();
      } catch (java.io.IOException e) {
      // Should not happen
      } finally {
        unknownFields = unknownFieldsOutput.toByteString();
      }
      makeExtensionsImmutable();
    }
  }
  public static org.jetbrains.kotlin.protobuf.Parser<IrReturnableBlock> PARSER =
      new org.jetbrains.kotlin.protobuf.AbstractParser<IrReturnableBlock>() {
    public IrReturnableBlock parsePartialFrom(
        org.jetbrains.kotlin.protobuf.CodedInputStream input,
        org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
        throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
      return new IrReturnableBlock(input, extensionRegistry);
    }
  };

  @java.lang.Override
  public org.jetbrains.kotlin.protobuf.Parser<IrReturnableBlock> getParserForType() {
    return PARSER;
  }

  private int bitField0_;
  public static final int STATEMENT_FIELD_NUMBER = 1;
  private java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement> statement_;
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
   */
  public java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement> getStatementList() {
    return statement_;
  }
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
   */
  public java.util.List<? extends org.jetbrains.kotlin.backend.common.serialization.proto.IrStatementOrBuilder> 
      getStatementOrBuilderList() {
    return statement_;
  }
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
   */
  public int getStatementCount() {
    return statement_.size();
  }
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
   */
  public org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement getStatement(int index) {
    return statement_.get(index);
  }
  /**
   * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
   */
  public org.jetbrains.kotlin.backend.common.serialization.proto.IrStatementOrBuilder getStatementOrBuilder(
      int index) {
    return statement_.get(index);
  }

  public static final int ORIGIN_NAME_FIELD_NUMBER = 2;
  private int originName_;
  /**
   * <code>optional int32 origin_name = 2;</code>
   */
  public boolean hasOriginName() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <code>optional int32 origin_name = 2;</code>
   */
  public int getOriginName() {
    return originName_;
  }

  public static final int INLINE_FUNCTION_SYMBOL_FIELD_NUMBER = 3;
  private long inlineFunctionSymbol_;
  /**
   * <code>optional int64 inline_function_symbol = 3;</code>
   */
  public boolean hasInlineFunctionSymbol() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <code>optional int64 inline_function_symbol = 3;</code>
   */
  public long getInlineFunctionSymbol() {
    return inlineFunctionSymbol_;
  }

  private void initFields() {
    statement_ = java.util.Collections.emptyList();
    originName_ = 0;
    inlineFunctionSymbol_ = 0L;
  }
  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    for (int i = 0; i < getStatementCount(); i++) {
      if (!getStatement(i).isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
    }
    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(org.jetbrains.kotlin.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    for (int i = 0; i < statement_.size(); i++) {
      output.writeMessage(1, statement_.get(i));
    }
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      output.writeInt32(2, originName_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      output.writeInt64(3, inlineFunctionSymbol_);
    }
    output.writeRawBytes(unknownFields);
  }

  private int memoizedSerializedSize = -1;
  public int getSerializedSize() {
    int size = memoizedSerializedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < statement_.size(); i++) {
      size += org.jetbrains.kotlin.protobuf.CodedOutputStream
        .computeMessageSize(1, statement_.get(i));
    }
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += org.jetbrains.kotlin.protobuf.CodedOutputStream
        .computeInt32Size(2, originName_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += org.jetbrains.kotlin.protobuf.CodedOutputStream
        .computeInt64Size(3, inlineFunctionSymbol_);
    }
    size += unknownFields.size();
    memoizedSerializedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  protected java.lang.Object writeReplace()
      throws java.io.ObjectStreamException {
    return super.writeReplace();
  }

  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      org.jetbrains.kotlin.protobuf.ByteString data)
      throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      org.jetbrains.kotlin.protobuf.ByteString data,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(byte[] data)
      throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      byte[] data,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      java.io.InputStream input,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseDelimitedFrom(
      java.io.InputStream input,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      org.jetbrains.kotlin.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parseFrom(
      org.jetbrains.kotlin.protobuf.CodedInputStream input,
      org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public static Builder newBuilder() { return Builder.create(); }
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder(org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock prototype) {
    return newBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() { return newBuilder(this); }

  /**
   * Protobuf type {@code org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock}
   */
  public static final class Builder extends
      org.jetbrains.kotlin.protobuf.GeneratedMessageLite.Builder<
        org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock, Builder>
      implements
      // @@protoc_insertion_point(builder_implements:org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock)
      org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlockOrBuilder {
    // Construct using org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
    }
    private static Builder create() {
      return new Builder();
    }

    public Builder clear() {
      super.clear();
      statement_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      originName_ = 0;
      bitField0_ = (bitField0_ & ~0x00000002);
      inlineFunctionSymbol_ = 0L;
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    public Builder clone() {
      return create().mergeFrom(buildPartial());
    }

    public org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock getDefaultInstanceForType() {
      return org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock.getDefaultInstance();
    }

    public org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock build() {
      org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock buildPartial() {
      org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock result = new org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        statement_ = java.util.Collections.unmodifiableList(statement_);
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.statement_ = statement_;
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000001;
      }
      result.originName_ = originName_;
      if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
        to_bitField0_ |= 0x00000002;
      }
      result.inlineFunctionSymbol_ = inlineFunctionSymbol_;
      result.bitField0_ = to_bitField0_;
      return result;
    }

    public Builder mergeFrom(org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock other) {
      if (other == org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock.getDefaultInstance()) return this;
      if (!other.statement_.isEmpty()) {
        if (statement_.isEmpty()) {
          statement_ = other.statement_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureStatementIsMutable();
          statement_.addAll(other.statement_);
        }
        
      }
      if (other.hasOriginName()) {
        setOriginName(other.getOriginName());
      }
      if (other.hasInlineFunctionSymbol()) {
        setInlineFunctionSymbol(other.getInlineFunctionSymbol());
      }
      setUnknownFields(
          getUnknownFields().concat(other.unknownFields));
      return this;
    }

    public final boolean isInitialized() {
      for (int i = 0; i < getStatementCount(); i++) {
        if (!getStatement(i).isInitialized()) {
          
          return false;
        }
      }
      return true;
    }

    public Builder mergeFrom(
        org.jetbrains.kotlin.protobuf.CodedInputStream input,
        org.jetbrains.kotlin.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (org.jetbrains.kotlin.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement> statement_ =
      java.util.Collections.emptyList();
    private void ensureStatementIsMutable() {
      if (!((bitField0_ & 0x00000001) == 0x00000001)) {
        statement_ = new java.util.ArrayList<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement>(statement_);
        bitField0_ |= 0x00000001;
       }
    }

    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public java.util.List<org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement> getStatementList() {
      return java.util.Collections.unmodifiableList(statement_);
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public int getStatementCount() {
      return statement_.size();
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement getStatement(int index) {
      return statement_.get(index);
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder setStatement(
        int index, org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureStatementIsMutable();
      statement_.set(index, value);

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder setStatement(
        int index, org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement.Builder builderForValue) {
      ensureStatementIsMutable();
      statement_.set(index, builderForValue.build());

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder addStatement(org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureStatementIsMutable();
      statement_.add(value);

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder addStatement(
        int index, org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureStatementIsMutable();
      statement_.add(index, value);

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder addStatement(
        org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement.Builder builderForValue) {
      ensureStatementIsMutable();
      statement_.add(builderForValue.build());

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder addStatement(
        int index, org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement.Builder builderForValue) {
      ensureStatementIsMutable();
      statement_.add(index, builderForValue.build());

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder addAllStatement(
        java.lang.Iterable<? extends org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement> values) {
      ensureStatementIsMutable();
      org.jetbrains.kotlin.protobuf.AbstractMessageLite.Builder.addAll(
          values, statement_);

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder clearStatement() {
      statement_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);

      return this;
    }
    /**
     * <code>repeated .org.jetbrains.kotlin.backend.common.serialization.proto.IrStatement statement = 1;</code>
     */
    public Builder removeStatement(int index) {
      ensureStatementIsMutable();
      statement_.remove(index);

      return this;
    }

    private int originName_ ;
    /**
     * <code>optional int32 origin_name = 2;</code>
     */
    public boolean hasOriginName() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int32 origin_name = 2;</code>
     */
    public int getOriginName() {
      return originName_;
    }
    /**
     * <code>optional int32 origin_name = 2;</code>
     */
    public Builder setOriginName(int value) {
      bitField0_ |= 0x00000002;
      originName_ = value;
      
      return this;
    }
    /**
     * <code>optional int32 origin_name = 2;</code>
     */
    public Builder clearOriginName() {
      bitField0_ = (bitField0_ & ~0x00000002);
      originName_ = 0;
      
      return this;
    }

    private long inlineFunctionSymbol_ ;
    /**
     * <code>optional int64 inline_function_symbol = 3;</code>
     */
    public boolean hasInlineFunctionSymbol() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int64 inline_function_symbol = 3;</code>
     */
    public long getInlineFunctionSymbol() {
      return inlineFunctionSymbol_;
    }
    /**
     * <code>optional int64 inline_function_symbol = 3;</code>
     */
    public Builder setInlineFunctionSymbol(long value) {
      bitField0_ |= 0x00000004;
      inlineFunctionSymbol_ = value;
      
      return this;
    }
    /**
     * <code>optional int64 inline_function_symbol = 3;</code>
     */
    public Builder clearInlineFunctionSymbol() {
      bitField0_ = (bitField0_ & ~0x00000004);
      inlineFunctionSymbol_ = 0L;
      
      return this;
    }

    // @@protoc_insertion_point(builder_scope:org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock)
  }

  static {
    defaultInstance = new IrReturnableBlock(true);
    defaultInstance.initFields();
  }

  // @@protoc_insertion_point(class_scope:org.jetbrains.kotlin.backend.common.serialization.proto.IrReturnableBlock)
}