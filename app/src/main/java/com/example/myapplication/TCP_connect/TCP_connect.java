package com.example.myapplication.TCP_connect;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCP_connect {
    private static Thread thread;
    private static Thread send_Thread;
    private Socket clientSocket;//客戶端的socket
    private BufferedWriter bw;  //取得網路輸出串流
    private BufferedReader br;  //取得網路輸入串流
    String IP,Name;
    int Port;

    String send_msg;
    String server_msg;

    public TCP_connect(String ip,int port,String userName){
        IP = ip;
        Port = port;
        Name = userName;
        send_msg = Name + " info";




        //thread=new Thread(Connection);
        //thread.start();
        Log.d("connect start","IP : "+IP + ", Port : "+Port +", Name : "+ Name);
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                send_str(send_msg);
            }
        }).start();

         */
        //send_Thread = new Thread(send_str,send_msg);
        //send_Thread.start();

    }
    public void send_str(String str){
        String s = str;
        try {
            bw.write(s+"\n");
            bw.flush();
            Log.d("send to server",s);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get_Msg(){
        if(server_msg!=null){
            String msg = server_msg;
            server_msg="";
            return msg;
        }
        else
            return "";
    }

    public void send_Msg(String s){
        send_msg = s;
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

    //連結socket伺服器做傳送與接收
    public void Connection(){
          // TODO Auto-generated method stub
            try{
                //輸入 Server 端的 IP
                InetAddress serverIp = InetAddress.getByName(IP);
                //InetAddress serverIp = InetAddress.getByName("10.0.2.2");
                //自訂所使用的 Port(1024 ~ 65535)
                int serverPort = Port;
                //建立連線
                clientSocket = new Socket(serverIp, serverPort);
                //取得網路輸出串流
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                //取得網路輸入串流
                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                send_str(send_msg);
                //檢查是否已連線
                while (clientSocket.isConnected()) {
                    //宣告一個緩衝,從br串流讀取 Server 端傳來的訊息

                    final String get_msg = receive_str(br);
                    if(get_msg!=null){
                        server_msg = get_msg;
                        Log.d("get from server : ", String.valueOf(get_msg.length())+" : "+get_msg);

                    }
                }
            }catch(Exception e){
                //當斷線時會跳到 catch,可以在這裡處理斷開連線後的邏輯
                e.printStackTrace();
                Log.e("text","Socket連線="+e.toString());
            }
        }


    public void destroy() {
        //super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
