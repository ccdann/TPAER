/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author ccdann
 */
public class Client {
    
         String ip ;


    public Client(String ip) throws SocketException, IOException {
        this.ip = ip;
    }
    


    
    

    public void receivefile() throws IOException {

       
                         //create the socket on port 5000
                         Socket s=new Socket("10.0.0.2",5000);  
                         DataInputStream din=new DataInputStream(s.getInputStream());  
                         DataOutputStream dout=new DataOutputStream(s.getOutputStream());  


                         String str="",filename="",filename2="/home/core/Desktop/testeumdois.txt";  
                         
                         try{
                        
                      
                             
                                 //str=br.readLine(); 

                    

                                 filename=din.readUTF();
                                 
                                 System.out.println("Receving file: "+filename);
                                // filename="client"+filename;
                                 System.out.println("Saving as file: "+filename);
                         //
                         long sz=Long.parseLong(din.readUTF());
                         System.out.println ("File Size: "+(sz/(1024*1024))+" MB");

                         byte b[]=new byte [1024];
                         System.out.println("Receving file..");
                         FileOutputStream fos=new FileOutputStream(new File(filename2),true);
                         long bytesRead;
                         do
                         {
                         bytesRead = din.read(b, 0, b.length);
                         fos.write(b,0,b.length);
                         }while(!(bytesRead<1024));
                         System.out.println("Completed");
                         fos.close(); 
                         dout.close();  	
                         s.close();  
                         }
                         catch(EOFException e)
                         {
                                 //do nothing
                         }



         }
      }


