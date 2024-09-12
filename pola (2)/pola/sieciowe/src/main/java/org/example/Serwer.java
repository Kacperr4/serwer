package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Serwer {
    private static final int port = 12345;
    private static Set<String> logins = new HashSet<>();
    public static Set<UserThread> userThreads = new HashSet<>();


    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Serwer uruchomiony na porcie: "+ port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Nowe polaczenie, polaczyl sie nowy uzytkownik");
                UserThread userThread = new UserThread(socket);
                userThreads.add(userThread);
                userThread.start();
            }
        } catch (IOException exception){
            System.out.println("Error: "+exception.getMessage());
        }

    }

    public static void broadcast(String message, UserThread actualUser){
        for(UserThread userThread: userThreads){
            if(actualUser != userThread){
                userThread.sendMessage(message);
            }
        }
    }

    public static void addUser(String name){
        logins.add(name);
    }
    public static void removeUser(String name, UserThread user){
        if(logins.remove(name)){
            userThreads.remove(user);
            System.out.println("Użytkownik "+ name + " opuscil serwer");
        }
    }

    public static Set<String> getLogins() {
        return logins;
    }
    public static boolean activeUsers(){
        return !getLogins().isEmpty();
    }

}