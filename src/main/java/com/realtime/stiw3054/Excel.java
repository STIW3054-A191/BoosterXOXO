package com.realtime.stiw3054;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;



public class Excel {

    private static final List<Data> softwareMetrics = new ArrayList();
    private static final List<Data3> softwareMetricsTotal = new ArrayList();
    private static final List<Data2> matricname = new ArrayList();

    public void MatricName(String matricNum, String name) {
        matricname.add(new Data2(matricNum, name));
    }

    public void Excel(int w, int d, int n, int b, int r, int l) {
        softwareMetrics.add(new Data(w, d, n, b, r, l));

    }

    void printData() {

        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s\n", softwareMetrics.get(softwareMetrics.size() - 1).getData0(), softwareMetrics.get(softwareMetrics.size() - 1).getData1(), softwareMetrics.get(softwareMetrics.size() - 1).getData2(), softwareMetrics.get(softwareMetrics.size() - 1).getData3(), softwareMetrics.get(softwareMetrics.size() - 1).getData4(), softwareMetrics.get(softwareMetrics.size() - 1).getData5());
        ckjmTotal(softwareMetrics.get(softwareMetrics.size() - 1).getData0(), softwareMetrics.get(softwareMetrics.size() - 1).getData1(), softwareMetrics.get(softwareMetrics.size() - 1).getData2(), softwareMetrics.get(softwareMetrics.size() - 1).getData3(), softwareMetrics.get(softwareMetrics.size() - 1).getData4(), softwareMetrics.get(softwareMetrics.size() - 1).getData5());
    }
    void ckjmTotal(int w, int d, int n, int b, int r, int l){
        softwareMetricsTotal.add(new Data3(w, d, n, b, r, l));
    }

    public void writeToExcel() {
        if (softwareMetrics.isEmpty()) {
            System.out.println("ERROR : No data to write, build terminated.");
            System.exit(0);
        }
        String excelFile = "STIW3054_A191_Project.xls";
        System.out.println("=================================");
        System.out.println("Writing the " + excelFile + "...");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Student_Project_Ckjm");
        try {
            int k = 0;
            do {
                if (k == 1) {
                    for (int i = 0; i < matricname.size(); i++) {
                        HSSFRow row = sheet.createRow(i+1);
                        HSSFCell cell1 = row.createCell(0);
                        cell1.setCellValue(i + 1);
                        HSSFCell cell2 = row.createCell(1);
                        cell2.setCellValue(matricname.get(i).getDataM());
                        HSSFCell cell3 = row.createCell(2);
                        cell3.setCellValue(matricname.get(i).getDataN());
                        HSSFCell cell4 = row.createCell(3);
                        cell4.setCellValue(softwareMetricsTotal.get(i).getData0());
                        HSSFCell cell5 = row.createCell(4);
                        cell5.setCellValue(softwareMetricsTotal.get(i).getData1());
                        HSSFCell cell6 = row.createCell(5);
                        cell6.setCellValue(softwareMetricsTotal.get(i).getData2());
                        HSSFCell cell7 = row.createCell(6);
                        cell7.setCellValue(softwareMetricsTotal.get(i).getData3());
                        HSSFCell cell8 = row.createCell(7);
                        cell8.setCellValue(softwareMetricsTotal.get(i).getData4());
                        HSSFCell cell9 = row.createCell(8);
                        cell9.setCellValue(softwareMetricsTotal.get(i).getData5());
                    }
                    k++;
                } else {
                    HSSFRow row = sheet.createRow(0);
                    HSSFCell cell1 = row.createCell(0);
                    cell1.setCellValue("No");
                    HSSFCell cell2 = row.createCell(1);
                    cell2.setCellValue("Matric No");
                    HSSFCell cell3 = row.createCell(2);
                    cell3.setCellValue("Name");
                    HSSFCell cell4 = row.createCell(3);
                    cell4.setCellValue("WMC");
                    HSSFCell cell5 = row.createCell(4);
                    cell5.setCellValue("DIT");
                    HSSFCell cell6 = row.createCell(5);
                    cell6.setCellValue("NOC");
                    HSSFCell cell7 = row.createCell(6);
                    cell7.setCellValue("CBD");
                    HSSFCell cell8 = row.createCell(7);
                    cell8.setCellValue("RFC");
                    HSSFCell cell9 = row.createCell(8);
                    cell9.setCellValue("LCOM");
                    k++;
                }
            } while (k <= 1);
            FileOutputStream outputFile = new FileOutputStream("C:/Github Repo/Project.xls");
            workbook.write(outputFile);
            outputFile.flush();
            outputFile.close();
            System.out.println("=================================");
            System.out.println(excelFile + " Is written successfully.");
        } catch (IOException e) {
            System.out.println("ERROR : Failed to write the file!");
        }
    }

    public void makeBarChart() throws FileNotFoundException, IOException, BiffException{
        jxl.Workbook readwb = null;

        readwb = Workbook.getWorkbook(new FileInputStream(new File("C:\\Github Repo\\Project.xls")));

        Sheet readsheet = readwb.getSheet(0);

        int rsColumns = readsheet.getColumns();

        int rsRows = readsheet.getRows();

        String[][] arr = (new String[rsColumns][rsRows]);
        double[][] data = (new double[rsColumns-3][rsRows-1]);
        String[] rowKeys = { "WMC", "DIT","NOC","CBD","RFC","LCOM" };
        String[] columnKeys = { "1", "2", "3", "4", "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"};
        for (int i = 0; i < rsColumns; i++) {
            for (int j = 0; j < rsRows; j++) {
                Cell cell = readsheet.getCell(i, j);
                arr[i][j] = cell.getContents();
            }
        }
        for (int i = 0; i < rsColumns-3; i++) {
            for (int j = 0; j < rsRows-1; j++) {

                data[i][j] = Double.valueOf(arr[i+3][j+1]);

            }
        }
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
        createStackedBarChart(dataset, "x", "y", "Bar Chart", "Bar.png");
    }
    public CategoryDataset getBarData(double[][] data, String[] rowKeys,
                                      String[] columnKeys) {
        return DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
    }
    public String createStackedBarChart(CategoryDataset dataset, String xName,
                                        String yName, String chartTitle, String charName) {

        JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle,
                xName,
                yName,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );

        chart.setTextAntiAlias(false);

        chart.setBackgroundPaint(Color.WHITE);


        chart
                .setTitle(new TextTitle(chartTitle, new Font("Sufyan", Font.BOLD,
                        25)));

        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);


        CategoryPlot plot = chart.getCategoryPlot();


        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.gray);


        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        vn.setUpperBound(1);
        DecimalFormat df = new DecimalFormat("#0.00");
        vn.setNumberFormatOverride(df);
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setLabelFont(labelFont);
        domainAxis.setTickLabelFont(labelFont);



        domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);

        plot.setDomainAxis(domainAxis);


        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(labelFont);
        rangeAxis.setTickLabelFont(labelFont);
        rangeAxis.setUpperMargin(0.1);
        rangeAxis.setLowerMargin(0.1);
        rangeAxis.setRange(0,400);
        plot.setRangeAxis(rangeAxis);

        StackedBarRenderer renderer = new StackedBarRenderer();
        renderer.setMaximumBarWidth(0.05);
        renderer.setBaseOutlinePaint(Color.BLACK);
        renderer.setDrawBarOutline(true);


        renderer.setSeriesPaint(0, new Color(204, 255, 204));
        renderer.setSeriesPaint(1, new Color(255, 204, 153));


        renderer.setItemMargin(0.4);

        plot.setRenderer(renderer);


        FileOutputStream fos_jpg = null;
        try {
            String CHART_PATH = "C:\\Github Repo\\";
            String chartName = CHART_PATH + charName;
            fos_jpg = new FileOutputStream(chartName);
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 1000, 1000, true, 10);
            return chartName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fos_jpg.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
