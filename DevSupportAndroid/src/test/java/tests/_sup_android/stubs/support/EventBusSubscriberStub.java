package tests._sup_android.stubs.support;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.libs.event_bus.interfaces.EventBusSubscriber;

import java.io.Serializable;


public class EventBusSubscriberStub implements EventBusSubscriber {
    @Override
    public <K> void subscribe(Class<K> aClass, CallbackSource<K> callbackSource) {

    }

    @Override
    public void post(Object o) {

    }

    @Override
    public void postMultiProcess(Serializable serializable) {

    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void onEvent(Object o) {

    }
}
