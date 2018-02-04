package com.paic.crm.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String a = "{\"createTime\":\"1517475797971\",\"customerId\":\"63FA555B54753AF2E053060B1F0A2CDC\",\"customerName\":\"廖翠云_粤B3B7G5_13888888884_标的\",\"dealMsgType\":\"text\",\"ecCaseNo\":\"90500002700021485080_1_粤B3B7G5\",\"eventType\":\"textEvent\",\"fromType\":\"2\",\"messageId\":\"c0ba6a9c-1122-4b7d-b17f-4c18873d4ea9\",\"messageSeq\":64198,\"msg\":\"64845495346495\",\"msgType\":\"text\",\"paImType\":\"09\",\"toType\":\"3\",\"umId\":\"WEIJIAO879\"}";
        CustomMsgContent s = TestClass.handleHttpResult(CustomMsgContent.class,a);
    }
}