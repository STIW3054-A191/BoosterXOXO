package com.realtime.stiw3054;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LogFile {
     static String path;

    public static List<String> getFolder(String path){
        LogFile.path = path;
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




    public void mkLog(){
        String mkDirectoryPath = "C:\\Github Repo\\log";
        CloneRepo repo = new CloneRepo();
        if(repo.mkDir(mkDirectoryPath)){
            System.out.println(mkDirectoryPath+" is Created");
        }
        else{
            System.out.println(mkDirectoryPath+" The Directory have been already existed");
        }
    }
    public static void CompileProgram(String path) throws InterruptedException{
        try {

            String matricNum = path.substring(12, 18);
            String Command = "cmd /c   cd "+path+"&& mvn compile > C:\\Github Repo\\log\\"+matricNum+".log.txt";
            Process a = Runtime.getRuntime().exec(Command);
            a.waitFor();
            a.destroy();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(LogFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void compile()throws InterruptedException{
        String path = "C:\\Github Repo";
        List<String> pomPathList= new ArrayList<>();
        for(int i = 0; i < getFolder(path).size(); i++){
            String folderPath = getFolder(path).get(i);
            CreateFile cf = new CreateFile();
            cf.pomPath(folderPath);
            if(!"".equals(cf.pomPath(folderPath))){
                int begin=cf.pomPath(folderPath).indexOf("C:");
                int last=cf.pomPath(folderPath).indexOf("\\pom");
                pomPathList.add(cf.pomPath(folderPath).substring(begin,last));
            }else{
                System.out.println("Please "+folderPath.substring(12,18)+" upload the full file");
            }
        }
        mkLog();
        for(int i = 0;i < pomPathList.size();i++){
            CompileProgram(pomPathList.get(i));
        }
    }
}