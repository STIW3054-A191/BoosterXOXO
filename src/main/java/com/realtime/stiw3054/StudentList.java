package com.realtime.stiw3054;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentList {

    public List<Data> showList() {





        try {
            final String listStudent = "https://github.com/STIW3054-A191/Assignments/wiki/List_of_Student";
            final Document document = Jsoup.connect(listStudent).get();

            List<Data> details = new ArrayList<Data>();


            for (int i = 1; i <= 35; i++) {
                Elements No = document.select("#wiki-body > div > table > tbody > tr:nth-child(" + i + ") > td:nth-of-type(1)");
                Elements Matric = document.select("#wiki-body > div > table > tbody > tr:nth-child(" + i + ") > td:nth-of-type(2)");
                Elements Name = document.select("#wiki-body > div > table > tbody > tr:nth-child(" + i + ") > td:nth-of-type(3)");

                System.out.printf("| %-5s| %-17s| %-50s\n", No.text(), Matric.text(), Name.text());
                details.add(new Data(No.text(), Matric.text(), Name.text()));
            }
            return details;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}