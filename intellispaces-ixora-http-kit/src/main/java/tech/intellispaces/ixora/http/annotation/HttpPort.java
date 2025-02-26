package tech.intellispaces.ixora.http.annotation;

import tech.intellispaces.ixora.http.annotation.processor.HttpPortProcessor;
import tech.intellispaces.jaquarius.annotation.AnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inbound HTTP logical port.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AnnotationProcessor(HttpPortProcessor.class)
public @interface HttpPort {

  /**
   * HTTP ontology classes.
   */
  Class<?>[] value();
}
