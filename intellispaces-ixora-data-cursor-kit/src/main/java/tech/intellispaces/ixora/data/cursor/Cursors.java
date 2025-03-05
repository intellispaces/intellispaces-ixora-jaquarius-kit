package tech.intellispaces.ixora.data.cursor;

import tech.intellispaces.ixora.data.collection.CollectionDomain;

public interface Cursors {

  /**
   * Creates new cursor.
   *
   * @param collection source collection.
   * @return created cursor.
   * @param <E> collection elements type.
   */
  static <E> Cursor<E> get(CollectionDomain<E> collection) {
    throw new RuntimeException("Not implemented");
  }
}
