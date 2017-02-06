package com.work.xinlai.data;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 网络请求数据更新后进行 ObserverCenter.notifyDataChanged(UpdateType.User);即是更新user数据
 **/

/**
 * Created by Administrator on 2016/12/12.
 * 目标对象-subject
 */
public class ObserverCenter {
    private static Collection<Observer> observers = new CopyOnWriteArrayList<>();

    /**
     * 注册
     **/
    public static void rigisterObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * 注销
     **/
    public static void removeObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    /**
     * 观察者中心-根据type提示数据更新
     **/
    public static void notifyDataChanged(UpdateType type) {
        notifyDataChanged(type, null);
    }

    /**
     * 此方法传回数据
     **/
    public static void notifyDataChanged(UpdateType type, Object object) {
        for (Observer observer : observers) {
            observer.onUpdate(type, object);
        }
    }

    /**
     * 观察者中心根据type进行数据更新
     **/
    public enum UpdateType {
        User, Work

    }

    /**
     * 观察者-observer ,实现此接口成为观察者
     **/
    public interface Observer {
        void onUpdate(UpdateType type, Object object);
    }
}
