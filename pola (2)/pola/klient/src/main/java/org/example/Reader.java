package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread{
   private Socket socket;
   private BufferedReader reader;

    public Reader(Socket socket) {
        this.socket = socket;
        try{
            InputStream is = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
        }catch(IOException exception){
            System.out.println("Error :"+exception.getMessage());
        }

    }
    public void run(){
        while(true){
            try{
                String resp = reader.readLine();
                System.out.println(resp);
            }catch(IOException exception){
                System.out.println("Error XXX : "+ exception.getMessage());
                break;
            }
        }
    }

}
