package com.paic.crm.myapplication;

import android.support.annotation.NonNull;


import java.io.Serializable;

/**
 * Created by hanyh on 16/3/23.
 *
 *   "createTime": "1496887525000",
 "customerId": "50DD412D250337D6E053060B1F0AE1FA",
 "dialogId": "50DD24AD8E495EE9E054001517EE10A6",
 "fromType": "3",
 "messageId": "F1E369A3-4DFC-4A76-94C8-6B36EEB97465",
 "messageSeq": "36596",
 "msg": " 1",
 "msgType": "text",
 "nowPage": null,
 "paImType": "01",
 "sendTime": 1496887525000,
 "toType": "2",
 "umId": "CUIJUN629",
 "umName": null
 *
 */
public class CustomMsgContent implements Comparable<CustomMsgContent>,Cloneable,Serializable{

    public int id;
    public String customerName;//客户昵称
    public String customerIcon;//客户头像地址
    public String customerId;// 客户ID
    public String msg;
    public String msgType;//文本类型（text:文本，image:图片，audio:语音，h5,video:视频等)
    public String paImType;//客户来源（客户im类型01微信，02-微博,03-android应用）
    public long messageSeq;
    public String umId;//坐席UMID
    public String msgId;//后台识别的id
    public String createTime;
    public String fromType;
    public String messageId;//社交云识别的id
    public String nowPage;
    public String toType;
    public int msgState;
    public boolean isRead;
    public boolean isPlayed;//如果是语音的话，是否播放过
    public int duration;//语音时长
    public String localPath;
    public String ecCaseNo;//e理赔案件号


    @Override
    public int compareTo(@NonNull CustomMsgContent another) {
        String lhsTime = this.createTime;
        String rhsTime = another.createTime;
        Double v1 = Double.valueOf(lhsTime);
        Double v2 = Double.valueOf(rhsTime);
        if (v1 == v2) {
            return 0;
        } else if (v1 > v2) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        String jsonString = "{id:"+id+",customerName:"+customerName+",customerIcon:"+customerIcon+",customerId:"+customerId
                +",msg:"+msg+",msgType:"+msgType+",paImType:"+paImType+",umId:"+umId+",createTime:"+createTime+",fromType:"+fromType+",messageId:"
                +messageId+",msgState:"+msgState+"，msgId:"+msgId+"，localPath:"+localPath;
        return jsonString ;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
