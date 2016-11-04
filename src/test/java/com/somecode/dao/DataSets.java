/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a test method that uses the {@link #before()} parameter
 * to set the initial DB state and the {@link #after()} parameter to set the expected DB state after the method that
 * is tested runs.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSets {
    String before() default "";
    String after() default "";
}
