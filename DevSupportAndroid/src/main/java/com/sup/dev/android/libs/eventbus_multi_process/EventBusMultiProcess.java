package com.sup.dev.android.libs.eventbus_multi_process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.libs.event_bus.EventBus;

import java.io.Serializable;

public class EventBusMultiProcess extends BroadcastReceiver {

    private static final String MULTI_PROCESS_INTENT_ACTION = "multi_process_event";
    private static final String MULTI_PROCESS_INTENT_EXTRA = "event";

    public static void init(){

        EventBus.setPostMultiProcessCallback(EventBusMultiProcess::post);

        if (!isRegisteredInManifest(SupAndroid.appContext)) {
            IntentFilter filter = new IntentFilter(EventBusMultiProcess.MULTI_PROCESS_INTENT_ACTION);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            SupAndroid.appContext.registerReceiver(new EventBusMultiProcess(), filter);
        }
    }

    private static Intent getBaseIntent() {
        Intent intent = new Intent();
        intent.setAction(MULTI_PROCESS_INTENT_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    public static void post(Serializable event) {
        Intent broadcastIntent = getBaseIntent();
        broadcastIntent.putExtra(MULTI_PROCESS_INTENT_EXTRA, event);
        SupAndroid.appContext.sendBroadcast(broadcastIntent);
    }

    private static boolean isRegisteredInManifest(Context context) {
        Intent intent = getBaseIntent();
        String process = ToolsAndroid.getProcessName();
        return ToolsAndroid.hasBroadcastReceiver(process, intent, context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Object event = intent.getSerializableExtra(EventBusMultiProcess.MULTI_PROCESS_INTENT_EXTRA);
            EventBus.post(event);
        } catch (Exception ex) {
            Debug.log(ex);
        }
    }


}