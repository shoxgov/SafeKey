package com.qingwing.safekey.bean;

/**
 * bit3(1表示没有门磁检测功能。0表示有门磁检测功能,此时bit0有意义)
 * bit2(1表示没有反锁功能,0表示有反锁功能)
 * bit1(0表示未反锁，1表示反锁)
 * bit0(1表示门开着的,0表示门关着的
 * )
 */
public class LockStatusEmue {
    private boolean hasDoorMagnetic;
    private boolean hasBackLock;
    private boolean isBackLock;
    private boolean isOpen;

    public boolean isHasDoorMagnetic() {
        return hasDoorMagnetic;
    }

    public void setHasDoorMagnetic(boolean hasDoorMagnetic) {
        this.hasDoorMagnetic = hasDoorMagnetic;
    }

    public boolean isHasBackLock() {
        return hasBackLock;
    }

    public void setHasBackLock(boolean hasBackLock) {
        this.hasBackLock = hasBackLock;
    }

    public boolean isBackLock() {
        return isBackLock;
    }

    public void setBackLock(boolean backLock) {
        isBackLock = backLock;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
