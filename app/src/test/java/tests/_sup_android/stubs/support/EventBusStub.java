package tests._sup_android.stubs.support;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.libs.event_bus.interfaces.EventBus;
import com.sup.dev.java.libs.event_bus.interfaces.EventBusSubscriber;

import java.io.Serializable;

public class EventBusStub implements EventBus {
    @Override
    public void setPostMultiProcessCallback(CallbackSource<Serializable> callbackSource) {

    }

    @Override
    public <K> void subscribeHard(Class<K> aClass, CallbackSource<K> callbackSource) {

    }

    @Override
    public <K> void subscribeHard(Object o, Class<K> aClass, CallbackSource<K> callbackSource) {

    }

    @Override
    public void subscribe(EventBusSubscriber eventBusSubscriber) {

    }

    @Override
    public void unsubscribe(Object o) {

    }

    @Override
    public void post(Object o) {

    }

    @Override
    public void postMultiprocess(Serializable serializable) {

    }
}
