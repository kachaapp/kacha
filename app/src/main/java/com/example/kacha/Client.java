package com.example.kacha;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client{

    public Client(){
        try{
            Log.e("----------loge","------------------------");
            Socket sock=new Socket("192.168.0.192",6013);
            Log.e("----------loge","connected");
            DataInputStream in=new DataInputStream(sock.getInputStream());
            DataOutputStream out=new DataOutputStream(sock.getOutputStream());
            Log.e( "----------loge","------------------------");
            Log.e("----------loge","------------------------");
            String str=new String("5");
            out.writeUTF(str);//向server传输数据
            String ret=in.readUTF();//从server获得结果
            System.out.println(ret);

            str=new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.writeUTF(str);//向server传输数据
            ret=in.readUTF();
            System.out.println(ret);

            out.close();
            in.close();
        }

        catch (IOException ioe){
            System.err.println(ioe);
        }
    }
}