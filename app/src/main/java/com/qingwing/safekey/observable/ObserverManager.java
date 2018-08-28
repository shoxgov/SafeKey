package com.qingwing.safekey.observable;

import java.util.Observable;

/**
 */
public class ObserverManager extends Observable {

    private static ObserverManager instance = null;

    public static ObserverManager getObserver() {
        if (null == instance) {
            instance = new ObserverManager();
        }
        return instance;
    }

    public void setMessage(Object data) {
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(data);
    }
}
