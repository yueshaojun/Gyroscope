package com.paic.crm.router.router;

import com.paic.crm.router.uriannotation.Delay;
import com.paic.crm.router.uriannotation.Field;
import com.paic.crm.router.uriannotation.FullUri;
import com.paic.crm.router.uriannotation.Host;
import com.paic.crm.router.uriannotation.ActionId;
import com.paic.crm.router.uriannotation.Schema;
import com.paic.crm.router.uriannotation.Uri;
import com.paic.crm.router.queue.Task;
import com.paic.crm.router.uri.HostType;
import com.paic.crm.router.uri.SchemaType;
import com.paic.crm.router.uri.UriString;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/8
 */

public class Router {
    public Router(){
    }
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                UriString.UriBuilder builder = new UriString.UriBuilder();
                parseUri(method,builder);
                parseSchemaAndHost(method,builder);
                parseActionId(method,builder);
                parseParams(method,objects,builder);

                RouterManager.getInstance().getLooperHandler().
                        postDelay(new Task(RouterManager.getInstance().getApplication(),
                                builder.build()),
                                parseDelay(method));
                System.out.println(builder.build().uriString());
                return null;
            }
        });
    }

    private boolean parseUri(Method method, UriString.UriBuilder builder) {
        Uri uri = method.getAnnotation(Uri.class);
        if(uri == null){
            return false;
        }
        builder.host(uri.host()).schema(uri.schema()).id(uri.actionId());
        return true;
    }

    private boolean parseActionId(Method method, UriString.UriBuilder builder) {
        ActionId id = method.getAnnotation(ActionId.class);
        if(id ==null){
            return false;
        }
        String hostId = id.value();
        builder.id(hostId);
        return true;
    }

    private void parseSchemaAndHost(Method m, UriString.UriBuilder builder){
        Schema schema = m.getAnnotation(Schema.class);
        Host host = m.getAnnotation(Host.class);
        if(schema!=null&&host!=null){
            SchemaType schemaType = schema.value();
            HostType hostType = host.value();
            builder.schema(schemaType).host(hostType);
        }
    }
    private void parseParams(Method m, Object[] args, UriString.UriBuilder builder){
        Annotation[][] fields = m.getParameterAnnotations();
        for(int i = 0;i < fields.length;i++){
            for (int j = 0 ;j < fields[i].length;j++) {
                Field field = (Field) fields[i][j];
                builder.addParam(field.value(),args[i]);
            }
        }
    }
    private boolean parseFullUri(Method m, UriString.UriBuilder builder){
        FullUri fullUri = m.getAnnotation(FullUri.class);
        if(fullUri ==null){
           return false;
        }
        builder.completeUri(fullUri.value());
        return true;
    }
    private long parseDelay(Method method) {
        Delay delay = method.getAnnotation(Delay.class);
        if(delay!=null){
           return delay.value();
        }
        return 0;
    }
}
