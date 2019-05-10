/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

/**
 *
 * @author core
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import tpaer.Node;
import tpaer.RoutingTable;
import static tpaer.Settings.secSendmsg;

 
public class ServerQueryFiles extends Thread{
 
    private ServerSocket serv;
    private Socket client;
    private File myFile;
    
    String filename = "/home/core/Desktop/teste.txt";
    // This will reference one line at a time
    String line = null;
    String holder=null;
    String clientWord;
    int bytNumber;
    String str="";  
    List<RoutingTable> rt =  new ArrayList<RoutingTable>();
    HashMap<String, String> files = new HashMap<String, String>();
    Random rand = new Random();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    int numfilesrandom = (int)(Math.random()*7);
    //int numfilesrandom = 5;
    private static ServerSocket serverSocket;
    private static Socket clientSocket = null;
    String file = null;
    
    
    
    
    
  
        public ServerQueryFiles(List<RoutingTable> rt,  HashMap<String, String> files) throws IOException{
           
            
            this.rt = rt;
            this.files = files;
        }
 



    @Override
      public void run() {
      
          addfiles(numfilesrandom);

         try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started.");
        } catch (Exception e) {
            System.err.println("Port already in use.");
            System.exit(1);
        }

        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            //convert ObjectInputStream object to String
            String message = null;
            try {
                message = (String) ois.readObject();
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Message Received: " + message);
            //create ObjectOutputStream object
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
               
               
             //   System.out.println("STATES " + s.isAlive());
              
                
                if(files.get(message)!=null){
                 oos.writeObject("302 Found file"); 
                 file = files.get(message);
                 //System.out.println("FILE!!"+  files.get(message));        
                }else{
                 oos.writeObject("Not found file");
                }
                
                if(message.equals("200")){
                    ServerSendTCP s = new ServerSendTCP(file);
                    s.start(); 
                }
                
                //if(message.equals("musica")){
                //oos.writeObject("302 Found file");
                //}else{
                // oos.writeObject("Not found file");
                //}
                //write object to Socket
                
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //close resources
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerQueryFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) 
                break;
        }
 
        }
      
      public void addfiles(int numfilesrandom){
          
            name.add("teste");
            name.add("need");
            name.add("hello");
            name.add("you");
            name.add("bye");
            name.add("get");
            name.add("x");
            address.add("/home/core/Desktop/files/teste.txt");
            address.add("/home/core/Desktop/files/need.txt");
            address.add("/home/core/Desktop/files/hello.txt");
            address.add("/home/core/Desktop/files/you.txt");
            address.add("/home/core/Desktop/files/bye.txt");
            address.add("/home/core/Desktop/files/get.txt");
            address.add("/home/core/Desktop/files/x.txt");
            
                for(int i=0;i<numfilesrandom;i++){
                    int random = rand.nextInt(name.size());
                        String namelocal = name.get(random);
                        String addrlocal = address.get(random);
                         files.put(namelocal, addrlocal);

             }
      
      
      }
            
}

     
     
     
 
    
    
  

         
         
    
    
