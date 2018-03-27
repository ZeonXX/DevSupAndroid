package tests._sup_android;

import android.content.Context;
import android.database.Cursor;

import com.sup.dev.android.app.SupAndroidDI;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.libs.mvp.activity_navigation.MvpActivityNavigation;
import com.sup.dev.android.utils.interfaces.UtilsAndroid;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.android.utils.interfaces.UtilsCursor;
import com.sup.dev.android.utils.interfaces.UtilsFiles;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.android.utils.interfaces.UtilsMediaPlayer;
import com.sup.dev.android.utils.interfaces.UtilsNotifications;
import com.sup.dev.android.utils.interfaces.UtilsPermission;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsStorage;
import com.sup.dev.android.utils.interfaces.UtilsText;
import com.sup.dev.android.utils.interfaces.UtilsToast;
import com.sup.dev.android.utils.interfaces.UtilsView;
import tests._sup_android.stubs.mvp.SaaActivityStub;
import tests._sup_android.stubs.mvp.MvpNavigatorStub;
import tests._sup_android.stubs.utils.UtilsAndroidStub;
import tests._sup_android.stubs.utils.UtilsBitmapStub;
import tests._sup_android.stubs.utils.UtilsCursorStub;
import tests._sup_android.stubs.utils.UtilsFilesStub;
import tests._sup_android.stubs.utils.UtilsIntentStub;
import tests._sup_android.stubs.utils.UtilsMediaPlayerStub;
import tests._sup_android.stubs.utils.UtilsNetworkStub;
import tests._sup_android.stubs.utils.UtilsNotificationsStub;
import tests._sup_android.stubs.utils.UtilsPermissionStub;
import tests._sup_android.stubs.utils.UtilsResourcesStub;
import tests._sup_android.stubs.utils.UtilsStorageStub;
import tests._sup_android.stubs.utils.UtilsTextStub;
import tests._sup_android.stubs.utils.UtilsToastStub;
import tests._sup_android.stubs.utils.UtilsViewStub;
import tests._sup_java.SupJavaDIStub;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.utils.interfaces.UtilsNetwork;

public class SupAndroidDIStub extends SupJavaDIStub implements SupAndroidDI {

    public MvpNavigatorStub navigatorStub = new MvpNavigatorStub();
    public SaaActivityStub mvpActivityStub = new SaaActivityStub();
    public MvpActivityNavigation setedMvpActivity;

    public UtilsAndroidStub utilsAndroidStub = new UtilsAndroidStub();
    public UtilsBitmapStub utilsBitmapStub = new UtilsBitmapStub();
    public UtilsCursorStub utilsCursorStub = new UtilsCursorStub();
    public UtilsFilesStub utilsFilesStub = new UtilsFilesStub();
    public UtilsIntentStub utilsIntentStub= new UtilsIntentStub();
    public UtilsMediaPlayerStub utilsMediaPlayerStub = new UtilsMediaPlayerStub();
    public UtilsNotificationsStub utilsNotificationsStub= new UtilsNotificationsStub();
    public UtilsPermissionStub utilsPermissionStub = new UtilsPermissionStub();
    public UtilsResourcesStub utilsResources = new UtilsResourcesStub();
    public UtilsStorageStub utilsStorageStub = new UtilsStorageStub();
    public UtilsTextStub utilsTextStub = new UtilsTextStub();
    public UtilsToastStub utilsToastStub = new UtilsToastStub();
    public UtilsViewStub utilsViewStub = new UtilsViewStub();
    public UtilsNetworkStub utilsNetworkStub = new UtilsNetworkStub();


    @Override
    public Context appContext() {
        return null;
    }

    @Override
    public String appName() {
        return "AndroidDevSupport_UnitTest";
    }

    //
    //  Mvp
    //

    @Override
    public void setMvpActivity(MvpActivityNavigation mvpActivity) {
        setedMvpActivity = mvpActivity;
    }

    @Override
    public void mvpActivity(CallbackSource<MvpActivityNavigation> onActivity) {
        onActivity.callback(mvpActivityStub);
    }

    @Override
    public MvpNavigator navigator() {
        return navigatorStub;
    }


    //
    //  Utils
    //

    @Override
    public UtilsAndroid utilsAndroid() {
        return utilsAndroidStub;
    }

    @Override
    public UtilsBitmap utilsBitmap() {
        return utilsBitmapStub;
    }

    @Override
    public UtilsCursor utilsCursor(Cursor cursor) {
        return utilsCursorStub;
    }

    @Override
    public UtilsFiles utilsFiles() {
        return utilsFilesStub;
    }

    @Override
    public UtilsIntent utilsIntent() {
        return utilsIntentStub;
    }

    @Override
    public UtilsMediaPlayer utilsMediaPlayer() {
        return utilsMediaPlayerStub;
    }

    @Override
    public UtilsNotifications utilsNotifications() {
        return utilsNotificationsStub;
    }

    @Override
    public UtilsPermission utilsPermission() {
        return utilsPermissionStub;
    }

    @Override
    public UtilsResources utilsResources() {
        return utilsResources;
    }

    @Override
    public UtilsStorage utilsStorage() {
        return utilsStorageStub;
    }

    @Override
    public UtilsText utilsText() {
        return utilsTextStub;
    }

    @Override
    public UtilsToast utilsToast() {
        return utilsToastStub;
    }

    @Override
    public UtilsView utilsView() {
        return utilsViewStub;
    }

    @Override
    public UtilsNetwork utilsNetwork() {
        return utilsNetworkStub;
    }

}
