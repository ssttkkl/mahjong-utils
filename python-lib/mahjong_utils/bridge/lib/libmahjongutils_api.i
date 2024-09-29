typedef _Bool libmahjongutils_KBoolean;

typedef unsigned short libmahjongutils_KChar;
typedef signed char libmahjongutils_KByte;
typedef short libmahjongutils_KShort;
typedef int libmahjongutils_KInt;
typedef long long libmahjongutils_KLong;
typedef unsigned char libmahjongutils_KUByte;
typedef unsigned short libmahjongutils_KUShort;
typedef unsigned int libmahjongutils_KUInt;
typedef unsigned long long libmahjongutils_KULong;
typedef float libmahjongutils_KFloat;
typedef double libmahjongutils_KDouble;
typedef void* libmahjongutils_KNativePtr;
struct libmahjongutils_KType;
typedef struct libmahjongutils_KType libmahjongutils_KType;

typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Byte;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Short;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Int;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Long;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Float;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Double;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Char;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Boolean;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Unit;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_UByte;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_UShort;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_UInt;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_ULong;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_entry_MethodExecutionException;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Throwable;


typedef struct {

  void (*DisposeStablePointer)(libmahjongutils_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libmahjongutils_KBoolean (*IsInstance)(libmahjongutils_KNativePtr ref, const libmahjongutils_KType* type);
  libmahjongutils_kref_kotlin_Byte (*createNullableByte)(libmahjongutils_KByte);
  libmahjongutils_KByte (*getNonNullValueOfByte)(libmahjongutils_kref_kotlin_Byte);
  libmahjongutils_kref_kotlin_Short (*createNullableShort)(libmahjongutils_KShort);
  libmahjongutils_KShort (*getNonNullValueOfShort)(libmahjongutils_kref_kotlin_Short);
  libmahjongutils_kref_kotlin_Int (*createNullableInt)(libmahjongutils_KInt);
  libmahjongutils_KInt (*getNonNullValueOfInt)(libmahjongutils_kref_kotlin_Int);
  libmahjongutils_kref_kotlin_Long (*createNullableLong)(libmahjongutils_KLong);
  libmahjongutils_KLong (*getNonNullValueOfLong)(libmahjongutils_kref_kotlin_Long);
  libmahjongutils_kref_kotlin_Float (*createNullableFloat)(libmahjongutils_KFloat);
  libmahjongutils_KFloat (*getNonNullValueOfFloat)(libmahjongutils_kref_kotlin_Float);
  libmahjongutils_kref_kotlin_Double (*createNullableDouble)(libmahjongutils_KDouble);
  libmahjongutils_KDouble (*getNonNullValueOfDouble)(libmahjongutils_kref_kotlin_Double);
  libmahjongutils_kref_kotlin_Char (*createNullableChar)(libmahjongutils_KChar);
  libmahjongutils_KChar (*getNonNullValueOfChar)(libmahjongutils_kref_kotlin_Char);
  libmahjongutils_kref_kotlin_Boolean (*createNullableBoolean)(libmahjongutils_KBoolean);
  libmahjongutils_KBoolean (*getNonNullValueOfBoolean)(libmahjongutils_kref_kotlin_Boolean);
  libmahjongutils_kref_kotlin_Unit (*createNullableUnit)(void);
  libmahjongutils_kref_kotlin_UByte (*createNullableUByte)(libmahjongutils_KUByte);
  libmahjongutils_KUByte (*getNonNullValueOfUByte)(libmahjongutils_kref_kotlin_UByte);
  libmahjongutils_kref_kotlin_UShort (*createNullableUShort)(libmahjongutils_KUShort);
  libmahjongutils_KUShort (*getNonNullValueOfUShort)(libmahjongutils_kref_kotlin_UShort);
  libmahjongutils_kref_kotlin_UInt (*createNullableUInt)(libmahjongutils_KUInt);
  libmahjongutils_KUInt (*getNonNullValueOfUInt)(libmahjongutils_kref_kotlin_UInt);
  libmahjongutils_kref_kotlin_ULong (*createNullableULong)(libmahjongutils_KULong);
  libmahjongutils_KULong (*getNonNullValueOfULong)(libmahjongutils_kref_kotlin_ULong);


  struct {
    struct {
      struct {
        struct {
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_entry_MethodExecutionException (*MethodExecutionException)(libmahjongutils_KInt code, libmahjongutils_kref_kotlin_Throwable cause);
            libmahjongutils_kref_mahjongutils_entry_MethodExecutionException (*MethodExecutionException_)(libmahjongutils_KInt code, const char* message, libmahjongutils_kref_kotlin_Throwable cause);
            libmahjongutils_KInt (*get_code)(libmahjongutils_kref_mahjongutils_entry_MethodExecutionException thiz);
          } MethodExecutionException;
          const char* (*call)(const char* name, const char* rawParams);
          const char* (*callReceivingData)(const char* name, const char* rawParams);
        } entry;
      } mahjongutils;
    } root;
  } kotlin;
} libmahjongutils_ExportedSymbols;
extern libmahjongutils_ExportedSymbols* libmahjongutils_symbols(void);
