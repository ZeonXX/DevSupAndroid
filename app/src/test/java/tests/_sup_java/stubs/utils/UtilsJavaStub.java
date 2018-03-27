package tests._sup_java.stubs.utils;

import com.sup.dev.java.utils.interfaces.UtilsJava;


public class UtilsJavaStub implements UtilsJava {

    public long ms;

    @Override
    public long timeMs() {
        return ms;
    }
}
