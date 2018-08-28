package com.qingwing.safekey.dialog;

import android.content.Context;


public class WaitTool {
    private static CustomProgressDialog waitingDialog;

    public static void showDialog(Context cont) {
        showDialog(cont, "正在加载中...", false);
    }

    public static void showDialog(Context cont, String msg) {
        showDialog(cont, msg, false);
    }

    public static void showDialog(Context cont, String msg, boolean outside) {
        try {
            if (waitingDialog == null) {
                waitingDialog = CustomProgressDialog.createDialog(cont);
            }
            waitingDialog.setCanceledOnTouchOutside(outside);
            waitingDialog.setMessage(msg);
            waitingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialogNotcancel(Context cont, String msg,
                                           boolean cancelflag) {
        try {
            msg = msg == null ? "正在加载中..." : msg;
            if (waitingDialog == null) {
                waitingDialog = CustomProgressDialog.createDialog(cont,
                        cancelflag);
            }
            waitingDialog.setCanceledOnTouchOutside(false);
            waitingDialog.setMessage(msg);
            waitingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialog() {
        try {
            if (waitingDialog != null) {
                waitingDialog.cancelDismiss();
                waitingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCanceHandler(CustomProgressDialog.IWaitParent parent) {
        waitingDialog.setCanceHandler(parent);
    }

}