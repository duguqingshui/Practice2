package com.example.practice.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by AMOBBS on 2016/12/7.
 */

public class TransitionUtils {

    /**
     * 文本文件转换为指定编码的字符串
     *
     * @param file     文本文件
     //* @param encoding 编码类型
     * @return 转换后的字符串
     */
    public static String file2String(File file, String encoding) {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(new FileInputStream(file), encoding);
            } else {
                reader = new InputStreamReader(new FileInputStream(file));
            }
            //reader = new InputStreamReader(new FileInputStream(file));
            //将输入流写入输出流
            char[] buffer = new char[1024];
            int len = 0;
            while ((len = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, len);
            }
            //返回转换结果
            return writer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
     *
     * @param reStr            原字符串
     * @param filePath 文件路径
     * @return 成功标记
     */
    public static boolean string2File(String reStr, String filePath) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            File file = new File(filePath);
//            //如果该文件的父文件不存在则创建一个父文件
//            if (!distFile.getParentFile().exists()){
//                distFile.getParentFile().mkdirs();
//            }
            //如果文件不存在则创建一个新文件
            if(!file.exists()){
                file.mkdirs();
            }
            reader = new BufferedReader(new StringReader(reStr));
            writer = new BufferedWriter(new FileWriter(file));
            char[] buf = new char[1024];         //字符缓冲区
            int len = 0;
            while ((len = reader.read(buf)) != -1) {
                writer.write(buf, 0, len);
            }
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null && writer != null) {
                try {
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
