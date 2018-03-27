package tests._sup_java;

import tests._sup_java.stubs.event_bus.EventBusStub;
import tests._sup_java.stubs.event_bus.EventBusSubscriberStub;
import tests._sup_java.stubs.utils.UtilsJavaStub;
import tests._sup_java.stubs.utils.UtilsNetworkStub;
import tests._sup_java.stubs.utils.UtilsThreadsStub;
import com.sup.dev.java.app.SupJavaDI;
import com.sup.dev.java.libs.event_bus.interfaces.EventBus;
import com.sup.dev.java.libs.event_bus.interfaces.EventBusSubscriber;
import com.sup.dev.java.utils.interfaces.UtilsJava;
import com.sup.dev.java.utils.interfaces.UtilsNetwork;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

public class SupJavaDIStub implements SupJavaDI{

    public EventBusStub eventBusStub = new EventBusStub();
    public EventBusSubscriberStub lastEventBusSubscriberStub = new EventBusSubscriberStub();

    public UtilsThreadsStub utilsThreadsStub = new UtilsThreadsStub();
    public UtilsJavaStub utilsJavaStub = new UtilsJavaStub();
    public UtilsNetworkStub utilsNetworkStub = new UtilsNetworkStub();


    //
    //  EventBus
    //

    @Override
    public EventBus eventBusRoot() {
        return eventBusStub;
    }

    @Override
    public EventBusSubscriber eventBus() {
        lastEventBusSubscriberStub = new EventBusSubscriberStub();
        return lastEventBusSubscriberStub;
    }

    @Override
    public EventBusSubscriber eventBus(Object o) {
        lastEventBusSubscriberStub = new EventBusSubscriberStub();
        return lastEventBusSubscriberStub;
    }

    //
    //  Utils
    //

    @Override
    public UtilsNetwork utilsNetwork() {
        return utilsNetworkStub;
    }

    @Override
    public UtilsThreads utilsThreads() {
        return utilsThreadsStub;
    }

    @Override
    public UtilsJava utilsJava() {
        return utilsJavaStub;
    }
}
