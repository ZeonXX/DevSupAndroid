package tests._sup_android.stubs.support;

import com.sup.dev.java.classes.callbacks.simple.CallbackPair;


public class CallbackPairStub<K, N> implements CallbackPair<K, N> {

    public boolean called;
    public K first;
    public N second;

    @Override
    public void callback(K k, N n) {
        called = true;
        this.first = k;
        this.second = n;
    }
}
