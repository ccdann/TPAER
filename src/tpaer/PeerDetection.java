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

/**
 *
 * @author ccdann
 */
public class PeerDetection extends Thread {

    

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
	public PeerDetection(String ip, int port) throws SocketException, UnknownHostException, IOException{
		
                
                socket = new MulticastSocket(port);
                socket.joinGroup(InetAddress.getByName(ip));
		
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

                                System.out.println("Received");
                                System.out.println("Time entre peers");
                                System.out.println("time to receive "+ (System.nanoTime() - t1));
                                

                               /* System.out.println(new String(packet.getData()) +" HOST: "
                                + packet.getAddress().getHostAddress()+"CANOLICALHOST: "
                                + Arrays.toString(packet.getAddress().getAddress()));
                                */
                               // String msg = new String(packet.getData());
                                
                                 String msg = new String( packet.getData(), packet.getOffset(), packet.getLength() );

                            System.out.println(msg);

                            // decide if goodbye or hello
                            if (msg.startsWith("Hello")){
                              
                                System.out.println("é um hello!!");
                                
                                
                               // System.out.println(message.substring( HELLO_HEADER.length(), packet.getAddress().getHostAddress());
                              //  processHello( 
                                //  message.substring( HELLO_HEADER.length() ),
                                //  packet.getAddress().getHostAddress()
                              ///  )) );
                            }
			

			} catch (IOException e) {
				/*
				 * Here we need to capture any exceptions thrown by our application
				 */
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
        
        /*
        
        
                        // process hello message from peer
                  public void savePeer( String peerName, 
                     String registryAddress  )
                  {
                     registryAddress += ( "/" + BINDING_NAME );
                     synchronized( peerTTLMap )
                     {

                        // if it is a new peer, call peerAdded event
                        if ( !peerTTLMap.containsKey( peerName ) ) {
                              peerDiscoveryListener.peerAdded( peerName, 
                                 registryAddress);
                        }

                        // add to map or if present, refresh TTL
                        peerTTLMap.put( peerName, new Integer( PEER_TTL ) );

                     } 
                  }*/

   
    
}
