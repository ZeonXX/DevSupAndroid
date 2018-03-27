package tests._sup_android.stubs.utils;

import android.content.Context;
import android.content.Intent;

import com.sup.dev.android.utils.interfaces.UtilsAndroid;

public class UtilsAndroidStub implements UtilsAndroid {
    @Override
    public String getLanguageCode() {
        return null;
    }

    @Override
    public boolean isEchoCancelerAvailable() {
        return false;
    }

    @Override
    public boolean isDirectToTV() {
        return false;
    }

    @Override
    public boolean appIsVisible() {
        return false;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public boolean isMainThread() {
        return false;
    }

    @Override
    public long getMaxMemory() {
        return 0;
    }

    @Override
    public long getFreeMemory() {
        return 0;
    }

    @Override
    public boolean isMiui() {
        return false;
    }

    @Override
    public String getCurrentProcess() {
        return null;
    }

    @Override
    public boolean hasBroadcastReceiver(String process, Intent intent, Context context) {
        return false;
    }

    @Override
    public boolean checkServiceStarted(String appId, String serviceName) {
        return false;
    }

    @Override
    public boolean isScreenPortrait() {
        return false;
    }

    @Override
    public boolean isScreenLandscape() {
        return false;
    }

    @Override
    public int getScreenOrientation() {
        return 0;
    }

    @Override
    public int getScreenW() {
        return 0;
    }

    @Override
    public int getScreenH() {
        return 0;
    }

    @Override
    public int maxScreenSide() {
        return 0;
    }

    @Override
    public int minScreenSide() {
        return 0;
    }

    @Override
    public boolean isScreenKeyLocked() {
        return false;
    }

    @Override
    public void screenOn() {

    }
}
