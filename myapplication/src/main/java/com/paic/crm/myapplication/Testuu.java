package com.paic.crm.myapplication;

import com.paic.crm.router.uriannotation.Field;
import com.paic.crm.router.uriannotation.FullUri;
import com.paic.crm.router.uriannotation.Host;
import com.paic.crm.router.uriannotation.ActionId;
import com.paic.crm.router.uriannotation.Schema;
import com.paic.crm.router.uriannotation.Uri;
import com.paic.crm.router.uri.HostType;
import com.paic.crm.router.uri.SchemaType;

/**
 * Created by yueshaojun988 on 2017/12/8.
 */

public interface Testuu {
    @FullUri("http://www.baidu.com")
    void getUri();
    @Uri(schema = SchemaType.LOCAL,host = HostType.VIEW,actionId = "com.paic.crm.router.yyyy")
    void getParam(@Field("signature")String a,@Field("timestamp") int b);
    @Schema(SchemaType.LOCAL)
    @Host(HostType.BLOCK)
    @ActionId("yueshaojun11")
    void bbbb(@Field("signature")String a,@Field("timestamp") int b);
}
