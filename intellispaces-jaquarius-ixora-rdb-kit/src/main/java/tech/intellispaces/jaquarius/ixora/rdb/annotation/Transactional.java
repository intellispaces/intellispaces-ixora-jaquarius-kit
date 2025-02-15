package tech.intellispaces.jaquarius.ixora.rdb.annotation;

import tech.intellispaces.jaquarius.annotation.ApplyAdvice;
import tech.intellispaces.jaquarius.ixora.rdb.transaction.TransactionalInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApplyAdvice(adviceClass = TransactionalInterceptor.class)
public @interface Transactional {
}
