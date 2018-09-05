package com.sup.dev.android.views.views;

import android.content.Context;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v4.os.BuildCompat;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;

import java.io.File;

public class ViewEditTextMedia extends android.support.v7.widget.AppCompatEditText {

    private Callback1<String> callback;

    public ViewEditTextMedia(Context context) {
        this(context, null);
    }

    public ViewEditTextMedia(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        try {
            final InputConnection ic = super.onCreateInputConnection(editorInfo);
            EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/*"});
            return InputConnectionCompat.createWrapper(ic, editorInfo, (inputContentInfo, flags, opts) -> {
                if (BuildCompat.isAtLeastNMR1() && (flags & InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                    try {
                        inputContentInfo.requestPermission();
                    } catch (Exception e) {
                        Debug.log(e);
                        return false;
                    }
                }

                try {
                    if (callback != null) callback.callback(inputContentInfo.getLinkUri().toString());
                    return true;
                } catch (Exception ex) {
                    Debug.log(ex);
                }

                return false;

            });
        }catch (Exception e){
            return super.onCreateInputConnection(editorInfo);
        }
    }

    public void setCallback(Callback1<String> callback) {
        this.callback = callback;
    }


}
