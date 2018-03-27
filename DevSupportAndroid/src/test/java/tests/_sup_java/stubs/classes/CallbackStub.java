package tests._sup_java.stubs.classes;

import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CallbackStub implements Callback {

    public boolean called;

    @Override
    public void callback() {
        this.called = true;
    }
}
