package tech.intellispaces.ixora.rdb.statement;

import jakarta.persistence.Column;
import tech.intellispaces.commons.collection.ArraysFunctions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.ixora.data.collection.List;
import tech.intellispaces.ixora.data.collection.Lists;
import tech.intellispaces.jaquarius.annotation.Dataset;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.MapperOfMoving;
import tech.intellispaces.jaquarius.annotation.Name;
import tech.intellispaces.jaquarius.dataset.DatasetFunctions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Guide
public class ResultSetToDataGuideImpl {

  @Mapper(ResultSetToDataChannel.class)
  @SuppressWarnings("unchecked")
  public <D> D resultSetToData(ResultSet resultSet, Type<D> dataType) {
    var dataClass = (Class<D>) dataType.asClassType().baseClass();
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    Object[] arguments = makeDataHandleArguments(resultSet, dataClass, domainClass, constructor);
    try {
      return constructor.newInstance(arguments);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Failed to create data handle");
    }
  }

  @MapperOfMoving(ResultSetToDataListChannel.class)
  @SuppressWarnings("unchecked")
  public <D> List<D> resultSetToDataList(ResultSet resultSet, Type<D> dataType) {
    var dataClass = (Class<D>) dataType.asClassType().baseClass();
    Class<?> domainClass = getDomainClass(dataClass);
    Constructor<D> constructor = getDataHandleConstructor(dataClass, domainClass);
    java.util.List<D> values = new ArrayList<>();
    while (((MovableResultSet) resultSet).next()) {
      Object[] arguments = makeDataHandleArguments(resultSet, dataClass, domainClass, constructor);
      try {
        values.add(constructor.newInstance(arguments));
      } catch (Exception e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Failed to create data handle");
      }
    }
    return Lists.handleOf(values, dataClass);
  }

  @SuppressWarnings("unchecked")
  private <D> Constructor<D> getDataHandleConstructor(Class<D> dataClass, Class<?> domainClass) {
    String dataHandleClassName = NameConventionFunctions.getUnmovableDatasetClassName(domainClass.getCanonicalName());
    Class<D> dataHandleClass = (Class<D>) ClassFunctions.getClass(dataHandleClassName).orElseThrow(() ->
        UnexpectedExceptions.withMessage("Could not find data handle class by name {0} ",
            dataHandleClassName)
    );

    Constructor<?>[] constructors = dataHandleClass.getConstructors();
    if (constructors.length > 1) {
      throw UnexpectedExceptions.withMessage("Data handle class should have one constructor");
    }
    return (Constructor<D>) constructors[0];
  }

  private <D> Class<?> getDomainClass(Class<D> dataClass) {
    Class<?> domainClass = ObjectReferenceFunctions.getDomainClassOfObjectHandle(dataClass);
    if (!DatasetFunctions.isDatasetDomain(domainClass)) {
      throw UnexpectedExceptions.withMessage("Expected object handle class of the data domain. " +
          "Data domain should be annotated with @{0}", Dataset.class.getSimpleName());
    }
    return domainClass;
  }

  private <D> Object[] makeDataHandleArguments(
      ResultSet resultSet, Class<D> dataClass, Class<?> domainClass, Constructor<D> constructor
  ) {
    Map<String, String> mapping = makeChannelAliasToColumnNameMapping(domainClass);

    Object[] arguments = new Object[constructor.getParameterCount()];
    for (int index = 0; index < constructor.getParameterCount(); index++) {
      Parameter param = constructor.getParameters()[index];
      Name name = param.getAnnotation(Name.class);
      if (name == null) {
        throw UnexpectedExceptions.withMessage("Parameter {0} of the data class {1} constructor " +
            "should be marked with annotation {2}", index, dataClass, Name.class.getSimpleName());
      }
      String alias = name.value();
      Class<?> paramClass = param.getType();
      setArgument(resultSet, arguments, index, mapping.get(alias), paramClass);
    }
    return arguments;
  }

  private void setArgument(
      ResultSet resultSet, Object[] arguments, int index, String columnName, Class<?> paramClass
  ) {
    if (paramClass == int.class) {
      Integer value = resultSet.integer32Value(columnName);
      if (value == null) {
        throw UnexpectedExceptions.withMessage("Null value of the primitive integer value by name {0}",
            columnName);
      }
      arguments[index] = value;
    } else if (paramClass == Integer.class) {
      arguments[index] = resultSet.integer32Value(columnName);
    } else if (paramClass == String.class) {
      arguments[index] = resultSet.stringValue(columnName);
    }
  }

  private Map<String, String> makeChannelAliasToColumnNameMapping(Class<?> domainClass) {
    Map<String, String> mapping = new HashMap<>();
    ArraysFunctions.foreach(domainClass.getDeclaredMethods(), method -> {
      Column column = method.getAnnotation(Column.class);
      if (column == null) {
        throw UnexpectedExceptions.withMessage("Method {0} of the class {1} should be marked with annotation {2}",
            method.getName(), domainClass.getCanonicalName(), Column.class.getSimpleName()
        );
      }
      if (StringFunctions.isNullOrBlank(column.name())) {
        throw UnexpectedExceptions.withMessage("Name attribute should be defined in annotation {0} " +
                "on the method {1} in class {2}",
            Column.class.getSimpleName(), method.getName(), domainClass.getCanonicalName()
        );
      }
      mapping.put(method.getName(), column.name());
    });
    return mapping;
  }
}
