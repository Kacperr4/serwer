package org.example;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private int port = 12345;
    private String hostname = "localhost";
    private String name;

    public Client(int port, String hostname) {
        this.port = port;
        this.hostname = hostname;
    }

    public void execute(){
        try{
            Socket socket = new Socket(hostname,port);
            Reader reader = new Reader(socket);
            reader.start();
            Writer writer = new Writer(socket,this);
            writer.start();
        }catch(UnknownHostException exception){
            System.out.println("Error: "+ exception.getMessage());
        }catch(IOException exception){
            System.out.println("Error: "+ exception.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {Client client = new Client(12345,"localhost");
        client.execute();
    }
}