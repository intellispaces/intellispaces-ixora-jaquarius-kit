package tech.intellispaces.ixora.data.association;

import tech.intellispaces.commons.type.Type;

public interface Maps {

  static <K, V> UnmovableMap<K, V> of(java.util.Map<K, V> map, Class<K> keyClass, Class<V> valueClass) {
    return new JavaMapWrapper<>(map, keyClass, valueClass);
  }

  static <K, V> UnmovableMap<K, V> of(java.util.Map<K, V> map, Type<K> keyType, Type<V> valueType) {
    return new JavaMapWrapper<>(map, keyType, valueType);
  }

  static <K, V> UnmovableMap<K, V> of(
    Class<K> keyClass, Class<V> valueClass,
    K key, V value
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key, value), keyClass, valueClass);
  }

  static <K, V> UnmovableMap<K, V> of(
    Class<K> keyClass, Class<V> valueClass,
    K key1, V value1,
    K key2, V value2
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key1, value1, key2, value2), keyClass, valueClass);
  }

  static <K, V> UnmovableMap<K, V> of(
    Class<K> keyClass, Class<V> valueClass,
    K key1, V value1,
    K key2, V value2,
    K key3, V value3
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key1, value1, key2, value2, key3, value3), keyClass, valueClass);
  }

  static <K, V> UnmovableMap<K, V> of(
    Type<K> keyType, Type<V> valueType,
    K key, V value
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key, value), keyType, valueType);
  }

  static <K, V> UnmovableMap<K, V> of(
    Type<K> keyType, Type<V> valueType,
    K key1, V value1,
    K key2, V value2
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key1, value1, key2, value2), keyType, valueType);
  }

  static <K, V> UnmovableMap<K, V> of(
    Type<K> keyType, Type<V> valueType,
    K key1, V value1,
    K key2, V value2,
    K key3, V value3
  ) {
    return new JavaMapWrapper<>(java.util.Map.of(key1, value1, key2, value2, key3, value3), keyType, valueType);
  }
}
