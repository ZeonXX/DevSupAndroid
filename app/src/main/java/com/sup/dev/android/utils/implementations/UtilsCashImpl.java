package com.sup.dev.android.utils.implementations;

import android.content.Context;
import android.util.Pair;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsCash;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class UtilsCashImpl implements UtilsCash {

    private static final String SPLITTER = "_";

    private final long cashSize;
    private final long overClearSize;
    private final int orderClearMaxCount;

    public UtilsCashImpl(long cashSize) {
        this(cashSize, cashSize / 2);
    }

    public UtilsCashImpl(long cashSize, long overClearSize) {
        this(cashSize, overClearSize, 1000);
    }

    public UtilsCashImpl(long cashSize, long overClearSize, int orderClearMaxCount) {
        this.cashSize = cashSize;
        this.overClearSize = overClearSize;
        this.orderClearMaxCount = orderClearMaxCount;
    }

    public void cacheData(byte[] data, String name) {

        File cacheDir = SupAndroid.di.appContext().getCacheDir();
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

        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files)
            if (file.isFile())
                size += file.length();

        return size;
    }

    private long getDate(String name) {
        return Long.parseLong(name.substring(0, name.indexOf(SPLITTER)));
    }

}
