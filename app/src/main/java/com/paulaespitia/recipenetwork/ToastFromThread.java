package com.paulaespitia.recipenetwork;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastFromThread {

    public static void Toast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}