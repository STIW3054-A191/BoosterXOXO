package com.realtime.stiw3054;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateFile {
    public String pomPath(String path) {
        CreateFile file = new CreateFile();
        List<String> filenameList = new ArrayList<String>();
        file.findFiles("pom.xml" , path, filenameList);
        String pomPath="";

        for (String filename : filenameList) {
            System.out.println(filename);
            pomPath = filename;
        }
        return pomPath;
    }


    public void findFiles(String filenameSuffix, String currentDirUsed,
                          List<String> currentFilenameList) {
        File dir = new File(currentDirUsed);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {

                findFiles(filenameSuffix,file.getAbsolutePath(), currentFilenameList);
            } else {

                if (file.getAbsolutePath().endsWith(filenameSuffix)) {
                    currentFilenameList.add(file.getAbsolutePath());
                }
            }
        }
    }
}