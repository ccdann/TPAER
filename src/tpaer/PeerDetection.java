/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author ccdann
 */
public class PeerDetection extends Thread {
    
       private HashMap peerMap;
       
          private PeerDetectionListener peerListener;
          

    

	private BufferedReader inFromUser;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
        private MulticastSocket socket;

	/*
	 * Our byte arrays that we'll use to read in and send out to our UDP server
	 */
	
    private byte[] outData;
    private byte[] inData;

    /*
     * Our Client constructor which instantiates our clientSocket
     * and get's our IPAddress
     */
	public PeerDetection(String ip, int port, PeerDetectionListener peer) throws SocketException, UnknownHostException, IOException{
		super( "PeerDetection" );
      
      // set peerDiscoveryListener
                               

                socket = new MulticastSocket(port);
                socket.joinGroup(InetAddress.getByName(ip));
                peerMap = new HashMap();
                peerListener = peer;
                
                
		
	}


	private void shutdown(){
		clientSocket.close();
	}

        @Override
	public void run() {
		System.out.println("PeerDiscovery started....");
		/*
		 * Start a while loop that will run until we kill the program, this will continuously
		 * poll for user input and send it to the server.
		 */
		while(true){
			try {			
				byte[] message = new byte[256];
                                DatagramPacket packet = new DatagramPacket(message, message.length);

                                // recieve the packet
                                socket.receive(packet);
                                long t1 = System.nanoTime();

                             //   System.out.println("Received");
                               // System.out.println("Time entre peers");
                               // System.out.println("time to receive "+ (System.nanoTime() - t1));
                                

                               /* System.out.println(new String(packet.getData()) +" HOST: "
                                + packet.getAddress().getHostAddress()+"CANOLICALHOST: "
                                + Arrays.toString(packet.getAddress().getAddress()));
                                */
                               // String msg = new String(packet.getData());
                                
                           String msg = new String( packet.getData(), packet.getOffset(), packet.getLength() );

                           // System.out.println(msg);
                            String msg1 = msg.substring(Settings.HELLO.length());
                               //System.out.println("MSG1:"+ msg1);
                            
                               //HOP1
                               
                            if (msg.startsWith(Settings.HELLO)){
                             
                                
                                if(!InetAddress.getLocalHost().getHostName().equals(msg1)){
                                     processHello( 
                                            msg.substring( Settings.HELLO.length() ),
                                            packet.getAddress().getHostAddress()
                                         );
                                }
                               // System.out.println("é um hello!!");
                               
                               //HELLO FROM N1
                               //peerMap.put("1", msg.substring( Settings.HELLO.length()));
                               
                               
                                
                               // System.out.println(message.substring( HELLO_HEADER.length(), packet.getAddress().getHostAddress());
                              //  processHello( 
                                //  message.substring( HELLO_HEADER.length() ),
                                //  packet.getAddress().getHostAddress()
                              ///  )) );
                              
                              //HOP2
                            }else{ 
                              
                                System.out.println("Hop2");                             
                                System.out.println(msg);

                                
                                
                              //  peerListener.addPeer( "teste", "teste",peerMap);
                                
                            
                            }
			

			} catch (IOException e) {
				/*
				 * Here we need to capture any exceptions thrown by our application
				 */
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
        
        

                                // process hello message from peer
                           public void processHello( String peerOrg, 
                              String registryAddress  ) throws UnknownHostException, IOException
                           {
                               
                               
                    
                                    //comparar com o no presente
                                 // if it is a new peer, call peerAdded event
                                 if (!peerMap.containsValue(peerOrg)) {
                                    
                                     peerMap.put(peerOrg,registryAddress);
                                    
                                      //System.out.println("add to hasmap" + peerOrg);
                                       peerListener.addPeer( peerOrg, 
                                          registryAddress,peerMap);
                                       
                                       
                                       
                                 }
                                 
                                 processhop2(peerOrg, registryAddress,peerMap);

                                 
                                 
                                 //Lista de  hops seguintes
                                  

                                 
                                 
                              //   peerMap.put(peerName, registryAddress);


                                

                              
                           }
                           
                           public void processhop2(String peerOrg, String registryAddress, HashMap peersList) throws IOException{
                           
                                Hello sendHello = new Hello(registryAddress, 9999, peerOrg);
                             
                                
                                //if(!peerListener.verifyPeer(peerList)){
                                
                                
                                //}
                                sendHello.sendhop2(peersList);
                                sendHello.close();
                           
                           
                           
                           }

   
    
}
