package com.sup.dev.android.magic_box;

import android.os.Environment;

import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Miui {

    public static boolean isMiui() {
        return getBuildProperties().containsKey("ro.miui.ui.version.name");
    }

    private static final File BUILD_PROP_FILE = new File(Environment.getRootDirectory(), "build.prop");
    private static Properties sBuildProperties;
    private static final Object sBuildPropertiesLock = new Object();

    private static Properties getBuildProperties() {
        synchronized (sBuildPropertiesLock) {
            if (sBuildProperties == null) {
                sBuildProperties = new Properties();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(BUILD_PROP_FILE);
                    sBuildProperties.load(fis);
                } catch (IOException e) {
                    Debug.INSTANCE.log(e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            Debug.INSTANCE.log(e);
                        }
                    }
                }
            }
        }
        return sBuildProperties;
    }

}
