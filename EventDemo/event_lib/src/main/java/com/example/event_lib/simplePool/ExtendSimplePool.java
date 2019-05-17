package com.example.event_lib.simplePool;

import android.support.v4.util.Pools;

import com.example.event_lib.CEvent;

/**
 * 继承系统的Pools.SimplePool线程池
 */
public class ExtendSimplePool extends Pools.SimplePool<CEvent> {

    public ExtendSimplePool(int maxPoolSize) {
        super(maxPoolSize);
    }

    @Override
    public CEvent acquire() {
        CEvent event = super.acquire();
        if (null == event) {
            return new CEvent();
        }
        return event;
    }
}
