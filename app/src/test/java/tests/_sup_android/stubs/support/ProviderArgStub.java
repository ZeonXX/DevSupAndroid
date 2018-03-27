package tests._sup_android.stubs.support;

import com.sup.dev.java.classes.providers.ProviderArg;

public class ProviderArgStub<A, I> implements ProviderArg<A, I> {

    public boolean called;
    public A lastArgument;
    public I item;

    public ProviderArgStub(I item){
        this.item = item;
    }

    @Override
    public I provide(A o) {
        called = true;
        lastArgument = o;
        return item;
    }
}
