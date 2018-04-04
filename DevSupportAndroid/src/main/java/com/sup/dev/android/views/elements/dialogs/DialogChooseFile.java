package com.sup.dev.android.views.elements.dialogs;


import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsPermission;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.elements.cards.Card;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.android.views.widgets.settings.SettingsAction;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

import java.io.File;

public class DialogChooseFile extends BaseDialog {

    private final UtilsPermission utilsPermission = SupAndroid.di.utilsPermission();

    private final RecyclerCardAdapter adapter;
    private final RecyclerView vRecycler;

    private File currentFolder;
    private File rootFolder;
    private boolean showFolders = true;
    private boolean showFiles = true;
    private boolean canGoInFolder = true;
    private String[] fileTypes;
    private CallbackSource<File> onFileSelected;
    private CallbackSource<File> onFolderSelected;

    public DialogChooseFile(Context viewContext) {
        super(viewContext, R.layout.dialog_choose_file);

        adapter = new RecyclerCardAdapter();
        rootFolder = Environment.getExternalStorageDirectory();
        currentFolder = rootFolder;

        vRecycler = findViewById(R.id.recycler);
        vRecycler.setLayoutManager(new LinearLayoutManager(viewContext));
        vRecycler.setAdapter(adapter);

        resetCards(rootFolder);
    }

    //
    //  Public methods
    //

    public DialogChooseFile showWithRequestPermission(Activity activity, Callback onPermissionRestriction) {
        utilsPermission.requestReadPermission(activity,
                () -> super.show(), onPermissionRestriction);
        return this;
    }

    public DialogChooseFile show() {
        throw new RuntimeException("Use showWithRequestPermission for DialogChooseFile");
    }

    public DialogChooseFile setShowFiles(boolean showFiles) {
        this.showFiles = showFiles;
        resetCards(currentFolder);
        return this;
    }

    public DialogChooseFile setShowFolders(boolean showFolders) {
        this.showFolders = showFolders;
        resetCards(currentFolder);
        return this;
    }

    public DialogChooseFile setCanGoInFolder(boolean canGoInFolder) {
        this.canGoInFolder = canGoInFolder;
        resetCards(currentFolder);
        return this;
    }

    public DialogChooseFile setFileTypes(String... fileTypes) {
        this.fileTypes = fileTypes;
        return this;
    }

    public DialogChooseFile setFolder(File file) {
        currentFolder = file;
        resetCards(file);
        return this;
    }

    public DialogChooseFile setRootFolder(File file) {
        rootFolder = file;
        resetCards(file);
        return this;
    }

    public DialogChooseFile setOnFileSelected(CallbackSource<File> onFileSelected) {
        this.onFileSelected = onFileSelected;
        resetCards(currentFolder);
        return this;
    }

    public DialogChooseFile setOnFolderSelected(CallbackSource<File> onFolderSelected) {
        this.onFolderSelected = onFolderSelected;
        resetCards(currentFolder);
        return this;
    }

    public DialogChooseFile setOnCancel(int s) {
        return (DialogChooseFile) super.setOnCancel(s);
    }

    public DialogChooseFile setOnCancel(String s) {
        return (DialogChooseFile) super.setOnCancel(s);
    }

    //
    //  Private Methods
    //

    private void resetCards(File file) {
        adapter.clear();
        if (!file.getAbsolutePath().equals(rootFolder.getAbsolutePath())) adapter.add(new CardBack(file));
        File[] files = file.listFiles();
        if (showFolders) for (File f : files) if (f.isDirectory()) adapter.add(new CardFile(f));
        if (showFiles && onFileSelected != null) for (File f : files) if (!f.isDirectory() && checkType(f)) adapter.add(new CardFile(f));
    }

    private boolean checkType(File f) {
        if (fileTypes == null) return true;
        for (String type : fileTypes) {
            String[] split = f.getName().split(".");
            if (type.toLowerCase().equals(split[split.length - 1].toLowerCase())) return true;
        }
        return false;
    }

    //
    //  Card
    //

    private class CardBack extends Card {

        private final File file;

        private CardBack(File file) {
            this.file = file;
        }

        public int getLayout() {
            return 0;
        }

        public View instanceView(Context context) {
            SettingsAction settingsAction = new SettingsAction(context);
            settingsAction.setIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
            settingsAction.setTitle(file.getName());
            settingsAction.setOnClickListener(v -> setFolder(file.getParentFile()));
            return settingsAction;
        }

        public void bindView(View view) {

        }
    }

    private class CardFile extends Card {

        private final File file;
        private ViewIcon viewIcon;

        private CardFile(File file) {
            this.file = file;
        }

        public int getLayout() {
            return 0;
        }

        public View instanceView(Context context) {
            return new SettingsAction(context);
        }

        private ViewIcon getViewIcon() {
            if (viewIcon == null) {
                viewIcon = SupAndroid.di.utilsView().inflate(viewContext, R.layout.w_icon);
                viewIcon.setImageResource(R.drawable.ic_done_white_24dp);
                viewIcon.setOnClickListener(v -> {
                    if (onFolderSelected != null) {
                        onFolderSelected.callback(file);
                        hide();
                    }
                });
            }
            return viewIcon;
        }

        @Override
        public void bindView(View view) {
            SettingsAction v = (SettingsAction) view;
            v.setTitle(file.getName());
            v.setIcon(file.isDirectory() ? R.drawable.ic_folder_white_24dp : R.drawable.ic_insert_drive_file_white_24dp);
            v.setSubView((file.isDirectory() && onFolderSelected != null && canGoInFolder) ? getViewIcon() : null);

            v.setOnClickListener(v1 -> {
                if (file.isDirectory()) {
                    if (!canGoInFolder) {
                        if (onFolderSelected != null) {
                            onFolderSelected.callback(file);
                            hide();
                        }
                    } else {
                        setFolder(file);
                    }
                } else {
                    if (onFileSelected != null) {
                        onFileSelected.callback(file);
                        hide();
                    }
                }
            });
        }
    }
}
