package com.sup.dev.android.utils.implementations;

import android.database.Cursor;

import com.sup.dev.android.utils.interfaces.UtilsCursor;

public class UtilsCursorImpl implements UtilsCursor {

    private final Cursor cursor;

    public UtilsCursorImpl(Cursor cursor){
        this.cursor = cursor;
    }

    public String string(String column){
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public String string(String column, String def){
        int columnIndex = cursor.getColumnIndex(column);
        if(columnIndex == -1)
            return def;
        else
            return cursor.getString(columnIndex);
    }

    public int integer(String column){
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public int integer(String column, int def){
        int columnIndex = cursor.getColumnIndex(column);
        if(columnIndex == -1)
            return def;
        else
            return cursor.getInt(columnIndex);
    }

    public boolean bool(String column){
        return integer(column) == 1;
    }

    public boolean bool(String column, boolean def){
        int columnIndex = cursor.getColumnIndex(column);
        if(columnIndex == -1)
            return def;
        else
            return integer(column) == 1;
    }


}
