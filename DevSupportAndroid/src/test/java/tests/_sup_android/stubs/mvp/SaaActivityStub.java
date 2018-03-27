package tests._sup_android.stubs.mvp;

import android.app.Fragment;

import com.sup.dev.android.libs.mvp.activity_navigation.MvpActivityNavigation;

public class SaaActivityStub implements MvpActivityNavigation {

    public int stackSize;
    public boolean navigationLocked = false;

    @Override
    public void onFragmentBackPressed() {

    }

    @Override
    public void addFragment(Fragment fragment, String key, boolean animate) {
        stackSize++;
    }

    @Override
    public void backFragment() {
        stackSize--;
    }

    @Override
    public int getBackStackSize() {
        return stackSize;
    }

    @Override
    public String getBackStackTagAt(int i) {
        return "0";
    }

    @Override
    public void setNavigationLock(boolean lock) {
        navigationLocked = lock;
    }
}
