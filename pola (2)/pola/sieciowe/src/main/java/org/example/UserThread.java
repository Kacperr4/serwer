package org.example;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread{
    private Socket socket;
    private String userName;
    private PrintWriter printWriter;

    public UserThread(Socket socket) {
        this.socket = socket;
    }

    public void printUsers(){
        if(Serwer.activeUsers()){
            printWriter.println("aktywni uzytkownicy " + Serwer.getLogins());
        }else{
            printWriter.println("Brak aktywnych uzytkwonikow");
        }
    }

    public void sendMessage(String message){
        printWriter.println(message);
    }

    public void sendPrivateMessage(String login, String message){
        for(UserThread userThread : Serwer.userThreads){
            if(userThread.userName.equals(login)){
                userThread.sendMessage("[PRIV]["+userName+"]: "+message);
                return;
            }
        }
        printWriter.println("Uzytkownik o takim nicku nie jest aktywny.");
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            printWriter = new PrintWriter(output, true);

            printUsers();
            String login = reader.readLine();
            Serwer.addUser(login);
            this.userName = login;
            Serwer.broadcast("Podlaczyl sie uzytkownik o nazwie: "+ login,this);

            String clientMessage;
            try{
                do{
                    clientMessage = reader.readLine();
                    if(clientMessage.startsWith("/w ")){ // "/w test testowa wiadomosc testowa wiadomosc"
                        String[] splitMessage = clientMessage.split(" ", 3);
                        if (splitMessage.length == 3){
                            sendPrivateMessage(splitMessage[1], splitMessage[2]);
                        } else{
                            printWriter.println("Zla skladnia prywatnej wiadomosci");
                        }
                    }else if(clientMessage.equals("/online")){ // "/online"
                        printUsers();
                    }else{  // wiadomosc        [username]: blahblahblah
                        if(!clientMessage.equals("/exit")){
                            Serwer.broadcast("["+userName +"]: "+clientMessage,this);
                        }
                    }
                }while(!clientMessage.equals("/exit"));
                Serwer.removeUser(userName, this);
                socket.close();
                Serwer.broadcast(userName+" wyszedl.", this);
            }catch(Exception e){
                Serwer.removeUser(userName, this);
                socket.close();
                Serwer.broadcast(userName+" utracil polaczenie z serwerem.", this);
            }


        }catch(IOException e){
            System.out.println("Error w watku uzytkownika: "+ e.getMessage());
        }
    }


}
