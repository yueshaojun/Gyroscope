package com.paic.crm.router.utils;

/**
 * Created by yueshaojun988 on 2017/12/11.
 */

public class StringUtil {
    public static boolean isEmpty(CharSequence sequence){
        if(sequence == null || sequence.length()<=0){
            return true;
        }
        return false;
    }
}
