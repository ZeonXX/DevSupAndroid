package com.sup.dev.android.utils.interfaces;

public interface UtilsCursor {

    String string(String column);

    String string(String column, String def);

    int integer(String column);

    int integer(String column, int def);

    boolean bool(String column);

    boolean bool(String column, boolean def);

}
