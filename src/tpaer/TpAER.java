/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author ccdann
 */
public class TpAER {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SocketException, InterruptedException, ParseException {
        PeerDetectionListener peer = new PeerDetectionListener();
        
        
        

        String node = InetAddress.getLocalHost().getHostName();
        
         final String ip = "ff02::1";
         final int port = 9999;
        
        PeerDetection detect   = new PeerDetection(ip, port, peer);
        detect.start();
        
        Hello sendHello = new Hello(ip, port, node);
        
       
       
        
         Runnable helloRunnable = new Runnable() {
                public void run() {
                    try {
                        sendHello.send();
                    } catch (IOException ex) {
                        Logger.getLogger(TpAER.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   // sendHello.close();
                }
            };
         
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(helloRunnable, 0, 5, TimeUnit.SECONDS);

        
        
        
        
     

          
        System.out.println("Press 1 Client or Press 2 Server");

        
        
        Scanner scan= new Scanner(System.in);

        
        int num= scan.nextInt();
        
        
        
        if (num==1){ 
            
            /*
            
                System.out.println("Init Client......");
                try {
                final String ip = "ff02::1";
                final int port = 9999;
                Client client = new Client(ip, port);
                client.printMessage();
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            */
                
         }
            
        
        
        if (num==2){
            
            
            peer.showPeers();
      
            
          
        }
        
         if (num==3){   
         //   Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
          //  for (NetworkInterface netint : Collections.list(nets)) {
            //  displayInterfaceInformation(netint);
            //}
            
            
             NetworkInterface nets1 = NetworkInterface.getByName("eth0");
           
             byte[] mac = nets1.getHardwareAddress(); 
             
             System.out.printf("Hardware name: %s%n", Arrays.toString(nets1.getHardwareAddress()));  
             
             List<InterfaceAddress> interfaceAddresses = nets1.getInterfaceAddresses();    
             System.out.printf("InterfaceAddress: %s%n", interfaceAddresses.get(2).getAddress().getCanonicalHostName());
             
             System.out.print("Current MAC address : ");
			
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
	     System.out.println(sb.toString());
           
             
             
             
            
            
            
         }
        
        
        
        
    
    }      
        
        private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
                System.out.printf("Display name: %s%n", netint.getDisplayName());
                System.out.printf("Name: %s%n", netint.getName());
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                  System.out.printf("InetAddress: %s%n", inetAddress.getCanonicalHostName());
                }

       
                System.out.printf("Hardware address: %s%n", Arrays.toString(netint.getHardwareAddress()));
                System.out.printf("Teste:", netint.getHardwareAddress());
                System.out.printf("Teste: %s%n", netint.getIndex());

                List<InterfaceAddress> interfaceAddresses = netint.getInterfaceAddresses();
                for (InterfaceAddress addr : interfaceAddresses) {
                  System.out.printf("InterfaceAddress: %s%n", addr.getAddress());
                }
                System.out.printf("%n");
                Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();
                for (NetworkInterface networkInterface : Collections.list(subInterfaces)) {
                  System.out.printf("%nSubInterface%n");
                  displayInterfaceInformation(networkInterface);
                }
                System.out.printf("%n");
         }
        
        
        
   
        
    }
    
  
    

