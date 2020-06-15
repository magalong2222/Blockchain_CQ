package com.example.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import javax.imageio.ImageIO;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.Properties;
import java.util.regex.*;

public class exportExcel {
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
        return exportBigDataExcel(dataList);
    }
//    public static void main(String[] args) {
//        List<String[]> dataList = createData();
//        exportBigDataExcel("C:\\excel\\22.xls", dataList);
//    }
}