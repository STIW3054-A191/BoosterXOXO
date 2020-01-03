package com.realtime.stiw3054;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OutFile{

    static String path;

    public static List<String> getFolder(String path){
        OutFile.path = path;
        File file = new File(path);
        File[] array = file.listFiles();
        List<String> folderPathList=new ArrayList<String>();
        for(int i =0;i < array.length;i++){
            if(array[i].isDirectory()){
                String folderPath = array[i].getPath();
                folderPathList.add(folderPath);
            }
        }
        return folderPathList;
    }

    public static void mvnInstall(String path) throws InterruptedException{
        try {
            String Command = "cmd /c   cd "+path+"&& mvn install";
            Process a = Runtime.getRuntime().exec(Command);
            a.waitFor(2, TimeUnit.SECONDS);
            a.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(LogFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void mkOut(){
        String mkDirectoryPath = "C:\\Github Repo\\out";
        CloneRepo repo = new CloneRepo();
        if(repo.mkDir(mkDirectoryPath)){
            System.out.println(mkDirectoryPath+" is Created");
        }
        else{
            System.out.println(mkDirectoryPath+" The Directory have been already existed");
        }
    }
    public static void runJar(String path) throws InterruptedException{

        Thread.sleep(2000);
        try{
            String matricNum = null;
            String jarName = null;
            File file = new File(path+"\\target");
            File[] array = file.listFiles();
            assert array != null;
            for(int i = 0; i < array.length; i++){
                if(array[i].isFile()){
                    if(array[i].getName().endsWith(".jar")){
                        jarName = array[i].getName();

                    }
                }
            }
            matricNum = path.substring(12,18);
            String Command = "cmd /c   cd "+path+"\\target  && java -jar "+jarName+" > C:\\Github Repo\\out\\"+matricNum+".out.txt";
            Process a = Runtime.getRuntime().exec(Command);
            a.waitFor(3, TimeUnit.SECONDS);
            a.destroy();
        }catch (IOException ex){
            ex.printStackTrace();
            Logger.getLogger(LogFile.class.getName()).log(Level.SEVERE,null,ex);
        }

    }
    public void OutFile() throws InterruptedException{
        String path = "C:\\Github Repo";
        List<String> pomPathList=new ArrayList<String>();
        for(int i =0;i < getFolder(path).size()-1;i++){
            String folderPath = getFolder(path).get(i);
            CreateFile cf = new CreateFile();
            if(!"".equals(cf.pomPath(folderPath))){
                int begin=cf.pomPath(folderPath).indexOf("C:");
                int last=cf.pomPath(folderPath).indexOf("\\pom");
                pomPathList.add(cf.pomPath(folderPath).substring(begin,last));
            }
            else{
                System.out.println("Please "+folderPath.substring(12,18)+" upload the full file");
            }
        }
        mkOut();
        for(int i = 0;i < pomPathList.size();i++){
//            System.out.println(pomPathList.get(i));
            mvnInstall(pomPathList.get(i));
        }
        for(int i = 0;i < pomPathList.size();i++){
//            System.out.println(pomPathList.get(i));
            runJar(pomPathList.get(i));
        }
    }
}
