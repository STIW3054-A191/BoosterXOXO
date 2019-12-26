package Project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class scrap {

    public static ArrayList<String> getLink() {
        // Get assignment 1 link
        ArrayList<String> arrayLink = new ArrayList<>();
        try {
            // connect
            final Document doc = Jsoup.connect("https://github.com/STIW3054-A191/Assignments/issues/1").get();
            Elements elements = doc.select("table").select("a");

            for (Element array : elements) {
                String elementLink = array.attr("href");

                // Test .git link
                if (elementLink.endsWith(".git")) {
                    arrayLink.add(elementLink.substring(0, elementLink.length() - 4));
                } else {
                    arrayLink.add(elementLink);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayLink;
    }
}