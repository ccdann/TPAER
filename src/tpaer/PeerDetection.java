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
    
        HashMap peershop1 = new HashMap();
        HashMap peershop1temp = new HashMap();
	private BufferedReader inFromUser;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
        private final MulticastSocket socket;
        List<Node> peer = new ArrayList<Node>();
        private int ttl = 0;
        private boolean alone = true;
        private String namelocal = InetAddress.getLocalHost().getHostName();
        
            
    /**
     *
     * @param ip
     * @param port
     * @param peer
     * @throws SocketException
     * @throws UnknownHostException
     * @throws IOException
     */
    public PeerDetection(String ip, int port, List<Node> peer) throws SocketException, UnknownHostException, IOException{                
                socket = new MulticastSocket(port);
                socket.joinGroup(InetAddress.getByName(ip));
                this.peer = peer;

	}


	private void shutdown(){
		clientSocket.close();
	}

        @Override
	public void run() {
		System.out.println("PeerDiscovery started....");
                
                ScheduledExecutorService executorverifyAlone = Executors.newScheduledThreadPool(1);
                executorverifyAlone.scheduleAtFixedRate(verifyAlone, 0, 5, TimeUnit.SECONDS);
                
                ScheduledExecutorService executorClear = Executors.newScheduledThreadPool(1);
                executorClear.scheduleAtFixedRate(clearTables, 0, 30, TimeUnit.SECONDS);
                
                //Por qualquer coisa para ver se o peer esta alone
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
                               long receivedTime = System.nanoTime() - t1;
                                                             
                           String msg = new String( packet.getData(), packet.getOffset(), packet.getLength() );
                         //2
                         
                         //System.out.println(msg);
                           
                           String receivednode = msg.substring(Settings.HELLO.length());
                               
                            if (msg.startsWith(Settings.HELLO)){
                                //Se for diferente do nome do localhost
                                if(!namelocal.equals(receivednode)){
                                    alone=false;
                                    peershop1temp.clear();
                                     processHello( 
                                            msg.substring(Settings.HELLO.length()),
                                            packet.getAddress().getHostAddress(),
                                            receivedTime  
                                         );
                                }else{
                                    //alone=true;
                               // System.out.println("Ola");            
                                }    
                            }else{   

                               System.out.println("Hop2");                             
                               //System.out.println(msg);
  
                                msg = msg.substring(1, msg.length()-1);           //remove curly brackets
                                String[] keyValuePairs = msg.split(",");              //split the string to creat key-value pairs
                                Map<String,String> map = new HashMap<>();               

                                for(String pair : keyValuePairs)                        //iterate over the pairs
                                {
                                    String[] entry = pair.split("=");                   //split the pairs to get key and value 
                                    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                                }
                                

                                
                                 if(map.containsKey(InetAddress.getLocalHost().getHostName())){                                
                                map.remove(InetAddress.getLocalHost().getHostName());
                              
                                }
   
                                
                                 //SE NA ROUTING TABLE TIVER CUSTO 1 remove estes peers!!
                                for (int i = 0; i < peer.size(); i++) {
                                         if(map.containsKey(peer.get(i).getDstid())){ 
                                             if(peer.get(i).getDist() == 1){
                                             map.remove(peer.get(i).getDstid());  
                                             
                                             }
                                         }else{     
                                             //map.remove(peer.get(i).getDstId()); 
                                            // System.out.println("N CONTEM");
                                         }
                                      }
                                
                                
                                    // System.out.println(map);
                                  //System.out.println( packet.getAddress().getHostAddress()); 
                                  
                                  for (Map.Entry<String, String> entry : map.entrySet()) {
                                        //System.out.println(entry.getKey()+" : "+entry.getValue());
                                        
                                        String idnexthop = entry.getKey();
                                        String ipnexthop = entry.getValue();

                                        System.out.println(packet.getAddress().getHostName());
                                        
                                        //PARA PREENCHER A TABELA É NECESSARIO SABER QUAL È 
                                        //O IP QUE VEM VISTO QUE NAO SE CONSEGUE SABER O ID
                                        
                                         //System.out.println(idnexthop);
                                        
                                        //EX PARA O N4 : N1 N3 
                                       // peer.add(new Node(packet.getAddress().getHostAddress(), packet.getAddress().getHostName(), idnexthop, ipnexthop,1,100));
                                    
                                  
                                  /* LETS USE TUPLE
                                       
                                       Map<String, Tuple2<Person, Person> peopleByForename = new HashMap<>();

                                        // populate it
                                        peopleByForename.put("Bob", new Tuple2(new Person("Bob Smith",
                                                                               new Person("Bob Jones"));

                                        // read from it
                                        Tuple<Person, Person> bobs = peopleByForename["Bob"];
                                        Person bob1 = bobs.Item1;
                                        Person bob2 = bobs.Item2;
                                       
                                       */
                                  }

                              //  peerListener.addPeer( "teste", "teste",peerMap);
                            }

			} catch (IOException e) {
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
        
        

              // process hello message from peer
              public void processHello( String nextHop, String ipNexthop, Long cost) throws UnknownHostException, IOException{
                      
                  
                  //Atencao ao acrescentar !!
                  
                  //HOP1 Comparacao local e inserir no global
               peershop1temp.put(nextHop,ipNexthop);

             if(peershop1.isEmpty() && peer.size() == 1){
                  peershop1.put(nextHop,ipNexthop);
                  peer.add(new Node(nextHop, ipNexthop, nextHop, ipNexthop,1,cost));    
                  //System.out.println("Adicionou 1" + nextHop); 
             }
             
             if(!peershop1.containsKey(nextHop)){    
                      peershop1.put(nextHop,ipNexthop); 
                      peer.add(new Node(nextHop, ipNexthop, nextHop, ipNexthop,1,cost)); 
                      //System.out.println("Adicionou1 ++" + nextHop);
                      
             } 
             
             //TESTES PARA REMOVER VALORES
             /*
                 
                
             for (int i = 0; i < peer.size(); i++) {  
                 if(peershop1.size() < peer.size()-1)
                     
                    if(peer.get(i).getDstid().contains(nextHop) &&peer.size()>3){  
                        System.out.println("Removeu " + peer.get(i).getDstid()); 
                                        peer.remove(i);   
                                        
                    }
               } 
             
             */
             
             
             
                //Se o que adicionou já estiver na lista 

                  
                    //HOP2
                    
                    
                    final int port = 9999;
                    HashMap peershop1temp = new HashMap();
                    peershop1temp = peershop1;
                    
                    //para cada endereco hop1 envia 
                    String iphop = (String) peershop1.get(nextHop);
                    
                    //hop1 //porta // noanterior
                    Hello sendHello = new Hello(iphop, port, nextHop); 
                    
                    //
                    sendHello.sendhop2(peershop1);
                   

                           }
              
              Runnable clearTables = () -> {
                 peer.clear();
                 peershop1.clear();
                 peer.add(new Node(namelocal, "0", "0", "0",0,0)); 
                };
              
              Runnable verifyAlone = new Runnable() {
                    public void run() {
                        if(alone == true){       
                         System.out.println("Alone");
                         
                         peer.clear();
                         peershop1.clear();
                         
                         peer.add(new Node(namelocal, "0", "0", "0",0,0)); 
                         
                        }else{
                        //System.out.println("Not Alone");
                        
                        }
                        
                        alone=true;
                    }
                };
                            
}
