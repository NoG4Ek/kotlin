fun String.foo(): Int = 10

// RESULT
/*
KtFirFunctionSymbol:
  annotatedType: [] kotlin/Int
  annotationClassIds: []
  annotations: []
  callableIdIfNonLocal: /foo
  dispatchType: null
  isExtension: true
  isExternal: false
  isInfix: false
  isInline: false
  isOperator: false
  isOverride: false
  isStatic: false
  isSuspend: false
  modality: FINAL
  name: foo
  origin: SOURCE
  receiverType: [] kotlin/String
  symbolKind: TOP_LEVEL
  typeParameters: []
  valueParameters: []
  visibility: Public
*/
