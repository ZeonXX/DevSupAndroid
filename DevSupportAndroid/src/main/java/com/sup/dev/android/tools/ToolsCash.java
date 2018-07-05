package com.sup.dev.android.tools;

import android.content.Context;
import android.util.Pair;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ToolsCash{

    private static final String SPLITTER = "_";

    private static long cashSize;
    private static long overClearSize;
    private static int orderClearMaxCount;

    public static void init() {
        init(1024 * 1024 * 20);
    }

    public static void init(long cashSize) {
        init(cashSize, cashSize / 2);
    }

    public static void init(long cashSize, long overClearSize) {
        init(cashSize, overClearSize, 1000);
    }

    public static void init(long cashSize, long overClearSize, int orderClearMaxCount) {
        if(cashSize != 0) throw new RuntimeException("Already init");
        ToolsCash.cashSize = cashSize;
        ToolsCash.overClearSize = overClearSize;
        ToolsCash.orderClearMaxCount = orderClearMaxCount;
    }

    public void cacheData(byte[] data, String name) {
        if(cashSize == 0) init();

        File cacheDir = SupAndroid.appContext.getCacheDir();
        long size = getDirSize(cacheDir);
        long newSize = data.length + size;

        if (newSize > cashSize) cleanDir(cacheDir, newSize - cashSize + overClearSize);

        File file = new File(cacheDir, System.currentTimeMillis() + SPLITTER + name);

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
            os.flush();
        } catch (IOException ex) {
            Debug.log(ex);
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    Debug.log(e);
                }
        }
    }

    public byte[] getData(Context context, String name) {
        if(cashSize == 0) init();

        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, name);

        if (!file.exists()) return null;


        byte[] data = new byte[(int) file.length()];
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            is.read(data);
        } catch (IOException ex) {
            Debug.log(ex);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Debug.log(e);
            }
        }
        return data;
    }

    private void cleanDir(File dir, long bytes) {
        if(cashSize == 0) init();

        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        if (files.length <= orderClearMaxCount) {

            ArrayList<Pair<Long, File>> keys = new ArrayList<>();
            ArrayList<Long> sorted = new ArrayList<>();

            for (File file : files) {
                long date = getDate(file.getName());
                sorted.add(date);
                keys.add(new Pair<>(date, file));
            }

            Collections.sort(sorted);

            for (int i = sorted.size() - 1; i > -1; i--) {
                for (Pair<Long, File> pair : keys)
                    if (pair.first.longValue() == sorted.get(i)) {
                        keys.remove(pair);
                        if (pair.second.delete())
                            bytesDeleted += pair.second.length();
                        break;
                    }
                if (bytesDeleted >= bytes) break;
            }
        } else {
            for (File file : files) {
                if (file.delete()) bytesDeleted += file.length();
                if (bytesDeleted >= bytes) break;
            }
        }
    }

    private long getDirSize(File dir) {
        if(cashSize == 0) init();

        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files)
            if (file.isFile())
                size += file.length();

        return size;
    }

    private long getDate(String name) {
        if(cashSize == 0) init();
        return Long.parseLong(name.substring(0, name.indexOf(SPLITTER)));
    }

}
