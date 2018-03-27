package tests._sup_android.stubs.support;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class CallbackSourceStub<K> implements CallbackSource<K>{

    public boolean called;
    public K value;

    @Override
    public void callback(K value) {
        this.called = true;
        this.value = value;
    }
}
