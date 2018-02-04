package com.paic.crm.router.router;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;

import com.paic.crm.router.R;
import com.paic.crm.router.provider.ReceiverListener;
import com.paic.crm.router.queue.looper.Handler;
import com.paic.crm.router.queue.looper.Looper;
import com.paic.crm.router.uri.Action;
import com.paic.crm.router.uri.Block;
import com.paic.crm.router.uri.Component;
import com.paic.crm.router.provider.Provider;
import com.paic.crm.router.uri.Response;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 *
 * @author yueshaojun988
 * @date 2017/12/13
 */

public class RouterContainer {
    private static final String TAG = RouterContainer.class.getSimpleName();
    private static final HashMap<String ,Component> COMPONENTS = new HashMap<>(16);
    private static final HashMap<String ,Block> BLOCKS = new HashMap<>(16);
    private static final HashMap<String ,Object> PROVIDERS = new HashMap<>(16);
    private static ArrayList<ReceiverListener> receiverListeners = new ArrayList<>(16) ;
    public static void parse(Context context) {
        InputStream configIs = RouterManager.getInstance().getConfig();
        if(configIs ==null){
            final TypedValue value = new TypedValue();
            configIs = context.getResources().openRawResource(
               R.raw.routerconfig, value);
        }
        parse(context,configIs);
    }
    public static void parse(Context context, InputStream config){
        Log.d(TAG,"开始解析。。。");
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder =null;
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(documentBuilder==null){
            return;
        }
        Document document = null;
        try {
            document = documentBuilder.parse(config);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        if(document == null){
            return;
        }

        parseComponent(document);
        parseBlock(document);
        parseProvider(context,document);
        Log.d(TAG,"解析完成。。。");
    }

    private static void parseProvider(Context context,Document document) {
        NodeList providers = document.getElementsByTagName("provider");
        if(providers.getLength()<=0){
            return;
        }
        for(int i = 0 ;i<providers.getLength();i++){
            Node node = providers.item(i);
            Provider provider = new Provider();
            NamedNodeMap attributes = node.getAttributes();
            provider.name = attributes.getNamedItem("name").getNodeValue();
            provider.path = attributes.getNamedItem("path").getNodeValue();
            Object providerObj = initProvider(context,provider);
            NodeList childNodes = node.getChildNodes();
            for(int j = 0;j<childNodes.getLength();j++){
                Node childNode = childNodes.item(j);
                String nodeName = childNode.getNodeName();
                if("notification".equalsIgnoreCase(nodeName)){
                    String notificationPath = childNode.getAttributes().getNamedItem("path").getNodeValue();
                    try {
                        Class notificationClass = Class.forName(notificationPath);
                        Method method = providerObj.
                                getClass().
                                getMethod("add"+getNotificationName(notificationPath),notificationClass);
                        Object notification =
                                Proxy.newProxyInstance(notificationClass.getClassLoader(), new Class[]{notificationClass}, new InvocationHandler() {
                                    @Override
                                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                        Log.d(TAG,"invoke");
                                        if(method.getName().contains("on")){
                                            Log.d(TAG,"invoke params" + args[0]);
                                            deliverToListener(method,args);
                                        }
                                        return null;
                                    }
                                });
                        method.invoke(providerObj,notification);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            PROVIDERS.put(provider.name,providerObj);
        }
    }

    private static void deliverToListener(Method method, Object[] args) {
        for (ReceiverListener l : receiverListeners){
            l.onReceive(method.getName(),args);
        }
    }

    private static String getNotificationName(String notificationPath) {
        String[] paths = notificationPath.split("\\.");
        return paths[paths.length-1];
    }

    private static Object initProvider(final Context context , Provider provider) {
        Object providerObj = null;
        try {
            providerObj = Class.forName(provider.path).newInstance();
            final Method init = providerObj.getClass().getDeclaredMethod("init",Context.class);
            final Object finalProviderObj = providerObj;
            invoke(finalProviderObj,init,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return providerObj;
    }

    private static void parseBlock(Document document) {
        NodeList blocks = document.getElementsByTagName("block");
        if(blocks.getLength()<=0){
            return;
        }
        for(int i = 0 ;i<blocks.getLength();i++){
            Node node = blocks.item(i);
            Block block = new Block();
            NamedNodeMap attributes = node.getAttributes();
            block.name = attributes.getNamedItem("name").getNodeValue();
            if(attributes.getNamedItem("path") == null || "".equals(attributes.getNamedItem("path").getNodeValue())){
                NamedNodeMap parentAttributes = node.getParentNode().getAttributes();
                block.path = parentAttributes.getNamedItem("path").getNodeValue();
                block.prarentName = parentAttributes.getNamedItem("name").getNodeValue();
            }else {
                block.path = attributes.getNamedItem("path").getNodeValue();
            }
            NodeList childNodes = node.getChildNodes();
            for(int j = 0;j<childNodes.getLength();j++){
                Node childNode = childNodes.item(j);
                String nodeName = childNode.getNodeName();
                if("action".equalsIgnoreCase(nodeName)){
                    Action action = new Action();
                    action.name = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    block.action = action;
                }
            }
            String blockKey = block.action.name;
            BLOCKS.put(blockKey,block);
        }
    }

    private static void parseComponent(Document document) {
        NodeList components = document.getElementsByTagName("component");
        if(components.getLength()<=0){return;}

        for (int i = 0 ;i<components.getLength();i++){
            Node node = components.item(i);

            NodeList childNodes = node.getChildNodes();
            Component component = new Component();

            NamedNodeMap attributes = node.getAttributes();
            component.name = attributes.getNamedItem("name").getNodeValue();
            component.path = attributes.getNamedItem("path").getNodeValue();

            for(int j = 0;j<childNodes.getLength();j++) {
                Node childNode = childNodes.item(j);
                String nodeName = childNode.getNodeName();

                if("action".equalsIgnoreCase(nodeName)){
                    Action action = new Action();
                    action.name = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    component.action = action;
                }
                if("response".equalsIgnoreCase(nodeName)){
                    Response response = new Response();
                    response.path = childNode.getAttributes().getNamedItem("path").getNodeValue();
                    component.response = response;
                }
            }

            COMPONENTS.put(component.action.name,component);
        }
    }

    private static void invoke(final Object receiver, final Method method, final Object ... params){
        try {
            method.invoke(receiver,params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Component getComponent(String actionName){
       return COMPONENTS.get(actionName);
    }
    public static Block getBlock(String actionName){
        return BLOCKS.get(actionName);
    }
    public static void addReceiverListener(@NonNull ReceiverListener l){
        receiverListeners.add(l);
    }
    public static Object getProvider(String providerName){
        return PROVIDERS.get(providerName);
    }
}
