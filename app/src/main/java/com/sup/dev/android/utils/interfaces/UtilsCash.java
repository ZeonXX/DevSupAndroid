package com.sup.dev.android.utils.interfaces;

import android.content.Context;

public interface UtilsCash {


    void cacheData(byte[] data, String name);

    byte[] getData(Context context, String name);

}
