package client.ui;

import java.io.File;

public class FileUtil {

    public static boolean fileExists(File file) {
        return file.exists();
    }

    public static void createDirectory(String path) {

        File directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}