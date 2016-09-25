package com.somecode.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by alexc_000 on 2016-09-21.
 */
@Service
public class Helper implements ApplicationContextAware {
    private static ApplicationContext ctx;
    private static Locale US_LOCALE = Locale.US;

    public static String getMessage(String s, Object[] objects) {
        return ctx.getMessage(s, objects, US_LOCALE);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Helper.ctx = ctx;
    }
}
