package com.realtime.stiw3054;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import jxl.read.biff.BiffException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class Ckjm {

    public static List<String> getFolder(String path){
        File file = new File(path);
        File[] array = file.listFiles();
        List<String> folderPathList= new ArrayList<>();
        assert array != null;
        for(int i = 0; i < array.length; i++){
            if(array[i].isDirectory()){
                String folderPath = array[i].getPath();
                folderPathList.add(folderPath);
            }
        }
        return folderPathList;
    }

    public static List<String>getPomPath(){
        String path = "C:\\Github Repo";
        List<String> pomPathList= new ArrayList<>();
        for(int i =0;i < getFolder(path).size();i++){
            String folderPath = getFolder(path).get(i);
            CreateFile cf = new CreateFile();
            cf.pomPath(folderPath);
            if(!"".equals(cf.pomPath(folderPath))){
                int begin=cf.pomPath(folderPath).indexOf("C:");
                int last=cf.pomPath(folderPath).indexOf("\\pom");
                pomPathList.add(cf.pomPath(folderPath).substring(begin,last));
            }

        }
        return pomPathList;
    }


    public static void getInfo() throws IOException{
        String url = "https://github.com/STIW3054-A191/Assignments/wiki/List_of_Student";
        Document doc = Jsoup.connect(url).timeout(5000).get();
        Elements elementsTable = doc.select("table").select("tr");
        List<String> info = new ArrayList<String>();
        for (int i = 0; i < elementsTable.size() - 1; i++) {
            Elements tds = elementsTable.get(i+1).select("td");
            String Name = tds.get(2).text();
            String matricNum = tds.get(1).text();
            String fullInfo = matricNum + Name;
            info.add(fullInfo);
        }
        String path = "C:\\Github Repo\\info.txt";
        File file = new File(path);

        if (!file.isFile()) {
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String l:info){
                writer.write(l + "\r\n");
            }
        }
    }


    public static List<String> readTxtFileIntoStringArrList(String filePath)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("File Found");
            }
        }
        catch (Exception e)
        {
            System.out.println("File Not Found");
            e.printStackTrace();
        }

        return list;
    }





    public static void getCkjm(String pomPath) throws IOException, InterruptedException {

        Excel t = new Excel();
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<ClassMetrics> ref = new AtomicReference<>();
        String matricNum = pomPath.substring(12,18);
        String name = null;
        List<String> stringList = readTxtFileIntoStringArrList("C:\\Github Repo\\info.txt");
        for(int i = 0;i < stringList.size();i++){
            if(matricNum.equals(stringList.get(i).substring(0,6))){
                name = stringList.get(i).substring(6);
            }
        }

        t.MatricName(matricNum, name);
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", "WMC", "DIT", "NOC", "CBD", "RFC", "LCOM");
        CkjmOutputHandler outputHandler = new CkjmOutputHandler() {
            int w = 0, d = 0, n = 0, b = 0, r = 0, l = 0;
            Excel t2 = new Excel();

            @Override
            public void handleClass(String name, ClassMetrics c) {

                ref.set(c);
                latch.countDown();
                w += c.getWmc();
                d += c.getDit();
                n += c.getNoc();
                b += c.getCbo();
                r += c.getRfc();
                l += c.getLcom();
                t2.Excel(w, d, n, b, r, l);
            }
        };
        File root = new File(pomPath);
        try {
            String[] extensions = {"class"};
            Collection files = FileUtils.listFiles(root, extensions, true);
            if(files.isEmpty()){
                t.Excel(0,0,0,0,0,0);
            }
            for (Object o : files) {
                File file = (File) o;
                assertTrue("File " + file.getAbsolutePath() + " not present", file.exists());
                MetricsFilter.runMetrics(new String[]{file.getAbsolutePath()}, outputHandler);
                latch.await(1, TimeUnit.SECONDS);
                assertEquals(ref.get().getWmc() >= 0, ref.get().getWmc() >= 0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        t.printData();
    }



    public static void deleteOther(){
        String path = "C:\\Github Repo";
        for(int i =0;i < getFolder(path).size();i++){
            String filenameZip=getFolder(path).get(i)+"\\target\\dependency-reduced-pom.xml";
            File file = new File(filenameZip);
            if(!file.exists()){

            }
            else{

                file.delete();
            }
        }
    }


    public void runckjm() throws IOException, InterruptedException, BiffException{
        deleteOther();
        getInfo();
        List<String> pomPathList = new ArrayList<>(getPomPath());
        getCkjm("C:\\Github Repo\\243147-STIW3054-A191-Assignment1");
        getCkjm("C:\\Github Repo\\249879-STIW3054-A191-A1\\com.assignment");
        for(int i = 2;i < pomPathList.size();i++){
            getCkjm(pomPathList.get(i));
        }
        Excel t22 = new Excel();
        t22.writeToExcel();
        t22.makeBarChart();
    }
}