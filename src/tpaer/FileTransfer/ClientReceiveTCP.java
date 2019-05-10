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
public class ClientReceiveTCP {
    
         


    public ClientReceiveTCP() throws SocketException, IOException {
        
    }
    


    
    

    public void receivefile(String ip) throws IOException {

       
                         //create the socket on port 5000
                         Socket s=new Socket(ip,5000);  
                         DataInputStream din=new DataInputStream(s.getInputStream());  
                         DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
                         BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  

                         System.out.println("Press enter to proceed");
                         String str="",filename="",filename2="/home/core/Desktop/testeumdois.txt";  
                         
                         try{
                                     str=br.readLine(); 
                         
                             
                                 //str=br.readLine(); 

                                 dout.writeUTF(str); 
                                 dout.flush();  

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


