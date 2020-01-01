package excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class exceloutput implements exceldata {

    static void output(XSSFWorkbook workbook, XSSFSheet sheet) {
        //Auto resize
        for (int i = 0; i < title.length; i++) {
            sheet.autoSizeColumn(i);
        }

        while (true) {
            // An output stream accepts output bytes and sends them to sink.
            try (OutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
                // Close fileOut and workbook
                fileOut.close();
                workbook.close();
                break;

            } catch (Exception e) {
                System.out.println("Failed");

                try {
                    System.in.read();
                } catch (IOException ignored) { }
            }
        }
    }
}
