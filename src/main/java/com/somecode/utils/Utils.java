/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * The self-explanatory utils class. Contains the english-locale logging method only so far.
 * @version 1.0
 */

@Service
public class Utils implements ApplicationContextAware {
    private static ApplicationContext ctx;
    private static Locale EN_LOCALE = Locale.ENGLISH;

    public static String getMessage(String s, Object[] objects) {
        return ctx.getMessage(s, objects, EN_LOCALE);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Utils.ctx = ctx;
    }
}
