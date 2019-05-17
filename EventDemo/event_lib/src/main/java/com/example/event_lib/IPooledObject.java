package com.example.event_lib;

/**
 * 对象池中的对象要求实现PooledObject接口
 */
public interface IPooledObject {
    /**
     * 重置到默认状态
     */
    void reset();
}
