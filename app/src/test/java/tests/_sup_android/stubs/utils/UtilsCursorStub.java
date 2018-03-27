package tests._sup_android.stubs.utils;

import com.sup.dev.android.utils.interfaces.UtilsCursor;

public class UtilsCursorStub implements UtilsCursor {
    @Override
    public String string(String column) {
        return null;
    }

    @Override
    public String string(String column, String def) {
        return null;
    }

    @Override
    public int integer(String column) {
        return 0;
    }

    @Override
    public int integer(String column, int def) {
        return 0;
    }

    @Override
    public boolean bool(String column) {
        return false;
    }

    @Override
    public boolean bool(String column, boolean def) {
        return false;
    }
}
