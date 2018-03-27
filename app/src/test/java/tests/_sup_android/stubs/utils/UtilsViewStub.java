package tests._sup_android.stubs.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class UtilsViewStub implements UtilsView {

    public boolean hideKeyboardCalled;

    @Override
    public int[] viewPointAsScreenPoint(View view, int x, int y) {
        return new int[0];
    }

    @Override
    public boolean checkHit(View view, float x, float y) {
        return false;
    }

    @Override
    public <K extends View> K inflate(Context viewContext, int res) {
        return (K) LayoutInflater.from(viewContext).inflate(res, null, false);
    }

    @Override
    public View inflate(@NonNull ViewGroup parent, int res) {
        return null;
    }

    @Override
    public ViewGroup getRootParent(View v) {
        return null;
    }

    @Override
    public <K extends View> K findViewOnParents(View v, int viewId) {
        return null;
    }

    @Override
    public void makeTransparentAppBar(Activity activity) {

    }

    @Override
    public int dpToPx(float dp) {
        return (int) (dp*2);
    }

    @Override
    public int spToPx(float sp) {
        return (int) (sp*2);
    }

    @Override
    public void hideKeyboard(Activity activity) {
        hideKeyboardCalled = true;
    }

    @Override
    public void hideKeyboard(View view) {
        hideKeyboardCalled = true;
    }

    @Override
    public void showKeyboard(View view) {

    }

    @Override
    public void setTextAnimate(TextView v, String text) {
        v.setText(text);
    }

    @Override
    public void setTextAnimate(TextView v, String text, Callback onAlpha) {
        v.setText(text);
        onAlpha.callback();
    }

    @Override
    public void crossfade(View in, View out) {
        in.setVisibility(View.VISIBLE);
        out.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clearAnimation(View v) {

    }

    @Override
    public void alpha(View v, boolean toAlpha) {
        if (toAlpha)
            v.setVisibility(View.INVISIBLE);
        else
            v.setVisibility(View.VISIBLE);
    }

    @Override
    public void fromAlpha(View v) {
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void fromAlpha(View v, int time) {
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void fromAlpha(View v, Callback onFinish) {
        v.setVisibility(View.VISIBLE);
        onFinish.callback();
    }

    @Override
    public void fromAlpha(View v, int time, Callback onFinish) {
        v.setVisibility(View.VISIBLE);
        onFinish.callback();
    }

    @Override
    public void toAlpha(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toAlpha(View v, int time) {
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toAlpha(View v, Callback onFinish) {
        v.setVisibility(View.INVISIBLE);
        onFinish.callback();
    }

    @Override
    public void toAlpha(View v, int time, Callback onFinish) {
        v.setVisibility(View.INVISIBLE);
        onFinish.callback();
    }

    @Override
    public void targetAlpha(View v, float alpha) {
        v.setVisibility(View.INVISIBLE);
    }
}
