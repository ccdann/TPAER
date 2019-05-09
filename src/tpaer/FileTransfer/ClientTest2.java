/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author core
 */
public class ClientTest2 {
    
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
   
     public void receivefile(String ipsend, String query) throws IOException{
            String ip= ipsend;
            Socket socket=new Socket(ip,4444); 
            //System.out.println("IP " + ip);
             //socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());

                      
            oos.writeObject(query);
            
            System.out.println("Sending request to Socket Server");
            
          
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = null;
         
            try {
              message = (String) ois.readObject();
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(ClientTest2.class.getName()).log(Level.SEVERE, null, ex);
          }
          
            
            
            
            if(message.startsWith("302")){
            System.out.println("Message: " + message + " From "+ socket.getInetAddress());
            System.out.println("Do you want to start tranfer?! 1-Yes ; 0-No");
            BufferedReader inputtrf = new BufferedReader(new InputStreamReader(System.in)); 
            String intrf="";           
            intrf=inputtrf.readLine(); 
            
            if(intrf.equals("1")){
            System.out.println("Starting . . . "); 
            Client cl = new Client("10.0.0.0");
            cl.receivefile();
            }else{
            System.out.println("Abort . . . "); 
            }
            
            
            }
            
            //close resources
            ois.close();
            oos.close();
            
        
        
    }
}
    
    
