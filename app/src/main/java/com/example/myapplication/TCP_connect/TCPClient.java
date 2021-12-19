package com.example.myapplication.TCP_connect;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements Runnable {
        public enum token_type{}
        public static final String TAG = "MyTCP";
        public static final String RECEIVE_ACTION = "GetTCPReceive";
        public static final String RECEIVE_STRING = "ReceiveString";
        public static final String RECEIVE_BYTES = "ReceiveBytes";
        //private String TAG = TCPServer.TAG;
        private PrintWriter pw;
        private InputStream is;
        private DataInputStream dis;
        private String  serverIP;
        private int serverPort;
        private boolean isRun = true;
        private Socket socket;
        private Context context;
        private BufferedWriter bw;  //取得網路輸出串流
        private BufferedReader br;  //取得網路輸入串流
        public String Name;

        public TCPClient(String ip , int port,String name,Context context){
            this.serverIP = ip;
            this.serverPort = port;
            this.Name = name;
            this.context = context;
        }
        public boolean getStatus(){
            return isRun;
        }

        public void closeClient(){
            isRun = false;
        }

        public void send(byte[] msg){
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String msg){
            Log.d("send to server",msg);
            pw.print(msg);
            pw.flush();
        }
        private String receive_str(BufferedReader br) throws IOException {
            //https://stackoverflow.com/questions/5694998/bufferedreader-values-into-char-array
            char[] tmp  =new char[1024];
            int str_end = (br.read(tmp,0,1024));
            Log.d("receive_str: size ", String.valueOf(str_end));
            //final String get_msg = new String(tmp, 0, str_end-4);
            StringBuilder filter_str = new StringBuilder();
            int j=0;
            while(tmp[j]!='\0'){
                filter_str.append(tmp[j++]);
                //Log.d("receive_str: ", filter_str);
            }
            return filter_str.toString();
        }
        @Override
        public void run() {
            byte[] buff = new byte[1024];
            try {
                /**將Socket指向指定的IP & Port*/
                socket = new Socket(serverIP,serverPort);
                socket.setSoTimeout(5000);
                pw = new PrintWriter(socket.getOutputStream(),true);
                is = socket.getInputStream();
                dis = new DataInputStream(is);
                br = new BufferedReader(new InputStreamReader(is));
                send(Name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (isRun){
                try {
                    final String get_msg = receive_str(br);
                    //int rcvLen = dis.read(buff);
                    //String rcvMsg = new String(buff, 0, rcvLen, "utf-8");
                    //Log.d(TAG, "收到訊息: "+ rcvMsg);
                    Log.d(TAG, "收到訊息: "+ get_msg);

                    Intent intent =new Intent();
                    intent.setAction(TCPClient.RECEIVE_ACTION);
                   // intent.putExtra(TCPClient.RECEIVE_STRING, rcvMsg);
                    intent.putExtra(TCPClient.RECEIVE_STRING, get_msg);
                    context.sendBroadcast(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            try {
                destroy();
                pw.close();
                is.close();
                dis.close();
                socket.close();
                Log.d(TAG, "關閉Client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void destroy() {
        //super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    send("exit");
                    /*
                    bw.write("exit\n");
                    //立即發送
                    bw.flush();

                     */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

