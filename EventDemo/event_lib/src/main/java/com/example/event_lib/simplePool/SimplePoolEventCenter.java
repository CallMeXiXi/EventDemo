package com.example.event_lib.simplePool;

import android.text.TextUtils;
import android.util.Log;

import com.example.event_lib.CEvent;
import com.example.event_lib.ICEventListener;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Pools.SimplePool事件分发中心
 */
public class SimplePoolEventCenter {

    private static final String TAG = SimplePoolEventCenter.class.getSimpleName();

    /**
     * 注册监听器列表
     */
    private static final HashMap<String, Object> REGISTER_LISTENER_MAP = new HashMap<>();

//    /**
//     * 取消注册监听器列表
//     */
//    private static final HashMap<String, Object> UNREGISTER_LISTENER_MAP = new HashMap<>();

    /**
     * 监听器列表锁
     */
    private static final Object LISTENER_LOCK = new Object();

    /**
     * 事件对象池
     */
    private static final ExtendSimplePool POOL = new ExtendSimplePool(5);

    /**
     * 注册/注销监听器
     * *
     *
     * @param isBind   true注册  false注销
     * @param listener 监听器
     * @param topic    单个主题
     */
    public static void onBindEvent(boolean isBind, ICEventListener listener, String topic) {
        onBindEvent(isBind, listener, new String[]{topic});
    }

    public static void onBindEvent(boolean isBind, ICEventListener listener, String[] topics) {
        if (isBind) {
            registerEventListener(listener, topics);
        } else {
            unregisterEventListener(listener, topics);
        }
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     * @param topic    主题
     */
    public static void registerEventListener(ICEventListener listener, String topic) {
        registerEventListener(listener, new String[]{topic});
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     * @param topics   多个主题
     */
    public static void registerEventListener(ICEventListener listener, String[] topics) {
        if (null == listener || null == topics) {
            return;
        }

        synchronized (LISTENER_LOCK) {
            for (String topic : topics) {
                if (TextUtils.isEmpty(topic)) {
                    continue;
                }

                Object obj = REGISTER_LISTENER_MAP.get(topic);
                if (null == obj) {
                    // 还没有监听器，直接放到Map集合
                    REGISTER_LISTENER_MAP.put(topic, listener);
                } else if (obj instanceof ICEventListener) {
                    ICEventListener oldListener = (ICEventListener) obj;
                    if (oldListener == listener) {
                        continue;
                    }
                    LinkedList<ICEventListener> list = new LinkedList<>();
                    list.add(oldListener);
                    list.add(listener);
                    REGISTER_LISTENER_MAP.put(topic, list);
                } else if (obj instanceof LinkedList) {
                    // 有多个监听器
                    LinkedList<ICEventListener> listeners = (LinkedList<ICEventListener>) obj;
                    if (listeners.indexOf(listener) >= 0) {
                        continue;
                    }
                    listeners.add(listener);
                }
            }
        }
    }

    /**
     * 注销监听器
     *
     * @param listener 监听器
     * @param topic    主题
     */
    public static void unregisterEventListener(ICEventListener listener, String topic) {
        unregisterEventListener(listener, new String[]{topic});
    }

    /**
     * 注销监听器
     *
     * @param listener 监听器
     * @param topics   多个主题
     */
    public static void unregisterEventListener(ICEventListener listener, String[] topics) {
        if (null == listener || null == topics) {
            return;
        }

        synchronized (LISTENER_LOCK) {
            for (String topic : topics) {
                if (TextUtils.isEmpty(topic)) {
                    continue;
                }
                Object obj = REGISTER_LISTENER_MAP.get(topic);
                if (null == obj) {
                    continue;
                } else if (obj instanceof ICEventListener) {
                    if (obj == listener) {
                        REGISTER_LISTENER_MAP.remove(obj);
                    }
                } else if (obj instanceof LinkedList) {
                    // 有多个监听器
                    LinkedList<ICEventListener> listeners = (LinkedList<ICEventListener>) obj;
                    listeners.remove(listener);
                }
            }
        }
    }

    /**
     * 同步分发事件
     *
     * @param topic      主题
     * @param msgCode    消息类型
     * @param resultCode 预留参数
     * @param obj        回调返回数据
     */
    public static void dispatchEvent(String topic, int msgCode, int resultCode, Object obj) {
        if (!TextUtils.isEmpty(topic)) {
            CEvent event = POOL.acquire();
            event.setTopic(topic);
            event.setMsgCode(msgCode);
            event.setResultCode(resultCode);
            event.setObj(obj);
            dispatchEvent(event);
        }
    }

    /**
     * 同步分发事件
     *
     * @param event
     */
    public static void dispatchEvent(CEvent event) {
        //如果没有监听器，直接退出
        if (REGISTER_LISTENER_MAP.size() == 0) {
            return;
        }

        if (null != event && !TextUtils.isEmpty(event.getTopic())) {
            String topic = event.getTopic();
            // 通知事件监听器处理事件
            ICEventListener listener = null;
            LinkedList<ICEventListener> listeners = null;

            synchronized (LISTENER_LOCK) {
                Log.d(TAG, "dispatchEvent | topic = " + topic + "\tmsgCode = " + event.getMsgCode()
                        + "\tresultCode = " + event.getResultCode() + "\tobj = " + event.getObj());

                Object obj = REGISTER_LISTENER_MAP.get(topic);
                if (null == obj) {
                    return;
                }
                if (obj instanceof ICEventListener) {
                    listener = (ICEventListener) obj;
                } else if (obj instanceof LinkedList) {
                    listeners = (LinkedList<ICEventListener>) obj;
                }
            }
            // 分发事件
            if (null != listener) {
                listener.onCEvent(topic, event.getMsgCode(), event.getResultCode(), event.getObj());
            } else if (null != listeners && listeners.size() > 0) {
                for (ICEventListener l : listeners) {
                    l.onCEvent(topic, event.getMsgCode(), event.getResultCode(), event.getObj());
                }
            }
            // 把对象放回池里面
            POOL.release(event);
        }
    }
}
