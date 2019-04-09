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
        private String localnode;
        ArrayList<String> neighbors = new ArrayList<>(10);
        Node node;
        List<RoutingTable> rt;

        
            
    /**
     *
     * @param ip
     * @param port
     * @param localnode
     * @param neighbors
     * @param rt
     * @param peer
     * @throws SocketException
     * @throws UnknownHostException
     * @throws IOException
     */
    public PeerDetection(String ip, int port, String localnode, ArrayList<String> neighbors, List<RoutingTable> rt) throws SocketException, UnknownHostException, IOException{                
                socket = new MulticastSocket(port);
                socket.joinGroup(InetAddress.getByName(ip));
                this.localnode = localnode;  
                this.neighbors = neighbors;
                this.rt = rt;
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
                                
                                
                                Gson gson = new Gson();
                                PDU pdu = gson.fromJson(msg,PDU.class);
                                   
                                    //para nao adicionar nele mesmo
                                if(!localnode.contains(pdu.getIdnode())){
                                    System.out.println("MSG " + msg);
                                    //Se nao contiver nos vizinhos insere o pduid
                                    if(!neighbors.contains(pdu.getIdnode())){
                                        neighbors.add(pdu.getIdnode()); 
                                    }    
                                    
                                    
                                    
                                    
                                    
                                    
                                 node = new Node();
                                 node.setNode(pdu.getIdnode(), "");
                                 node.setNextHop(pdu.getIdnode(), "");
                                 node.setDist(1);
                                 node.setCost(1);
                                    
                                }
                                
                                
                                
                                
                                //Add node
                                
                                if(!neighbors.isEmpty()){
                                    //COmparacao e adiciona na routing table !! 
                                    if(rt.isEmpty()){
                                    rt.add(new RoutingTable(node,1)); 
                                }
                                }
                                
                                for(int i=0; i<rt.size();i++){
                                if(!rt.get(i).node.getDstid().contains(node.getDstid())){
                                    System.out.println("ADD repetido");
                                    System.out.println("RT "+ rt.get(i).node.getDstid());
                                    System.out.println("Node "+ node.getDstid());
      
                                    rt.add(new RoutingTable(node,1)); 
                                }}
                             
                                
                                if(!neighbors.isEmpty()){
                                    //COmparacao e adiciona na routing table !! 
                                    //rt.addNode(node);
                                }
                               
                                
                                
                      

			} catch (IOException e) {
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
        
        
        
        

                            
}
