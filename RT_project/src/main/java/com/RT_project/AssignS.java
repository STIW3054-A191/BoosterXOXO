package com.RT_project;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class AssignS {

    public static String[] followers = new String[30];

    public static String readFileAsString(String fileName) throws IOException {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    static void app() {
        String saveDir = System.getProperty("java.io.tmpdir");
        String studentsListData ="";
        String All = "";
        char sep = (char) 10;

        try {

            https.downloadFile("https://raw.githubusercontent.com/wiki/STIW3054-A191/Assignments/List_of_Student.md", saveDir); 
            https.downloadFile("https://api.github.com/repos/STIW3054-A191/Assignments/issues/1/comments?page=1", saveDir);
            
            studentsListData = readFileAsString(saveDir + System.getProperty("file.separator") + "List_of_Student.md");       
            All = All + readFileAsString(saveDir + System.getProperty("file.separator") + "commentspage=1.txt");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        String[] lines = studentsListData.split(Character.toString(sep));
        for (int i = 2; i < lines.length; i++) {
            String line = lines[i];
        }
        
        System.out.println(studentsListData);
        
        All = All.replace("},{", "\n");
        All = All.replaceAll("\"url\".*?er\":", "");//for replace the text after user 
        All = All.replaceAll("\"id\".*?A191-A1\"", "");//for replace the text after user 
        All = All.replaceAll("\"id\".*?git\"", "");//for replace the text after user 
        All = All.replaceAll("\"id\".*?ment1\"", "");//for replace the text after user 
        All = All.replaceAll("\"id\".*?-A1-A1", "");//for replace the text after user 
        All = All.replace("[", "\n");
        All = All.replace("{", "");
        All = All.replace("}", "");
        All = All.replace("]", "");
        All = All.replace("\r", "");
        All = All.replace("\n", "");
        All = All.replace(",", "\n");
        All = All.replace("\"", "");
        All = All.replace("login:", "");
        All = All.replace("\\r\\n", "");
        String[] lines = All.split(Character.toString(sep));

        
        followers = lines;
        System.out.println("List All students submit Assignment 1 ");
        System.out.println("------------------------------------------");
        System.out.print(All);
//System.out.println(Arrays.toString(lines));
    }
}
