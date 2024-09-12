package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Writer extends Thread{
    private Socket socket;
    private PrintWriter printWriter;
    private Client client;
    private Scanner scanner;

    public Writer(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        scanner = new Scanner(System.in);
        try{
            printWriter = new PrintWriter(socket.getOutputStream(),true);
        }catch(IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    public void run(){
        System.out.println("Podaj login: ");
        String login = scanner.nextLine();
        client.setName(login);
        printWriter.println(login);
        String clientMessage;
        do{
            clientMessage = scanner.nextLine();
            printWriter.println(clientMessage);
        }while(!clientMessage.equals("/exit"));
        try{
            socket.close();
        }
        catch(IOException exception){
            System.out.println(exception.getMessage());
        }
    }
}
