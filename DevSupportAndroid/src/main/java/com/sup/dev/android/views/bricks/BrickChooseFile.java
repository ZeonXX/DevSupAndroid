package com.sup.dev.android.views.bricks;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsPermission;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.settings.SettingsAction;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

import java.io.File;

public class BrickChooseFile extends BrickRecycler {

    private File currentFolder;
    private File rootFolder;
    private boolean showFolders = true;
    private boolean showFiles = true;
    private boolean canGoInFolder = true;
    private String[] fileTypes;
    private Callback1<File> onFileSelected;
    private Callback1<File> onFolderSelected;
    
    public BrickChooseFile() {

        adapter = new RecyclerCardAdapter();
        rootFolder = Environment.getExternalStorageDirectory();
        currentFolder = rootFolder;
    }
    

    @Override
    public void bindView(View view, Mode mode) {
        super.bindView(view, mode);

        RecyclerView vRecycler = view.findViewById(R.id.recycler);

        vRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        vRecycler.setAdapter(adapter);

        ToolsPermission.requestReadPermission(() -> resetCards(rootFolder));
    }


    private void resetCards(File file) {
        adapter.clear();
        if (!file.getAbsolutePath().equals(rootFolder.getAbsolutePath())) adapter.add(new BrickChooseFile.CardBack(file));
        File[] files = file.listFiles();
        if (showFolders) for (File f : files) if (f.isDirectory()) adapter.add(new BrickChooseFile.CardFile(f));
        if (showFiles && onFileSelected != null) for (File f : files) if (!f.isDirectory() && checkType(f)) adapter.add(new BrickChooseFile.CardFile(f));
    }


    private boolean checkType(File f) {
        if (fileTypes == null) return true;
        for (String type : fileTypes) {
            String name = f.getName().toLowerCase();
            String t = type+".";
            if (name.length() > -t.length() && name.substring(name.length() - t.length()).equals(t)) return true;
        }
        return false;
    }


    //
    //  Setters
    //

    public BrickChooseFile setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickChooseFile setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public BrickChooseFile setShowFiles(boolean showFiles) {
        this.showFiles = showFiles;
        resetCards(currentFolder);
        return this;
    }

    public BrickChooseFile setShowFolders(boolean showFolders) {
        this.showFolders = showFolders;
        resetCards(currentFolder);
        return this;
    }

    public BrickChooseFile setCanGoInFolder(boolean canGoInFolder) {
        this.canGoInFolder = canGoInFolder;
        resetCards(currentFolder);
        return this;
    }

    public BrickChooseFile setFileTypes(String... fileTypes) {
        this.fileTypes = fileTypes;
        return this;
    }

    public BrickChooseFile setFolder(File file) {
        currentFolder = file;
        resetCards(file);
        return this;
    }

    public BrickChooseFile setRootFolder(File file) {
        rootFolder = file;
        resetCards(file);
        return this;
    }

    public BrickChooseFile setOnFileSelected(Callback1<File> onFileSelected) {
        this.onFileSelected = onFileSelected;
        resetCards(currentFolder);
        return this;
    }

    public BrickChooseFile setOnFolderSelected(Callback1<File> onFolderSelected) {
        this.onFolderSelected = onFolderSelected;
        resetCards(currentFolder);
        return this;
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

        private ViewIcon getViewIcon(Context context) {
            if (viewIcon == null) {
                viewIcon = ToolsView.inflate(context, R.layout.w_icon);
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
            v.setSubView((file.isDirectory() && onFolderSelected != null && canGoInFolder) ? getViewIcon(view.getContext()) : null);

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
