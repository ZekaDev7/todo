package com.example.practice;

import android.content.Context;

import io.objectbox.BoxStore;
import io.objectbox.android.Admin;


public class ObjectBox {

    public static BoxStore boxStore;

    public static void init(Context context) {

        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        boolean started = new Admin(boxStore).start(context);
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
