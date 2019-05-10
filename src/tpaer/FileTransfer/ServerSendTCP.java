/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccdann
 */
public class ServerSendTCP extends Thread {

    
    
    //String filename = "/home/core/Desktop/teste.txt";
    String filename;
    
    public ServerSendTCP(String filename) throws SocketException, IOException{
     this.filename = filename;   
    }

   // FileOutputStream f = new FileOutputStream("/home/core/Desktop/teste.txt");
    

@Override
    public void run() {
      
            while(true){
                    
      
                    //create server socket on port 5000
            ServerSocket ss = null; 
                try {
                    ss = new ServerSocket(5000);
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            System.out.println ("Waiting for request");
            Socket s = null;  
                try {
                    s = ss.accept();
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            System.out.println ("Connected With "+s.getInetAddress().toString());
            DataInputStream din = null;  
                try {
                    din = new DataInputStream(s.getInputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            DataOutputStream dout = null;  
                try {
                    dout = new DataOutputStream(s.getOutputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            try{
            String str="";  

            str=din.readUTF();
            System.out.println("SendGet....Ok");

            if(!str.equals("stop")){  

            System.out.println("Sending File: "+filename);
            dout.writeUTF(filename);  
            dout.flush();  

            File f=new File(filename);
            FileInputStream fin=new FileInputStream(f);
            long sz=(int) f.length();

            byte b[]=new byte [1024];

            int read;

            dout.writeUTF(Long.toString(sz)); 
            dout.flush(); 

            System.out.println ("Size: "+sz);
            System.out.println ("Buf size: "+ss.getReceiveBufferSize());

            while((read = fin.read(b)) != -1){
                dout.write(b, 0, read); 
                dout.flush(); 
            }
            fin.close();

            System.out.println("..ok"); 
            dout.flush(); 
            }  
            dout.writeUTF("stop");  
            System.out.println("Send Complete");
            dout.flush(); 
            din.close();
            s.close();
            ss.close();
            break;
            }
            catch(Exception e)
            {
                    e.printStackTrace();
                    System.out.println("An error occured");
            }
                try {  
                    din.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {  
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {  
                    ss.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerSendTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
                
              
            }
            
           

    }
    
    

}



    
    
    
    

