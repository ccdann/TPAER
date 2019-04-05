/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ccdann
 */
public class PeerDetection extends Thread {

	private BufferedReader inFromUser;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
        private final MulticastSocket socket;

        
            
    /**
     *
     * @param ip
     * @param port
     * @param peer
     * @throws SocketException
     * @throws UnknownHostException
     * @throws IOException
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
                
		while(true){
			try {	
                                		
                               byte[] message = new byte[256];
                               DatagramPacket packet = new DatagramPacket(message, message.length);
                                // recieve the packet
                               socket.receive(packet);
                                
                                //long t1 = System.nanoTime();
                                //long receivedTime = System.nanoTime() - t1;
                                                             
                                String msg = new String( packet.getData(), packet.getOffset(), packet.getLength() );
                                System.out.println("MSG "+ msg);
                                
                                Gson gson = new Gson();
                                PDU pdu = gson.fromJson(msg,PDU.class);
                                   
                                
                                
                                System.out.println(pdu.getIdnode());
                                System.out.println(pdu.getNeighbors());
                                
                                
                                
                                //Adiciona no No
                                
                                
                                //COmparacao e adiciona na routing table !!
                                   
          
                            

			} catch (IOException e) {
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
        
        

                            
}
