package com.example.event_lib;

/**
 * @Description: 事件模型
 */
public class CEvent implements IPooledObject {

    private String topic; //主题
    private int msgCode; //消息类型
    private int resultCode; //预留参数
    private Object obj; //回调返回数据

    public CEvent() {
    }

    public CEvent(String topic, int msgCode, int resultCode, Object obj) {
        this.topic = topic;
        this.msgCode = msgCode;
        this.resultCode = resultCode;
        this.obj = obj;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public void reset() {
        this.topic = "";
        this.msgCode = 0;
        this.resultCode = 0;
        this.obj = null;
    }
}
