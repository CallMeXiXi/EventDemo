package com.example.event_lib;

/**
 * @Description: 自定义的对象池
 */
public abstract class ObjectPool<T extends IPooledObject> {

    private T[] container;//对象数组
    private final Object LOCK = new Object();//对象锁
    private int length; //每次返回对象都放到数据末端，length表示前面可用对象数

    public ObjectPool(int capacity) {
        this.container = createObjectPool(capacity);
    }

    /**
     * 从对象池总获取一个对象，如果池已满，会重新创建一个对象
     *
     * @return
     */
    public T get() {
        T obj = findFreeObject();
        if (null == obj) {
            obj = createObject();
        } else {
            // 清除对象状态
            obj.reset();
        }
        return obj;
    }

    /**
     * 从池中找到空闲的对象
     *
     * @return
     */
    public T findFreeObject() {
        T obj = null;
        synchronized (LOCK) {
            if (length > 0) {
                --length;
                obj = container[length];
                // 赋值完成后，释放资源
                container[length] = null;
            }
        }
        return obj;
    }

    public void returnObject(T obj) {
        synchronized (LOCK) {
            int size = container.length;
            if (length < size) {
                container[length] = obj;
                length++;
            }
        }
    }

    /**
     * 创建对象池
     *
     * @param capacity 对象池容量
     * @return
     */
    abstract T[] createObjectPool(int capacity);

    /**
     * 创建一个对象
     *
     * @return
     */
    abstract T createObject();
}
