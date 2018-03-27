package tests._sup_java;

import com.sup.dev.java.app.SupJava;

public class TestJava {

    public static SupJavaDIStub di;

    public static void init() {
        init(new SupJavaDIStub());
    }

    public static void init(SupJavaDIStub di) {
        TestJava.di = di;
        SupJava.di = di;
    }
}
