package com.paic.crm.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by yueshaojun988 on 2017/12/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface IntentField {
    String name();
}
