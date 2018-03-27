package tests._sup_java.stubs.utils;

import tests._sup_java.TestJava;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

public class UtilsThreadsStub implements UtilsThreads {

    public long thread_time;
    public long main_time;

    public long timerThread_start;
    public long timerThread_step;
    public long timerThread_time;
    public Subscription timerThread_subscription;
    public CallbackSource<Subscription> timerThread_callbackSource;
    public Callback timerThread_callback;

    public long timerMain_start;
    public long timerMain_step;
    public long timerMain_time;
    public Subscription timerMain_subscription;
    public CallbackSource<Subscription> timerMain_callbackSource;
    public Callback timerMain_callback;

    @Override
    public void thread(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void thread(long l, Runnable runnable) {
        thread_time = l;
        runnable.run();
    }

    @Override
    public void sleep(long l) {

    }

    @Override
    public void main(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void main(boolean b, Runnable runnable) {
        runnable.run();
    }

    @Override
    public void main(long l, Runnable runnable) {
        main_time = l;
        runnable.run();
    }

    @Override
    public void timerThread(long step, CallbackSource<Subscription> callbackSource) {
        timerThread(step, 0, callbackSource, null);
    }

    @Override
    public void timerThread(long step, long time, CallbackSource<Subscription> callbackSource, Callback callback) {
        timerThread_start = TestJava.di.utilsJavaStub.ms;
        timerThread_step = step;
        timerThread_time = time;
        timerThread_subscription = new Subscription();
        timerThread_callbackSource = callbackSource;
        timerThread_callback = callback;
    }

    @Override
    public void timerMain(long step, CallbackSource<Subscription> callbackSource) {
        timerMain(step, 0, callbackSource, null);
    }

    @Override
    public void timerMain(long step, long time, CallbackSource<Subscription> callbackSource, Callback callback) {
        timerMain_start = TestJava.di.utilsJavaStub.ms;
        timerMain_step = step;
        timerMain_time = time;
        timerMain_subscription = new Subscription();
        timerMain_callbackSource = callbackSource;
        timerMain_callback = callback;
    }

    //
    //  TestJava
    //

    public void stepTimerThread(long time) {
        TestJava.di.utilsJavaStub.ms += time;
        timerThread_callbackSource.callback(timerThread_subscription);
        if (timerThread_time != 0 && timerThread_callback != null && timerThread_start > TestJava.di.utilsJavaStub.ms - timerThread_time)
            timerThread_callback.callback();
    }

    public void stepTimerMain(long time) {
        TestJava.di.utilsJavaStub.ms += time;
        timerMain_callbackSource.callback(timerMain_subscription);
        if (timerMain_time != 0 && timerMain_callback != null && timerMain_start > TestJava.di.utilsJavaStub.ms - timerMain_time)
            timerMain_callback.callback();
    }

}
