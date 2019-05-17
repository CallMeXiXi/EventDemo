package com.example.event_lib;

/**
 * 事件对象池
 */
public class CEventPool extends ObjectPool<CEvent> {


    public CEventPool(int capacity) {
        super(capacity);
    }

    @Override
    protected CEvent[] createObjectPool(int capacity) {
        return new CEvent[capacity];
    }

    @Override
    protected CEvent createObject() {
        return new CEvent();
    }
}
