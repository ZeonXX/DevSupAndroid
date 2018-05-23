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

public class MultiProcessEventBus extends BroadcastReceiver {

    private static final String MULTI_PROCESS_INTENT_ACTION = "multi_process_event";
    private static final String MULTI_PROCESS_INTENT_EXTRA = "event";

    private final Context context = SupAndroid.appContext;

    public MultiProcessEventBus(){

        EventBus.setPostMultiProcessCallback(this::post);

        if (!isRegisteredInManifest(context)) {
            IntentFilter filter = new IntentFilter(MultiProcessEventBus.MULTI_PROCESS_INTENT_ACTION);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            context.registerReceiver(new MultiProcessEventBus(), filter);
        }
    }

    private Intent getBaseIntent() {
        Intent intent = new Intent();
        intent.setAction(MULTI_PROCESS_INTENT_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    public void post(Serializable event) {
        Intent broadcastIntent = getBaseIntent();
        broadcastIntent.putExtra(MULTI_PROCESS_INTENT_EXTRA, event);
        context.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Object event = intent.getSerializableExtra(MultiProcessEventBus.MULTI_PROCESS_INTENT_EXTRA);
            EventBus.post(event);
        } catch (Exception ex) {
            Debug.log(ex);
        }
    }

    private boolean isRegisteredInManifest(Context context) {
        Intent intent = getBaseIntent();
        String process = ToolsAndroid.getCurrentProcess();
        return ToolsAndroid.hasBroadcastReceiver(process, intent, context);
    }
}
