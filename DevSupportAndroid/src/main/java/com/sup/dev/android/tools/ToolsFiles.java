package com.sup.dev.android.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.libs.debug.Debug;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ToolsFiles extends com.sup.dev.java.tools.ToolsFiles{

    //
    //  Files
    //

    public static File writeFile(String patch, byte[] bytes) throws IOException {

        File file = new File(patch);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists())
            parent.mkdirs();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
            out.close();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
                Debug.log(ex);
            }
        }

        return file;
    }

    public static byte[] readAsZip(String filePath) throws IOException {
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bytesOutputStream);

        addFolderToZip("", filePath, zip);

        zip.flush();
        zip.close();

        return bytesOutputStream.toByteArray();
    }

    private static void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        File folder = new File(srcFile);

        if (flag) {
            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
        } else {
            if (folder.isDirectory()) {
                addFolderToZip(path, srcFile, zip);
            } else {

                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) zip.write(buf, 0, len);

            }
        }
    }

    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);

        if (folder.list().length == 0) {
            System.out.println(folder.getName());
            addFileToZip(path, srcFolder, zip, true);
        } else {
            for (String fileName : folder.list()) {
                if (path.equals(""))
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                 else
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
            }
        }
    }

    public static void unpackZip(String path, File zipFile) throws IOException {
        InputStream is = null;
        ZipInputStream zis = null;
        try {
            String filename;
            is = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();
                File file = new File(path + filename);
                if (ze.isDirectory()) continue;

                File parentFile = file.getParentFile();
                if(parentFile != null)parentFile.mkdirs();

                FileOutputStream fos = new FileOutputStream(path + filename);
                while ((count = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                zis.closeEntry();
            }
            zis.close();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ex) {
                Debug.log(ex);
            }
            try {
                if (zis != null) zis.close();
            } catch (IOException ex) {
                Debug.log(ex);
            }

        }
    }

    //
    //  Bitmap
    //

    public static void saveImageInCameraFolder(Activity activity, Bitmap bitmap, Callback1<String> onResult, Callback onPermissionPermissionRestriction) {
        ToolsPermission.requestWritePermission(activity, () -> {
            File file = createJpgFileInCameraFolder();
            writeBitmap(bitmap, file);
            onResult.callback(file.getAbsolutePath());
        }, onPermissionPermissionRestriction);
    }

    private static File createJpgFileInCameraFolder() {

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        storageDir.mkdir();
        String fileName = "/Camera/" + System.currentTimeMillis() + ".jpg";
        File file = new File(storageDir, fileName);
        file.getParentFile().mkdirs();
        return new File(storageDir, fileName);
    }

    private static void writeBitmap(Bitmap bitmap, File file) {

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception ex) {
            Debug.log(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Debug.log(ex);
                }
            }
        }
    }

    //
    //  Getters
    //

    public static File getDiskCacheDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = SupAndroid.appContext.getExternalCacheDir();
            if (externalCacheDir != null)
                return new File(SupAndroid.appContext.getExternalCacheDir().getPath());
        }

        return new File(SupAndroid.appContext.getCacheDir().getPath());
    }


}
