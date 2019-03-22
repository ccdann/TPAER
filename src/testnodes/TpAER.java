/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testnodes;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import testnodes.Client;
import testnodes.Server;


/**
 *
 * @author ccdann
 */
public class TpAER {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SocketException {
        
     

          
        System.out.println("Press 1 Client or Press 2 Server");

        
        
        Scanner scan= new Scanner(System.in);
        
      
        
        
        int num= scan.nextInt();
        
        
        
        if (num==1){ 
            
            
            
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
                
         }
            
        
        
        if (num==2){
            
      
            
            
              System.out.println("Init Server......");
               try {
                final String ip = "ff02::1";
                final int port = 9999;
                Server server = new Server(ip, port);
                server.send();
                server.close();
         } catch (IOException ex) {
        ex.printStackTrace();
         }
        }
        
         if (num==3){   
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
              displayInterfaceInformation(netint);
            }
         }
        
        
        
        
    
    }      
        
        private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
                System.out.printf("Display name: %s%n", netint.getDisplayName());
                System.out.printf("Name: %s%n", netint.getName());
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                  System.out.printf("InetAddress: %s%n", inetAddress);
                }

                System.out.printf("Parent: %s%n", netint.getParent());
                System.out.printf("Up? %s%n", netint.isUp());
                System.out.printf("Loopback? %s%n", netint.isLoopback());
                System.out.printf("PointToPoint? %s%n", netint.isPointToPoint());
                System.out.printf("Supports multicast? %s%n", netint.isVirtual());
                System.out.printf("Virtual? %s%n", netint.isVirtual());
                System.out.printf("Hardware address: %s%n", Arrays.toString(netint.getHardwareAddress()));
                System.out.printf("MTU: %s%n", netint.getMTU());

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
    
  
    

