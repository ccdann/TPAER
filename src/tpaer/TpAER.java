/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;



import com.google.gson.Gson;
import java.io.BufferedReader;
import tpaer.FileTransfer.*;

import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author ccdann
 */
public class TpAER extends Settings {
    
   

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
       
         int num =0;
         String localnode = InetAddress.getLocalHost().getHostName();
         
         NetworkInterface nets1 = NetworkInterface.getByName("eth0");
         List<InterfaceAddress> interfaceAddresses = nets1.getInterfaceAddresses();    
         //System.out.printf("InterfaceAddress: %s%n", interfaceAddresses.get(2).getAddress().getCanonicalHostName());
         String localnodeip = interfaceAddresses.get(2).getAddress().getCanonicalHostName();
         
         //RoutingTable rt = new RoutingTable();
         List<RoutingTable> rt = new ArrayList<>();
         HashMap<String, String> files = new HashMap<>();
        
        
         
         PDU pduhello = new PDU();
         pduhello.setType(HELLO);
         pduhello.setIdnode(localnode);
         pduhello.setIpnode(localnodeip);
         
         ArrayList<String> neighbors = new ArrayList<>(10);
         ArrayList<String> neighborsip = new ArrayList<>(10);
         //neighbors.add("n2");
         //neighbors.add("n3");
         pduhello.setNeighbors(neighbors);
         pduhello.setNeighborsip(neighborsip);
         //pduhello.setRoutingTable((ArrayList<RoutingTable>) rt);
         
        
        
         Gson gson = new Gson();
         //System.out.println(gson.toJson(pduhello));
         
         
         PeerDetection detect   = new PeerDetection(ip, port,localnode, neighbors, neighborsip, rt);
         detect.start();
         ServerQueryFiles serverFT = new ServerQueryFiles(rt, files);
         serverFT.start();
        
        //O hello tem de ser criado antes de ser enviado
        
        Hello sendHello = new Hello(ip, port);    
        
         Runnable helloRunnable = () -> {
             try {
                 sendHello.send(gson.toJson(pduhello));
             } catch (IOException ex) {
                 Logger.getLogger(TpAER.class.getName()).log(Level.SEVERE, null, ex);
             }
             // sendHello.close();
         };
         
         ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
         executor.scheduleAtFixedRate(helloRunnable, 0, secSendmsg, TimeUnit.SECONDS);
         
                    Runnable emptyneighRunnable = new Runnable() {
               public void run() {
                   pduhello.removeNeighbors();
               }
           };

           ScheduledExecutorService executorneigh = Executors.newScheduledThreadPool(1);
           executorneigh.scheduleAtFixedRate(emptyneighRunnable, 0, 15, TimeUnit.SECONDS);
         

        
        boolean stop = false;
        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ));

        while( !stop )
        {
          System.out.println("---" +"MENU "+"Node:"+localnode+"----");
          System.out.println( "1-Show Table");    
          System.out.println( "2-Query File");
          System.out.println( "3-Show files on Server"); 
          System.out.println("----------" + "----" + "-----------");
 
          //System.out.println( "3-SendFile");  
        
        //  System.out.println( "enter \"0\" to exit");
          String s = br.readLine();

          if( s.equals( "0" ) )
          {
            System.out.print( "Closing down..." );
            detect.stop();
            System.out.println( " done" );
            exit(0);
          }
          
            if( s.equals( "1" ) )
          {
               System.out.println("----------" + localnode + "-----------");
              for(RoutingTable rtr : rt) {
                        System.out.println("Node:" + rtr.node.getDstid() +" Dist:" + rtr.dist + " TTL:" + (30 - (System.nanoTime()- rtr.node.getTTL()) / 1000000000));
             }
            
            
          }
          
          if( s.equals( "2" ) )
          {
              ClientQueryFiles ct = new ClientQueryFiles();
              
              
              BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in)); 
              System.out.println("Type query");  
              String str="";           
              str=br2.readLine(); 
               
               
               for(int i=1; i<rt.size(); i++){
                   ct.receivefile(rt.get(i).node.getDstip(), str);
               // System.out.println("IP " + rt.get(i).node.getDstip());
               }
           
                 //Client clientFT = new Client("127.0.0.1");
            //clientFT.receivefile();

                
           }
           

          if( s.equals( "3" ) )
          {    
           for (Map.Entry<String, String> entry : files.entrySet()) {
                 System.out.println(entry.getKey()+" : "+entry.getValue());
            } 
          }
          
           if( s.equals( "4" ) )
          {
              /*
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
              */
            
          }
          
        
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
    
  
    

