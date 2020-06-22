package com.example.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.hssf.*;
import org.apache.poi.hssf.usermodel.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.*;
import javax.imageio.ImageIO;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

public class exportExcel {
    private static void  generateCQexcel() {
        List<String[]> dataList = new ArrayList<String[]>();
        String[] sheetname = {"结算清单","","","","","","","","","","","","","",""};
        dataList.add(sheetname);
        String[] sheetname1 = {"项目组：靖边钻井业务外包项目组","","","","","","","","","","","","","","单位：元"};
        dataList.add(sheetname1);
        String[] headStr = new String[15];
        headStr[0] = "序号";
        headStr[1] = "油田公司项目组";
        headStr[2] = "井号";
        headStr[3] = "直+斜进尺\n（米）";
        headStr[4] = "单价\n（元/米）";
        headStr[5] = "水平段\n（米）";
        headStr[6] = "单价\n（元/米）";
        headStr[7] = "取心进尺\n（米）";
        headStr[8] = "取心单价\n（元/米）";
        headStr[9] = "应结算金额";
        headStr[10] = "补偿合计";
        headStr[11] = "扣款合计";
        headStr[12] = "实际结算金额";
        headStr[13] = "结算合同号";
        headStr[14] = "备注";
        dataList.add(headStr);
        String[] contextStr1 = {"1","","","","","","","","","","","","","",""};
        String[] contextStr2 = {"2","","","","","","","","","","","","","",""};
        String[] contextStr3 = {"3","","","","","","","","","","","","","",""};
        String[] contextStr4 = {"4","","","","","","","","","","","","","",""};
        String[] contextStr5 = {"5","","","","","","","","","","","","","",""};
        String[] contextStr6 = {"合计","","","","","","","","","","","","","",""};
        String[] contextStr7 = {"承包商签字：\n\n\n\n\n\n\n                                      年    日    月","","","项目组生产副经理意见：\n\n\n\n\n\n\n ","","项目组技术副经理意见：\n\n\n\n\n\n\n ","","项目组经营副经理意见：\n\n\n\n\n\n\n ","","业务主管部门意见：\n\n\n\n\n\n\n                                      年    日    月","","","财务资产部门意见：\n\n\n\n\n\n\n                                      年    日    月","",""};
        dataList.add(contextStr1);
        dataList.add(contextStr2);
        dataList.add(contextStr3);
        dataList.add(contextStr4);
        dataList.add(contextStr5);
        dataList.add(contextStr6);
        dataList.add(contextStr7);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet();
        sh.setColumnWidth(1, 15 * 256);
        sh.setColumnWidth(3, 13 * 256);
        sh.setColumnWidth(9, 15 * 256);
        sh.setColumnWidth(5, 11 * 256);
        sh.setColumnWidth(6, 11 * 256);
        sh.setColumnWidth(7, 11 * 256);
        sh.setColumnWidth(8, 11 * 256);
        sh.setColumnWidth(12, 13 * 256);
        sh.setColumnWidth(13, 13 * 256);

        HSSFCellStyle mycellStyle = wb.createCellStyle();
        mycellStyle.setWrapText(true);
        mycellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        mycellStyle.setAlignment(HorizontalAlignment.CENTER);
        mycellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        mycellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        mycellStyle.setBorderTop(BorderStyle.THIN);//上边框
        mycellStyle.setBorderRight(BorderStyle.THIN);//右边框
        mycellStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        //为最后一行设置
        HSSFCellStyle mycellStyle1 = wb.createCellStyle();
        mycellStyle1.setWrapText(true);
        mycellStyle1.setBorderBottom(BorderStyle.THIN); //下边框
        mycellStyle1.setBorderLeft(BorderStyle.THIN);//左边框
        mycellStyle1.setBorderTop(BorderStyle.THIN);//上边框
        mycellStyle1.setBorderRight(BorderStyle.THIN);//右边框
        mycellStyle1.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle1.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle1.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        mycellStyle1.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        //为表名设置
        HSSFFont font1 = wb.createFont();
        font1.setFontName(HSSFFont.FONT_ARIAL);
        font1.setFontHeightInPoints((short) 16);
        font1.setBold(true);//设置为粗体
        HSSFCellStyle sheetname1Style = wb.createCellStyle();
        sheetname1Style.setWrapText(true);
        sheetname1Style.setVerticalAlignment(VerticalAlignment.CENTER);
        sheetname1Style.setAlignment(HorizontalAlignment.CENTER);
        sheetname1Style.setFont(font1);


        for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
            String[] str = dataList.get(i);
            Row row_value = sh.createRow(i);
            if (i == 0) {
                row_value.setHeight((short) 1000);
            }
            if (i == 9) {
                row_value.setHeight((short) 2000);
            }
            for (int j = 0, strLength = str.length; j < strLength; j++) {
                Cell cel_value = row_value.createCell(j);
                if (i == 9) {
                    cel_value.setCellStyle(mycellStyle1);
                }
                if (i > 1 && i < 9) {
                    cel_value.setCellStyle(mycellStyle);
                }
                if (i == 0) {
                    cel_value.setCellStyle(sheetname1Style);
                }
                cel_value.setCellValue(str[j]);
            }
        }
        CellRangeAddress region = new CellRangeAddress(8, 8, 0, 2);
        sh.addMergedRegion(region);
        CellRangeAddress region1 = new CellRangeAddress(9, 9, 0, 2);
        sh.addMergedRegion(region1);
        CellRangeAddress region2 = new CellRangeAddress(9, 9, 3, 4);
        sh.addMergedRegion(region2);
        CellRangeAddress region4 = new CellRangeAddress(9, 9, 5, 6);
        sh.addMergedRegion(region4);
        CellRangeAddress region5 = new CellRangeAddress(9, 9, 7, 8);
        sh.addMergedRegion(region5);
        CellRangeAddress region6 = new CellRangeAddress(9, 9, 9, 11);
        sh.addMergedRegion(region6);
        CellRangeAddress region7 = new CellRangeAddress(9, 9, 12, 14);
        sh.addMergedRegion(region7);
        //为表名设置
        CellRangeAddress region8 = new CellRangeAddress(0, 0, 0, 14);
        sh.addMergedRegion(region8);
        //为第二行设置
        CellRangeAddress region9 = new CellRangeAddress(1, 1, 3, 13);
        sh.addMergedRegion(region9);
        CellRangeAddress region10 = new CellRangeAddress(1, 1, 0, 2);
        sh.addMergedRegion(region10);
        FileOutputStream fileOut;
        try {
            String savePath = "C:" + File.separator + "corda" + File.separator + "结算清单.xls";
            File fileDir = new File(savePath.substring(0, savePath.lastIndexOf(File.separator)));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            fileOut = new FileOutputStream(savePath);
            wb.write(fileOut);
            fileOut.close();
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean exportBigDataExcel(List<String[]> dataList) {
//        long startTime = System.currentTimeMillis();
        boolean flag = false;

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sh = wb.createSheet();

        for (int i = 0, dataListSize = dataList.size(); i < dataListSize; i++) {
            String[] str = dataList.get(i);
            Row row_value = sh.createRow(i);
            for (int j = 0, strLength = str.length; j < strLength; j++) {
                Cell cel_value = row_value.createCell(j);
                cel_value.setCellValue(str[j]);
            }
        }

        FileOutputStream fileOut;
        try {
            Properties prop = System.getProperties();
            BufferedImage bufferImg = null;
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            String imagepath = "C:" + File.separator + "Users" + File.separator + prop.getProperty("user.name") + File.separator + "Downloads" + File.separator + "QRcode.jpg";
            String savePath = "C:" + File.separator + "corda" + File.separator + "report.xls";
            System.out.println(imagepath);
            File imagefile = new File(imagepath);
            if (!imagefile.exists()) {
                return false;
            }
            bufferImg = ImageIO.read(new File(imagepath));
            ImageIO.write(bufferImg, "png", byteArrayOut);

            HSSFPatriarch patriarch = sh.createDrawingPatriarch();

            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 50,
                    (short) 5, 1, (short) 7, 8);
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

//            System.out.println(prop.getProperty("user.name"));
//            System.out.println(savePath);
//            System.out.println(savePath.lastIndexOf(File.separator));

            File fileDir = new File(savePath.substring(0, savePath.lastIndexOf(File.separator)));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            fileOut = new FileOutputStream(savePath);
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            String image_path = "C:" + File.separator + "Users" + File.separator + prop.getProperty("user.name") + File.separator + "Downloads";
            File file = new File(image_path);
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (!file2.isDirectory()) {
                    String name = file2.getName();
                    String pattern = ".*QRcode.*";
                    boolean isMatch = Pattern.matches(pattern, name);
                    if (isMatch) {
                        file2.delete();
                    }
                }
            }
//            file.delete();
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("导出完毕,共耗时" + (endTime-startTime) + "毫秒");
        return flag;
    }

    private static List<String[]> createData (){
        List<String[]> dataList = new ArrayList<String[]>();
        String[] headStr = new String[8];
        headStr[0] = "买家";
        headStr[1] = "卖家";
        headStr[2] = "结算金额";
        headStr[3] = "代扣金额";
        dataList.add(headStr);

        for (int i = 0; i < 10; i++) {
            String[] str = new String[8];
            str[0] = "1";
            str[1] = "1";
            str[2] = "1";
            str[3] = "1";
            dataList.add(str);
        }
        return dataList;
    }

    public boolean Test(String[] datalist) {
//        List<String[]> dataList = createData();
        List<String[]> dataList = new ArrayList<String[]>();
        String[] headStr = new String[15];
        headStr[0] = "买家";
        headStr[1] = "卖家";
        headStr[2] = "结算金额";
        headStr[3] = "代扣金额";
        headStr[4] = "HASH value";
        headStr[5] = "签名";
        String[] data_list = new String[15];
        data_list[0] = datalist[0];
        data_list[1] = datalist[1];
        data_list[2] = datalist[2];
        data_list[3] = datalist[3];
        data_list[4] = datalist[4];
        data_list[5] = "";
        dataList.add(headStr);
        dataList.add(data_list);
        generateCQexcel();
        return exportBigDataExcel(dataList);
    }
//    public static void main(String[] args) {
//        List<String[]> dataList = createData();
//        exportBigDataExcel("C:\\excel\\22.xls", dataList);
//    }
}