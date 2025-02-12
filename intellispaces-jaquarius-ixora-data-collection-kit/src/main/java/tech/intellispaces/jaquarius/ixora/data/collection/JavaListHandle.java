package tech.intellispaces.jaquarius.ixora.data.collection;

import tech.intellispaces.commons.base.type.Type;
import tech.intellispaces.commons.base.type.Types;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;

import java.util.List;

@ObjectHandle(ListDomain.class)
abstract class JavaListHandle<E> implements UnmovableListHandle<E> {
  private final List<E> list;
  private final Type<E> elementType;

  JavaListHandle(List<E> list, Class<E> elementClass) {
    this.list = java.util.Collections.unmodifiableList(list);
    this.elementType = Types.get(elementClass);
  }

  JavaListHandle(List<E> list, Type<E> elementType) {
    this.list = java.util.Collections.unmodifiableList(list);
    this.elementType = elementType;
  }

  List<E> list() {
    return list;
  }

  @Mapper
  @Override
  public UnmovableCollectionHandle<E> asCollection() {
    return Collections.of(list, elementType);
  }

  @Override
  public Type<ListDomain<E>> domain() {
    return Types.get(ListDomain.class, elementType);
  }

  @Mapper
  @Override
  public Type<E> elementDomain() {
    return elementType;
  }

  @Mapper
  @Override
  public E get(int index) {
    return list.get(index);
  }

  @Mapper
  @Override
  public Integer size() {
    return list.size();
  }

  @Mapper
  @Override
  public int sizeAsPrimitive() {
    return list.size();
  }

  @Override
  public List<E> nativeList() {
    return list;
  }

  @Override
  public java.util.Collection<E> nativeCollection() {
    return list;
  }
}
