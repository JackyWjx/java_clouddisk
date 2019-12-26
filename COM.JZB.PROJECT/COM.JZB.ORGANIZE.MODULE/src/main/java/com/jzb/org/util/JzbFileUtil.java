package com.jzb.org.util;

import com.jzb.base.entity.media.FileType;
import com.jzb.base.util.JzbRandom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * 文件上传工具类
 */
public class JzbFileUtil {
    private volatile static int hour;
    private volatile static int fileStep = 1;

    /**
     * 利用java原生的摘要实现SHA256加密
     */
    public static String SHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    /***
     * 获取文件路径
     */
    public synchronized static String getFilePath() {
        String typeFile = "";
        String fileName = "";
        //年月日
        Calendar now = Calendar.getInstance();
        int yyyy = now.get(Calendar.YEAR);
        int mm = now.get(Calendar.MONTH) + 1;
        int dd = now.get(Calendar.DAY_OF_MONTH);
        int hh = now.get(Calendar.HOUR_OF_DAY);
        if (hour == 0 || hh != hour) {
            fileStep = 1;
            typeFile = "01";
        } else {
            typeFile = fileStep > 9 ? fileStep + "" : "0" + fileStep;
        }
//        String path = "JZB/"+yyyy+"/"+mm+"/"+dd+"/"+hh+"/"+typeFile;
        String path =  "/images/" +yyyy + "/" + mm + "/" + dd + "/" + hh + "/" + typeFile;

        //文件下有多少文件
        File file = new File(path); // 图片存放路径
        File list[] = file.listFiles();
        if (list == null) {
        } else {
            if (list.length >= 1000) {
                fileStep++;
                typeFile = fileStep > 9 ? fileStep + "" : "0" + fileStep;
                path = "/images/" + yyyy + "/" + mm + "/" + dd + "/" + hh + "/" + typeFile;
            }
        }
        if (new File(path).mkdirs()) {
            fileName = now.get(Calendar.MINUTE) + "" + now.get(Calendar.SECOND) + "" + now.get(Calendar.MILLISECOND) + JzbRandom.getRandomCharLow(9);
        } else {
            // file create failed.\
            fileName = now.get(Calendar.MINUTE) + "" + now.get(Calendar.SECOND) + "" + now.get(Calendar.MILLISECOND) + JzbRandom.getRandomCharLow(9);
        }
        return path + "/" + fileName;
    }

    /**
     * 将文件头转换成16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     */
    public static String getFileContent(InputStream is) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            is.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     */
    public static FileType getType(String fileHead) throws IOException {
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }

        return null;
    }

    /**
     * 表示文件类型
     */
    public static String isFileType(FileType value) {
        String type = "其他";// 其他
        // 图片
        FileType[] pics = {FileType.JPEG, FileType.PNG, FileType.GIF, FileType.TIFF, FileType.BMP, FileType.DWG, FileType.PSD};

        FileType[] docs = {FileType.RTF, FileType.XML, FileType.HTML, FileType.CSS, FileType.JS, FileType.EML, FileType.DBX, FileType.PST, FileType.XLS_DOC, FileType.XLSX_DOCX, FileType.VSD,
                FileType.MDB, FileType.WPS, FileType.WPD, FileType.EPS, FileType.PDF, FileType.QDF, FileType.PWL, FileType.ZIP, FileType.RAR, FileType.JSP, FileType.JAVA, FileType.CLASS,
                FileType.JAR, FileType.MF, FileType.EXE, FileType.CHM};

        FileType[] videos = {FileType.AVI, FileType.RAM, FileType.RM, FileType.MPG, FileType.MOV, FileType.ASF, FileType.MP4, FileType.FLV, FileType.MID};

        FileType[] tottents = {FileType.TORRENT};

        FileType[] audios = {FileType.WAV, FileType.MP3};

        FileType[] others = {};

        // 图片
        for (FileType fileType : pics) {
            if (fileType.equals(value)) {
                type = "图片";
            }
        }
        // 文档
        for (FileType fileType : docs) {
            if (fileType.equals(value)) {
                type = "文档";
            }
        }
        // 视频
        for (FileType fileType : videos) {
            if (fileType.equals(value)) {
                type = "视频";
            }
        }
        // 种子
        for (FileType fileType : tottents) {
            if (fileType.equals(value)) {
                type = "种子";
            }
        }
        // 音乐
        for (FileType fileType : audios) {
            if (fileType.equals(value)) {
                type = "音乐";
            }
        }
        return type;
    }

    /**
     * 获取文件Id
     */
    public static String getFileId() {
        String fileId = JzbRandom.getRandomCharLow(8);
        //获取年月日时
        Calendar now = Calendar.getInstance();
        Integer yyyy = now.get(Calendar.YEAR);
        String yyy = yyyy.toString();
        yyy = yyy.substring(2, 4);
        fileId = fileId + yyy;
        int mm = now.get((Calendar.MONTH)) + 1;
        if (mm >= 10) {
            if (mm == 11) {
                fileId = fileId + "b";
            } else if (mm == 12) {
                fileId = fileId + "c";
            } else {
                fileId = fileId + "a";
            }
        } else {
            fileId = fileId + mm;
        }
        int dd = now.get(Calendar.DAY_OF_MONTH);
        fileId = fileId + dd;
        int hh = now.get(Calendar.HOUR_OF_DAY);
        fileId = fileId + hh;
        return fileId;
    }
}
