package com.somecode.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by alexc_000 on 2016-09-21.
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
