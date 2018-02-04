package com.paic.crm.router.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.paic.crm.router.uri.Block;
import com.paic.crm.router.uri.HostType;
import com.paic.crm.router.uri.SchemaType;
import com.paic.crm.router.uri.Component;
import com.paic.crm.router.uri.UriString;
import com.paic.crm.router.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/12
 */

public class Dispatcher {
    public static void dispatch(Context context, UriString uriString){
        Log.d("Dispatcher",Thread.currentThread().getName()+"||uri:"+uriString);
        SchemaType schemaType = uriString.getSchemaType();
        if(SchemaType.LOCAL == schemaType){
            HostType hostType = uriString.getHostType();
            if(HostType.VIEW == hostType){
                performJumpToActivity(context ,uriString);
            }
            if(HostType.COMPONENT == hostType){
                performDeliverToComponent(uriString);
            }
            if(HostType.BLOCK == hostType){
                performDeliverToBlock(uriString);
            }
        }
    }

    private static void performDeliverToBlock(UriString uriString) {
        String actionId = uriString.getHostId();
        Block block = RouterContainer.getBlock(actionId);
        if(block == null){
            return;
        }
        Object receiverObj = null;
        if(!StringUtil.isEmpty(block.prarentName)){
            receiverObj = RouterContainer.getProvider(block.prarentName);
        }else {
            try {
                receiverObj = Class.forName(block.path).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(receiverObj == null){
            return;
        }
        try {
            Method method = receiverObj.getClass().getDeclaredMethod(block.name,uriString.getParamsType());
            method.invoke(receiverObj,uriString.getParamsArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void performDeliverToComponent(UriString uriString) {
        String actionId = uriString.getHostId();
        Component targetComponent = RouterContainer.getComponent(actionId);
    }

    private static void performJumpToActivity(Context context,UriString uriString) {
        Intent jumpIntent = new Intent();
        if(!(context instanceof Activity)){
            jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        jumpIntent.setAction(uriString.getHostId());
        jumpIntent.putExtras(assembleParamsType(uriString.getParams()));
        context.startActivity(jumpIntent);
    }
    private static Bundle assembleParamsType(HashMap<String,Object> serializedParams){
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : serializedParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Integer) {
                bundle.putInt(key, Integer.parseInt(value.toString()));
            } else if (value instanceof Long) {
                bundle.putLong(key, Long.parseLong(value.toString()));
            } else if (value instanceof Double) {
                bundle.putDouble(key, Double.parseDouble(value.toString()));
            } else if (value instanceof Short) {
                bundle.putShort(key, Short.parseShort(value.toString()));
            } else if (value instanceof Float) {
                bundle.putFloat(key, Float.parseFloat(value.toString()));
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof CharSequence) {
                bundle.putCharSequence(key, (CharSequence) value);
            } else if (value.getClass().isArray()) {
                if (int[].class.isInstance(value)) {
                    bundle.putIntArray(key, (int[]) value);
                } else if (long[].class.isInstance(value)) {
                    bundle.putLongArray(key, (long[]) value);
                } else if (double[].class.isInstance(value)) {
                    bundle.putDoubleArray(key, (double[]) value);
                } else if (short[].class.isInstance(value)) {
                    bundle.putShortArray(key, (short[]) value);
                } else if (float[].class.isInstance(value)) {
                    bundle.putFloatArray(key, (float[]) value);
                } else if (String[].class.isInstance(value)) {
                    bundle.putStringArray(key, (String[]) value);
                } else if (CharSequence[].class.isInstance(value)) {
                    bundle.putCharSequenceArray(key, (CharSequence[]) value);
                } else if (Parcelable[].class.isInstance(value)) {
                    bundle.putParcelableArray(key, (Parcelable[]) value);
                }
            } else if (value instanceof ArrayList && !((ArrayList) value).isEmpty()) {
                ArrayList list = (ArrayList) value;
                if (list.get(0) instanceof Integer) {
                    bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
                } else if (list.get(0) instanceof String) {
                    bundle.putStringArrayList(key, (ArrayList<String>) value);
                } else if (list.get(0) instanceof CharSequence) {
                    bundle.putCharSequenceArrayList(key, (ArrayList<CharSequence>) value);
                } else if (list.get(0) instanceof Parcelable) {
                    bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                }
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            } else if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            } else {
                throw new IllegalArgumentException("不支持的参数类型！");
            }
        }
        return bundle;
    }
}
