package com.realtime.stiw3054;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepoLink {

    public List<String> showList() throws IOException {


        final String link = "https://github.com/STIW3054-A191/Assignments/issues/1";
        final Document document = Jsoup.connect(link).get();
        Elements table = document.select("td.d-block.comment-body.markdown-body.js-comment-body");
        List<String> listRepo=new ArrayList<String>();


        for (Element listLink : table) {
            String url = listLink.getElementsByTag("a").attr("abs:href");
            listRepo.add(url);
        }

        return listRepo;
    }

    }








