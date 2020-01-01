package excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileInputStream;

public class getlist implements exceldata {

    public static void get() {

        try {

            FileInputStream file = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //ReadSheets
            XSSFSheet sheet = workbook.getSheet(sheetName);

            final Document doc = Jsoup.connect("https://github.com/STIW3054-A191/Assignments/wiki/List_of_Student").get();
            Elements elementsList = doc.select("table").select("tr");

            if (elementsList.size() == 0) {
                System.out.println("\u001B[31mUnable to receive data from the URL !\u001B[0m");
                System.exit(0);
            }else {
                for (int i = 1; i < elementsList.size(); i++) {
                    Elements elementsItem = elementsList.get(i).select("td");


                    Row row = sheet.createRow(Integer.parseInt(elementsItem.get(0).text()));
                    row.createCell(0).setCellValue(elementsItem.get(0).text());
                    row.createCell(1).setCellValue(elementsItem.get(1).text());
                    row.createCell(2).setCellValue(elementsItem.get(2).text());
                }


                OutputExcel.output(workbook,sheet);
            }

        }catch (Exception e) {
            System.out.println("\u001B[31mUnable to receive data from the URL !\u001B[0m");
            System.exit(0);
        }
    }
}
