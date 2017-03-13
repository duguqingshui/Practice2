package com.example.practice.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by AMOBBS on 2016/12/8.
 */

public class LoadUtils {

    private static final String TAG = "uploadFile";
    private static HttpURLConnection conn = null;
    private static DataOutputStream dout = null;
    private static FileInputStream fin = null;

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的url
     * @return 返回响应的内容
     */
    public static void uploadFile(final File file, final String RequestURL) {
        final String result = null;
        final String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        final String PREFIX = "--", LINE_END = "\r\n";
        final String CONTENT_TYPE = "multipart/form-data";   //内容类型

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(RequestURL);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(3000);
                    conn.setConnectTimeout(3000);
                    conn.setDoInput(true);  //允许输入流
                    conn.setDoOutput(true); //允许输出流
                    conn.setUseCaches(false);  //不允许使用缓存
                    conn.setRequestMethod("POST");  //请求方式
                    conn.setRequestProperty("Charset", "utf-8");  //设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

                    if (file != null) {
                        // 当文件不为空，把文件包装并且上传
                        dout = new DataOutputStream(conn.getOutputStream());
//                        StringBuffer sb = new StringBuffer();
//                        sb.append(PREFIX);
//                        sb.append(BOUNDARY);
//                        sb.append(LINE_END);
//                        /**
//                         * 这里重点注意：
//                         * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
//                         * filename是文件的名字，包含后缀名的   比如:abc.png
//                         */
//                        sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
//                        sb.append("Content-Type: application/octet-stream; charset=" + "utf-8" + LINE_END);
//                        sb.append(LINE_END);
//                        dout.write(sb.toString().getBytes());
                        fin = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = fin.read(bytes)) != -1) {
                            dout.write(bytes, 0, len);
                        }
//                        dout.write(LINE_END.getBytes());
//                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//                        dout.write(end_data);
                        dout.flush();
                        Log.i(TAG, conn.getResponseCode()+"");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (dout != null && fin != null && conn != null) {
                        try {
                            dout.close();
                            fin.close();
                            conn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private static HttpURLConnection connection = null;
    private static InputStream in = null;
    private static FileOutputStream fos = null;

    /**
     * 从tomcat下载文件
     *
     * @param downLoadUrl 下载地址
     * @param fileName    存放在本的路径和名字
     */
    public static void downLodaFile(final String downLoadUrl, final File fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downLoadUrl);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setReadTimeout(3000);              //读取超时时间
                    connection.setConnectTimeout(3000);           //连接超时时间
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");          //请求方式
                    //connection.setRequestProperty("Charset", "utf-8");  //设置编码

                    Log.i("响应码", connection.getResponseCode() + "");

                    if (connection.getResponseCode() == 200) {
                        in = connection.getInputStream();
                        fos = new FileOutputStream(fileName);
                        byte[] b = new byte[1024];
                        int len = 0;
                        while ((len = in.read(b)) != -1) {
                            fos.write(b, 0, len);
                        }
                    } else {
                        Log.i("downLoadFile", "文件下载失败");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null && fos != null && connection != null) {
                        try {
                            in.close();
                            fos.close();
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

}
