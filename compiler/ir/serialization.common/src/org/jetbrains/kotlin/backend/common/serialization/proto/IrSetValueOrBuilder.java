// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compiler/ir/serialization.common/src/KotlinIr.proto

package org.jetbrains.kotlin.backend.common.serialization.proto;

public interface IrSetValueOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jetbrains.kotlin.backend.common.serialization.proto.IrSetValue)
    org.jetbrains.kotlin.protobuf.MessageLiteOrBuilder {

  /**
   * <code>required int64 symbol = 1;</code>
   */
  boolean hasSymbol();
  /**
   * <code>required int64 symbol = 1;</code>
   */
  long getSymbol();

  /**
   * <code>required .org.jetbrains.kotlin.backend.common.serialization.proto.IrExpression value = 2;</code>
   */
  boolean hasValue();
  /**
   * <code>required .org.jetbrains.kotlin.backend.common.serialization.proto.IrExpression value = 2;</code>
   */
  org.jetbrains.kotlin.backend.common.serialization.proto.IrExpression getValue();

  /**
   * <code>optional int32 origin_name = 3;</code>
   */
  boolean hasOriginName();
  /**
   * <code>optional int32 origin_name = 3;</code>
   */
  int getOriginName();
}