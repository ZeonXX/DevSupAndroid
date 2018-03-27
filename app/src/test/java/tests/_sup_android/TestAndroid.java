package tests._sup_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

import org.robolectric.Robolectric;
import org.robolectric.android.controller.FragmentController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import tests._sup_java.TestJava;

public class TestAndroid extends TestJava{

    public static SupAndroidDIStub di;

    public static void init() {
        init(new SupAndroidDIStub());
    }

    public static void init(SupAndroidDIStub di) {
        TestAndroid.di = di;
        TestJava.init(di);
        SupAndroid.di = di;
    }

    public static void setBundle(Fragment fragment, Bundle bundle) {
        try {
            Field field = Fragment.class.getDeclaredField("mSavedFragmentState");
            field.setAccessible(true);
            field.set(fragment, bundle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <K> K field(Object from, String fieldName) {
        try {
            Field field = from.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (K) field.get(from);
        } catch (Exception e) {
            try {
                Field field = from.getClass().getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return (K) field.get(from);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void setField(Object from, String fieldName, Object value) {
        try {
            Field field = from.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(from, value);
        } catch (Exception e) {
            try {
                Field field = from.getClass().getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(from, value);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void method(Object from, String methodName, Object... values) {
        try {
            Class[] valuesClasses = new Class[values.length];
            for(int i = 0; i < values.length; i++)
                valuesClasses[i] = values[i].getClass();
            Method method = from.getClass().getDeclaredMethod(methodName, valuesClasses);
            method.setAccessible(true);
            method.invoke(from, values);
        } catch (Exception ex) {
            try {
                Method[] methods = from.getClass().getDeclaredMethods();
                for (Method method : methods)
                    if(method.getName().equals(methodName)){
                        method.setAccessible(true);
                        method.invoke(from, values);
                        break;
                    }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <K extends MvpFragment> K fragment(Class<K> fragmentClass, MvpPresenterInterface presenter){

        FragmentController<K> fragmentFragmentController = Robolectric.buildFragment(fragmentClass);

        K fragment = fragmentFragmentController.get();
        TestAndroid.di.navigatorStub.setPresenter(fragment, presenter);
        fragmentFragmentController.create();

        return fragment;
    }

    public static void measure(View view, int w, int h){
        method(view, "setMeasuredDimension", w, h);
    }


}
