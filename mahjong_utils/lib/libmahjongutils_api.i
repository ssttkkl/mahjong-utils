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
} libmahjongutils_kref_mahjongutils_Entry;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_collections_List;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_models_Tile;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlinx_serialization_internal_SerializationConstructorMarker;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Any;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlinx_serialization_descriptors_SerialDescriptor;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_Array;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlinx_serialization_encoding_Decoder;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlinx_serialization_encoding_Encoder;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_Companion;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlinx_serialization_KSerializer;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HanHu;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HanHu_$serializer;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HanHu_Companion;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HoraArgs;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_models_Wind;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_kotlin_collections_Set;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HoraArgs_$serializer;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_HoraArgs_Companion;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_ShantenArgs;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer;
typedef struct {
  libmahjongutils_KNativePtr pinned;
} libmahjongutils_kref_mahjongutils_ShantenArgs_Companion;


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
          libmahjongutils_KType* (*_type)(void);
          const char* (*call)(libmahjongutils_kref_mahjongutils_Entry thiz, const char* name, const char* rawParams);
        } Entry;
        struct {
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer thiz);
            libmahjongutils_kref_kotlin_Array (*childSerializers)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer thiz);
            libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs (*deserialize)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Decoder decoder);
            void (*serialize)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Encoder encoder, libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs value);
          } $serializer;
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_Companion (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_KSerializer (*serializer)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs_Companion thiz);
          } Companion;
          libmahjongutils_KType* (*_type)(void);
          libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs (*FuroChanceShantenArgs)(libmahjongutils_KInt seen1, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_mahjongutils_models_Tile chanceTile, libmahjongutils_KBoolean allowChi, libmahjongutils_KBoolean bestShantenOnly, libmahjongutils_KBoolean allowKuikae, libmahjongutils_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
          libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs (*FuroChanceShantenArgs_)(libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_mahjongutils_models_Tile chanceTile, libmahjongutils_KBoolean allowChi, libmahjongutils_KBoolean bestShantenOnly, libmahjongutils_KBoolean allowKuikae);
          libmahjongutils_KBoolean (*get_allowChi)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_KBoolean (*get_allowKuikae)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_KBoolean (*get_bestShantenOnly)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Tile (*get_chanceTile)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*get_tiles)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*component1)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Tile (*component2)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_KBoolean (*component3)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_KBoolean (*component4)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_KBoolean (*component5)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs (*copy)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_mahjongutils_models_Tile chanceTile, libmahjongutils_KBoolean allowChi, libmahjongutils_KBoolean bestShantenOnly, libmahjongutils_KBoolean allowKuikae);
          libmahjongutils_KBoolean (*equals)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz, libmahjongutils_kref_kotlin_Any other);
          libmahjongutils_KInt (*hashCode)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
          const char* (*toString)(libmahjongutils_kref_mahjongutils_FuroChanceShantenArgs thiz);
        } FuroChanceShantenArgs;
        struct {
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_HanHu_$serializer (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libmahjongutils_kref_mahjongutils_HanHu_$serializer thiz);
            libmahjongutils_kref_kotlin_Array (*childSerializers)(libmahjongutils_kref_mahjongutils_HanHu_$serializer thiz);
            libmahjongutils_kref_mahjongutils_HanHu (*deserialize)(libmahjongutils_kref_mahjongutils_HanHu_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Decoder decoder);
            void (*serialize)(libmahjongutils_kref_mahjongutils_HanHu_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Encoder encoder, libmahjongutils_kref_mahjongutils_HanHu value);
          } $serializer;
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_HanHu_Companion (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_KSerializer (*serializer)(libmahjongutils_kref_mahjongutils_HanHu_Companion thiz);
          } Companion;
          libmahjongutils_KType* (*_type)(void);
          libmahjongutils_kref_mahjongutils_HanHu (*HanHu)(libmahjongutils_KInt seen1, libmahjongutils_KInt han, libmahjongutils_KInt hu, libmahjongutils_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
          libmahjongutils_kref_mahjongutils_HanHu (*HanHu_)(libmahjongutils_KInt han, libmahjongutils_KInt hu);
          libmahjongutils_KInt (*get_han)(libmahjongutils_kref_mahjongutils_HanHu thiz);
          libmahjongutils_KInt (*get_hu)(libmahjongutils_kref_mahjongutils_HanHu thiz);
          libmahjongutils_KInt (*component1)(libmahjongutils_kref_mahjongutils_HanHu thiz);
          libmahjongutils_KInt (*component2)(libmahjongutils_kref_mahjongutils_HanHu thiz);
          libmahjongutils_kref_mahjongutils_HanHu (*copy)(libmahjongutils_kref_mahjongutils_HanHu thiz, libmahjongutils_KInt han, libmahjongutils_KInt hu);
          libmahjongutils_KBoolean (*equals)(libmahjongutils_kref_mahjongutils_HanHu thiz, libmahjongutils_kref_kotlin_Any other);
          libmahjongutils_KInt (*hashCode)(libmahjongutils_kref_mahjongutils_HanHu thiz);
          const char* (*toString)(libmahjongutils_kref_mahjongutils_HanHu thiz);
        } HanHu;
        struct {
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_HoraArgs_$serializer (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libmahjongutils_kref_mahjongutils_HoraArgs_$serializer thiz);
            libmahjongutils_kref_kotlin_Array (*childSerializers)(libmahjongutils_kref_mahjongutils_HoraArgs_$serializer thiz);
            libmahjongutils_kref_mahjongutils_HoraArgs (*deserialize)(libmahjongutils_kref_mahjongutils_HoraArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Decoder decoder);
            void (*serialize)(libmahjongutils_kref_mahjongutils_HoraArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Encoder encoder, libmahjongutils_kref_mahjongutils_HoraArgs value);
          } $serializer;
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_HoraArgs_Companion (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_KSerializer (*serializer)(libmahjongutils_kref_mahjongutils_HoraArgs_Companion thiz);
          } Companion;
          libmahjongutils_KType* (*_type)(void);
          libmahjongutils_kref_mahjongutils_HoraArgs (*HoraArgs)(libmahjongutils_KInt seen1, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult shantenResult, libmahjongutils_kref_mahjongutils_models_Tile agari, libmahjongutils_KBoolean tsumo, libmahjongutils_KInt dora, libmahjongutils_kref_mahjongutils_models_Wind selfWind, libmahjongutils_kref_mahjongutils_models_Wind roundWind, libmahjongutils_kref_kotlin_collections_Set extraYaku, libmahjongutils_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
          libmahjongutils_kref_mahjongutils_HoraArgs (*HoraArgs_)(libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult shantenResult, libmahjongutils_kref_mahjongutils_models_Tile agari, libmahjongutils_KBoolean tsumo, libmahjongutils_KInt dora, libmahjongutils_kref_mahjongutils_models_Wind selfWind, libmahjongutils_kref_mahjongutils_models_Wind roundWind, libmahjongutils_kref_kotlin_collections_Set extraYaku);
          libmahjongutils_kref_mahjongutils_models_Tile (*get_agari)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_KInt (*get_dora)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_Set (*get_extraYaku)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*get_furo)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Wind (*get_roundWind)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Wind (*get_selfWind)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult (*get_shantenResult)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*get_tiles)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_KBoolean (*get_tsumo)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*component1)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*component2)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult (*component3)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Tile (*component4)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_KBoolean (*component5)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_KInt (*component6)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Wind (*component7)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_models_Wind (*component8)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_kotlin_collections_Set (*component9)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          libmahjongutils_kref_mahjongutils_HoraArgs (*copy)(libmahjongutils_kref_mahjongutils_HoraArgs thiz, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_kref_mahjongutils_shanten_CommonShantenResult shantenResult, libmahjongutils_kref_mahjongutils_models_Tile agari, libmahjongutils_KBoolean tsumo, libmahjongutils_KInt dora, libmahjongutils_kref_mahjongutils_models_Wind selfWind, libmahjongutils_kref_mahjongutils_models_Wind roundWind, libmahjongutils_kref_kotlin_collections_Set extraYaku);
          libmahjongutils_KBoolean (*equals)(libmahjongutils_kref_mahjongutils_HoraArgs thiz, libmahjongutils_kref_kotlin_Any other);
          libmahjongutils_KInt (*hashCode)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
          const char* (*toString)(libmahjongutils_kref_mahjongutils_HoraArgs thiz);
        } HoraArgs;
        struct {
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_descriptors_SerialDescriptor (*get_descriptor)(libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer thiz);
            libmahjongutils_kref_kotlin_Array (*childSerializers)(libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer thiz);
            libmahjongutils_kref_mahjongutils_ShantenArgs (*deserialize)(libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Decoder decoder);
            void (*serialize)(libmahjongutils_kref_mahjongutils_ShantenArgs_$serializer thiz, libmahjongutils_kref_kotlinx_serialization_encoding_Encoder encoder, libmahjongutils_kref_mahjongutils_ShantenArgs value);
          } $serializer;
          struct {
            libmahjongutils_KType* (*_type)(void);
            libmahjongutils_kref_mahjongutils_ShantenArgs_Companion (*_instance)();
            libmahjongutils_kref_kotlinx_serialization_KSerializer (*serializer)(libmahjongutils_kref_mahjongutils_ShantenArgs_Companion thiz);
          } Companion;
          libmahjongutils_KType* (*_type)(void);
          libmahjongutils_kref_mahjongutils_ShantenArgs (*ShantenArgs)(libmahjongutils_KInt seen1, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_KBoolean bestShantenOnly, libmahjongutils_kref_kotlinx_serialization_internal_SerializationConstructorMarker serializationConstructorMarker);
          libmahjongutils_kref_mahjongutils_ShantenArgs (*ShantenArgs_)(libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_KBoolean bestShantenOnly);
          libmahjongutils_KBoolean (*get_bestShantenOnly)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*get_furo)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*get_tiles)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*component1)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_kref_kotlin_collections_List (*component2)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_KBoolean (*component3)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          libmahjongutils_kref_mahjongutils_ShantenArgs (*copy)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz, libmahjongutils_kref_kotlin_collections_List tiles, libmahjongutils_kref_kotlin_collections_List furo, libmahjongutils_KBoolean bestShantenOnly);
          libmahjongutils_KBoolean (*equals)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz, libmahjongutils_kref_kotlin_Any other);
          libmahjongutils_KInt (*hashCode)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
          const char* (*toString)(libmahjongutils_kref_mahjongutils_ShantenArgs thiz);
        } ShantenArgs;
        libmahjongutils_kref_mahjongutils_Entry (*get_ENTRY)();
      } mahjongutils;
    } root;
  } kotlin;
} libmahjongutils_ExportedSymbols;
extern libmahjongutils_ExportedSymbols* libmahjongutils_symbols(void);
