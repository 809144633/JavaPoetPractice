package com.yolo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value();
}
