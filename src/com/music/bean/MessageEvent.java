package com.music.bean;

public class MessageEvent {
    public String type;
    public Object data;
    public MessageEvent(String type){
        this.type=type;
    }
    public MessageEvent(String type,Object data){
        this.type=type;
        this.data=data;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}