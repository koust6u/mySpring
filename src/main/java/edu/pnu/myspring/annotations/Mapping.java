package edu.pnu.myspring.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    String method() default "GET";
    String value();

}