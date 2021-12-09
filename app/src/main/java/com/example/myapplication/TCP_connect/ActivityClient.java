package com.example.myapplication.TCP_connect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ActivityClient extends AppCompatActivity {
    private Thread thread;
    private Socket clientSocket;//客戶端的socket
    private BufferedWriter bw;  //取得網路輸出串流
    private BufferedReader br;  //取得網路輸入串流

    private String send_msg;

    TextView tv;
    TextView title;
    public void send_str(){
        Button send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                String s = send_msg;
                try {
                    bw.write(s+"\n");
                    bw.flush();
                    Log.d("send to server",s);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }).start();
            }
        });
    }
    /*
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_connect:
                connect();
                break;
            case R.id.bn_send:
                sendStr();
                break;
            case R.id.tx_receive:
                clear();
                break;
        }
    }
    */

    public String receive_str(BufferedReader br) throws IOException {
        //https://stackoverflow.com/questions/5694998/bufferedreader-values-into-char-array
        char[] tmp  =new char[1024];
        int str_end = (br.read(tmp,0,1024));
        Log.d("receive_str: size ", String.valueOf(str_end));
        //final String get_msg = new String(tmp, 0, str_end-4);
        String filter_str = "";
        int j=0;
        while(tmp[j]!='\0'){
            filter_str+=tmp[j++];
            //Log.d("receive_str: ", filter_str);
        }
        return filter_str;
    }

    private void refreshUI(final String msg) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(msg);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        tv = (TextView) findViewById(R.id.textView3);
        send_msg = "5 中文";
        send_str();
        thread=new Thread(Connection);
        thread.start();
    }

    //連結socket伺服器做傳送與接收
    private Runnable Connection=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try{
                //輸入 Server 端的 IP
                InetAddress serverIp = InetAddress.getByName("140.117.168.167");
                //InetAddress serverIp = InetAddress.getByName("10.0.2.2");
                //自訂所使用的 Port(1024 ~ 65535)
                int serverPort = 8888;
                //建立連線
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                //取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //檢查是否已連線
                while (clientSocket.isConnected()) {
                    //宣告一個緩衝,從br串流讀取 Server 端傳來的訊息

                    final String get_msg = receive_str(br);
                    if(get_msg!=null){
                        refreshUI(get_msg);
                        Log.d("get from server : ", String.valueOf(get_msg.length())+" : "+get_msg);
                    }
                }
            }catch(Exception e){
                //當斷線時會跳到 catch,可以在這裡處理斷開連線後的邏輯
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
                finish();    //當斷線時自動關閉 Socket
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //傳送離線 Action 給 Server 端
                    //jsonWrite = new JSONObject();
                    //jsonWrite.put("action","離線");
                    //寫入
                    //bw.write(jsonWrite + "\n");
                    bw.write("exit\n");
                    //立即發送
                    bw.flush();
                    //關閉輸出入串流後,關閉Socket
                    bw.close();
                    br.close();
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
