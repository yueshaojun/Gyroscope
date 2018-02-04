package com.paic.crm.router.uriannotation;

import com.paic.crm.router.uri.HostType;
import com.paic.crm.router.uri.SchemaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yueshaojun988 on 2017/12/7.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Uri {
    SchemaType schema();
    HostType host();
    String actionId();
}
