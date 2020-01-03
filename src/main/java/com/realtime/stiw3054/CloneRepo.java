package com.realtime.stiw3054;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CloneRepo extends Thread{

//File is created in the file directory called Github Repo file
    public boolean mkDir(String path){
        File file = null;
        try{
            file = new File(path);
            if(!file.exists()){
                return file.mkdirs();
            }
            else{
                return false;
            }
        }catch(Exception e){
        }finally{
            file = null;
        }
        return false;
    }

    @Override
    public void run(){
        RepoLink a1 = new RepoLink();
        List<String> listRepo=new ArrayList<String>();
        try {
            listRepo.addAll(a1.showList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0;i<listRepo.size();i++){
            try {
                String full_command;
                String command = "git clone {}";
                full_command = command.replace("{}",listRepo.get(i));

                Process p = Runtime.getRuntime().exec(full_command,null,new File("C:\\Github Repo"));
            } catch (IOException ex) {
                Logger.getLogger(CloneRepo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Clone() throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        Future<Integer> result = executor.submit(task);
        executor.shutdown();
        try {
            Thread.sleep(1000);
            CloneRepo a = new CloneRepo();
            a.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Downloading........"); //the process of downloading the repository file

        System.out.println("All repositories have been successfully cloned ");
    }
}
class Task implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        System.out.println("Cloning Repositories From Github");
        Thread.sleep(3000);
        String mkDirPath = "C:\\Github Repo";
        CloneRepo clone = new CloneRepo();
        if(clone.mkDir(mkDirPath)){
            System.out.println(mkDirPath+" The Direcory have been sucessfully created");
        }
        else{
            System.out.println(mkDirPath+" The Directory created failed!The Directory have been already existed");
        }
        int repoNumber = 0;
        RepoLink a1 = new RepoLink();
        for(int i=0;i<a1.showList().size();i++)
            repoNumber += 1;
        return repoNumber;
    }
}
