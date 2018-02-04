package com.paic.crm.router.uri;

import com.paic.crm.router.utils.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/11
 */

public class UriString {
    private StringBuilder completeUri;
    private SchemaType schemaType;
    private HostType hostType;
    private String hostId;
    private LinkedHashMap<String,Object> params = new LinkedHashMap<>(16);
    private UriString(){

    }

    public static class UriBuilder{
        String completeUri;
        SchemaType schemaType;
        HostType hostType;
        String id;
        LinkedHashMap<String,Object> params = new LinkedHashMap<>();
        public UriBuilder schema(SchemaType schemaType){
            this.schemaType = schemaType;
            return this;
        }
        public UriBuilder host(HostType hostType){
            this.hostType = hostType;
            return this;
        }
        public UriBuilder completeUri(String url){
            completeUri = url;
            return this;
        }
        public UriBuilder addParam(String key ,Object value){
            synchronized (params) {
                params.put(key, value);
            }
            return this;
        }
        public UriBuilder id(String id){
            this.id = id;
            return this;
        }
        public UriString build(){

            return applyBuilder();
        }
        private String convertSchema(SchemaType schemaType){
            if(SchemaType.HTTP == schemaType){
                return "http";
            }
            if(SchemaType.HTTPS == schemaType){
                return "https";
            }
            if(SchemaType.LOCAL == schemaType){
                return "local";
            }
            return "";
        }
        private String convertHost(HostType hostType){
            if(HostType.VIEW == hostType){
                return "view.";
            }
            if(HostType.COMPONENT == hostType){
                return "component.";
            }
            if(HostType.BLOCK == hostType){
                return "block.";
            }
            return "";
        }

        private UriString applyBuilder(){
            UriString uriString = new UriString();
            uriString.completeUri = new StringBuilder();
            String schema = convertSchema(schemaType);
            String host = convertHost(hostType);
            if(!StringUtil.isEmpty(completeUri)){
                uriString.completeUri.append(completeUri);
                return uriString;
            }
            if (StringUtil.isEmpty(schema)){
                throw new RuntimeException("no complete uri,schema must not be empty!");
            }
            if (StringUtil.isEmpty(host)){
                throw new RuntimeException("no complete uri,host must not be empty!");
            }
            if (StringUtil.isEmpty(id)){
                throw new RuntimeException("no complete uri,id must not be empty!");
            }
            uriString.schemaType = schemaType;
            uriString.hostType = hostType;
            uriString.hostId = id;
            uriString.params = params;

            uriString.completeUri.append(schema);
            uriString.completeUri.append("://");
            uriString.completeUri.append(host);
            uriString.completeUri.append(id);
            HashMap<String,Object> params = this.params;
            if(params.size()>0){
                uriString.completeUri.append("/?");
                Set<String> keySet = params.keySet();
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    uriString.completeUri.append(key);
                    uriString.completeUri.append("=");
                    uriString.completeUri.append(params.get(key));
                    if(it.hasNext()) {
                        uriString.completeUri.append("&");
                    }
                }
            }
            return uriString;
        }
    }
    public String uriString(){
        return completeUri.toString();
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    public HostType getHostType() {
        return hostType;
    }

    public String getHostId() {
        return hostId;
    }
    public HashMap<String,Object> getParams(){
        return params;
    }
    public Object[] getParamsArgs(){
        Object[] paramsArgs =  new Object[params.size()];
        int i = 0;
        for(String key : params.keySet()){
            paramsArgs[i] = params.get(key);
            i++;
        }
        return paramsArgs;
    }
    public Class<?>[] getParamsType(){
        Class<?>[] paramsKeyType =  new Class[params.size()];
        int i = 0;
        for(String key : params.keySet()){
            Object value = params.get(key);
            paramsKeyType[i] = value.getClass();
            i++;
        }
        return paramsKeyType;
    }
}
