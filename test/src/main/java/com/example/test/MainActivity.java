package com.example.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_pic;
    private Button bt_pic;
    private Button bt_record;
    private HttpURLConnection conn;
    private InputStream in;
    private FileOutputStream fout;
    private String path;
    //private String fileName = "p1481350927884.jpg";
    //private String fileName = "r1481350958868.amr";
    private String fileName = "p1481357301242.jpg";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    File file = new File(path);
                    if(file.exists() && file != null){
                        MediaPlayer mPlayer = new MediaPlayer();
                        try {
                            mPlayer.reset();
                            mPlayer.setDataSource(path);
                            mPlayer.prepare();
                            mPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    iv_pic.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_record = (Button) findViewById(R.id.bt_record);
        bt_pic = (Button) findViewById(R.id.bt_pic);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);

        bt_record.setOnClickListener(this);
        bt_pic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_record:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+fileName;
                            String urlLoad = "http://192.168.1.104:8080/LoadServlet/"+fileName;
                            URL url = new URL(urlLoad);
                            conn = (HttpURLConnection) url.openConnection();

                            conn.setReadTimeout(3000);
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            if(conn.getResponseCode() == 200){
                                in = conn.getInputStream();
                                fout = new FileOutputStream(new File(path));
                                byte[] b = new byte[1024];
                                int len = 0;
                                while ((len = in.read(b)) != -1){
                                    fout.write(b, 0, len);
                                }
                            }

                            handler.sendEmptyMessage(0);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            if(in != null && fout != null && conn != null){
                                try {
                                    in.close();
                                    fout.close();
                                    conn.disconnect();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
                break;
            case R.id.bt_pic:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+fileName;
                            String urlLoad = "http://192.168.1.104:8080/LoadServlet/"+fileName;
                            URL url = new URL(urlLoad);
                            conn = (HttpURLConnection) url.openConnection();

                            conn.setReadTimeout(3000);
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            if(conn.getResponseCode() == 200){
                                in = conn.getInputStream();
                                fout = new FileOutputStream(new File(path));
                                byte[] b = new byte[1024];
                                int len = 0;
                                while ((len = in.read(b)) != -1){
                                    fout.write(b, 0, len);
                                }
                            }

                            handler.sendEmptyMessage(1);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            if(in != null && fout != null && conn != null){
                                try {
                                    in.close();
                                    fout.close();
                                    conn.disconnect();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
                break;
        }
    }
}
