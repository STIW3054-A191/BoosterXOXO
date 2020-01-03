package com.realtime.stiw3054;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class ConvertPDF {
    public void mkpdf() {
        Document document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        String input = "C:\\Github Repo\\Bar.png";
        String output = "C:\\Github Repo\\Bar.pdf";

        try {
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            Image img = Image.getInstance(input);
            img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            document.add(img);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}