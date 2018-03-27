package tests._sup_java.stubs.utils;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.libs.json.Json;
import com.sup.dev.java.utils.interfaces.UtilsNetwork;

import java.io.IOException;
import java.net.Socket;


public class UtilsNetworkStub implements UtilsNetwork {
    @Override
    public byte[] getBytesFromURL(String s) throws IOException {
        return new byte[0];
    }

    @Override
    public void getBytesFromURL(String s, CallbackSource<byte[]> callbackSource) {

    }

    @Override
    public Json read(Socket socket) throws IOException {
        return null;
    }

    @Override
    public void write(Socket socket, byte[] bytes) throws IOException {

    }
}
