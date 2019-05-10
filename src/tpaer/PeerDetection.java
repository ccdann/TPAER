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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static tpaer.Settings.HELLO;
import static tpaer.Settings.ip;
import static tpaer.Settings.port;
import static tpaer.Settings.secSendmsg;

/**
 *
 * @author ccdann
 */
public class PeerDetection extends Thread{

    private BufferedReader inFromUser;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private final MulticastSocket socket;
    private String localnode;
    ArrayList<String> neighbors = new ArrayList<>(10);
    ArrayList<String> neighborsip = new ArrayList<>(10);
    long t1 = System.nanoTime();
    long t2 = System.nanoTime();
    int cont = 0;
    long elapsedTimeInSeconds = (t2 - t1) / 1000000000;

    HashMap<String, Node> nodes = new HashMap<String, Node>();
            
    List<RoutingTable> rt =  new ArrayList<RoutingTable>();


   
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
    
    public PeerDetection(String ip, int port, String localnode, ArrayList<String> neighbors, ArrayList<String> neighborsip, List<RoutingTable> rt) throws SocketException, UnknownHostException, IOException {
        socket = new MulticastSocket(port);
        socket.joinGroup(InetAddress.getByName(ip));
        this.localnode = localnode;
        this.neighbors = neighbors;
        this.neighborsip = neighborsip;
        this.rt = rt;
        
        ScheduledExecutorService refreshcont = Executors.newScheduledThreadPool(1);
        refreshcont.scheduleAtFixedRate(emptyCount, 0, 60, TimeUnit.SECONDS);

        
        
        
       
    }

    private void shutdown() {
        clientSocket.close();
    }

    @Override
    public void run() {
        System.out.println("PeerDiscovery started....");
        nodes.put(localnode, new Node(localnode, "", localnode, "",0,1,System.nanoTime()));
        while (true) {   
            try {
                //System.out.println(cont);
                byte[] message = new byte[256];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                // recieve the packet
                socket.receive(packet);

                //long t1 = System.nanoTime();
                //long receivedTime = System.nanoTime() - t1;
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

                Gson gson = new Gson();
                PDU pdu = gson.fromJson(msg, PDU.class);

                /*<!---
                    Ver se consigo adicionar e remover vizinhos (Keep Alive) ou TTL
                    Temporizador ! O tempo é atualizado a cada mensagem recebida (Se passar o tempo máximo definido retira-se)
                    Se receber do n1 mensagem atualiza o TTL !
                           
                 */
                //Ignorar as mensagens do mesmo no
                
            
                    
                if(pdu.getType().equals(HELLO)){    
                if (!localnode.contains(pdu.getIdnode())) {

                    //System.out.println("MSG " + msg);
                    //System.out.println("Neigh " + neighbors);
                    /*
                    <!---Atenção !! Verificar os neighbors, porque quando estao 4 ou mais vizinhos de hop 1 há problema !!
                                    
                     */
                    //Se nao contiver nos vizinhos insere o pduid
                    if (!neighbors.contains(pdu.getIdnode())) {
                        neighbors.add(pdu.getIdnode());
                        neighborsip.add(pdu.getIpnode());
                        //Algo para refresh dos neighbors
                    }

                    //{"type":"Hello","idnode":"n2","neighbors":["n1","n4"]
                    /*
                                <!--- Em primeiro adiciona o "idnode" com dist(hop) = 1
                                <!--- Todos os restastes (Ex:neigbors têm de ser verificados!! -->
                                <!--- É preciso verificar primeiro se o nó está na RT! 
                    */
                    
                   nodes.put(pdu.getIdnode(), new Node(pdu.getIdnode(), pdu.getIpnode(), pdu.getIdnode(), pdu.getIpnode(), 1, 1,System.nanoTime()));                
                   
                   
                  
                   if(rt.isEmpty()){   
                      rt.add(new RoutingTable(nodes.get(localnode), 0));
                      rt.add(new RoutingTable(nodes.get(pdu.getIdnode()), 1));
                   }
   
                 // HOP1
                 //Procura e insere

                 
                    RoutingTable exists = rt.stream()
                   .filter(node -> pdu.getIdnode().equals(node.node.getDstid()))
                   .findAny()
                   .orElse(null);

                    if(exists == null){
                        //add dist 1
                        rt.add(new RoutingTable(nodes.get(pdu.getIdnode()), 1));
                        
                        
                    }else{
                        //update ttl
                         for (int i = 0; i < rt.size(); i++) {
                            if(rt.get(i).node.getDstid().equals(pdu.getIdnode()))
                           rt.set(i, new RoutingTable(nodes.get(pdu.getIdnode()), 1));
                         }        
                    }
                 
                    
                    // 20 segundos percorrer dist = 1
                    if(cont >= 4){
          
                    //DIST = 2
                     
                    //N1
                    //{"type":"Hello","idnode":"n2","neighbors":["n1","n4"]}
                    //Procura em todos os neighbors e adiciona em falta
                    for (Object neighbor : pdu.getNeighbors()) {
                            
                         if(!localnode.contains(neighbor.toString())){         
                             //getneighborip
                             int index = pdu.getNeighbors().indexOf(neighbor);
                             //System.out.println(index);
                             //System.out.println(pdu.getNeighborsip().get(index).toString());
                             
                             
                             
                             nodes.put(neighbor.toString(), new Node(neighbor.toString(), pdu.getNeighborsip().get(index).toString(), pdu.getIdnode(), pdu.getIpnode(), 2, 1,System.nanoTime()));
                             //updateTableOptimized(neighbor.toString(),2, pdu);  
                         }
                         
                         if (nodes.containsKey(neighbor.toString())) {   
                             updateTableOptimized(neighbor.toString(),2,pdu);   
    
                        }

                        }
                    
                       
                    }
                    //// 40 segundos percorrer dist = 2
                    if(cont >= 8){
                        
                     PDU pduget = new PDU();
                       pduget.setType("get");   
                      if(verifyDist2()!= null){
                       //System.out.println("HOP3" + verifyDist2().node.getDstid());
                       pduget.setIdnode(verifyDist2().node.getDstid());
                       pduget.setIpnode(verifyDist2().node.getDstip());
                      
                       //pduget.setRoutingTable((List<RoutingTable>) rt.iterator());
                       
                       //Volta a enviar com o hop2 para na tabela adicionar o hop 3
                       Hello sendGet = new Hello(ip, port); 
                       try {
                        sendGet.send(gson.toJson(pduget));
                        } catch (IOException ex) {
                            Logger.getLogger(TpAER.class.getName()).log(Level.SEVERE, null, ex);
                        }
                         
                         //System.out.println("GET" +  pdu.getRoutingTable(nodes.get(neighbor.toString())));
  
                         
                        }
                    }
                    
                    
                    
                     

                    
                    
                    //DIST = 3     
              
                    //node contains hop =2 return rt;
                    
                 cont ++; 
                }
            }else if(pdu.getType().equals("get")){
                    if(cont >= 8){
                     //System.out.println("GET" +  msg);
                     //System.out.println("GET pdu" +  pdu.getIdnode());
                     if(pdu.getIdnode()!=null){
                     nodes.put(pdu.getIdnode(), new Node(pdu.getIdnode(), pdu.getIpnode(), pdu.getIdnode(), pdu.getIpnode(), 3, 1,System.nanoTime()));
                     updateTableOptimized(pdu.getIdnode(),3,pdu);
                     }
                    }      
            }
                        // Verifica e remove da RT TTL
                         
                        for (int i = 1; i < rt.size(); i++) {
                                long ttl = (System.nanoTime() - rt.get(i).node.getTTL() ) / 1000000000;
                                // System.out.println("TTL " + ttl);
                                if(ttl > Settings.TTL)  {
                                rt.remove(i);
                                }        
                            }

            } catch (IOException e) {
                System.out.println("Exception Thrown: " + e.getLocalizedMessage());
            }
            
        }
        
        
    }
    
    
     Runnable emptyCount = new Runnable() {
               public void run() {
                   cont =0; 
               }
           };
     
     
     public static int getIndexOf(ArrayList[] strings, String item) {
            for (int i = 0; i < strings.length; i++) {
                if (item.equals(strings[i])) return i;
            }
            return -1;
        
    }

           

    public RoutingTable verifyTable(String id) {
              RoutingTable existshop2 = rt.stream()
                                                .filter(node -> id.equals(node.node.getDstid()))
                                                .findAny()
                                                .orElse(null);
                                                 //System.out.println(existshop1);   
        return existshop2;

    }
    
     public RoutingTable verifyDist2() {
        for (int i=0; i<rt.size(); i++){
            if(rt.get(i).node.getDist()== 2){
            return rt.get(i);
            }
        
        }
        return null;
    }
 
    
    public void updateTableOptimized(String name, int dist, PDU pdu){
        
        if (!localnode.equals(name)) {
            
            RoutingTable exists = rt.stream()
                .filter(node -> name.equals(node.node.getDstid()))
                .findAny()
                .orElse(null);

                 if(exists == null){
                     //System.out.println("ADD 1 " + nodes.get(name).getDstid() +" D " + nodes.get(name).getDist());    
                     rt.add(new RoutingTable(nodes.get(name), dist)); 
                 }else{
                    if(exists.node.getDist() == dist){  
                                          for (int i = 0; i < rt.size(); i++) {
                                                                   if(rt.get(i).node.getDstid().equals(exists.node.getDstid())){           
                                                                   //   System.out.println("Update " + nodes.get(name).getDstid());  
                                                                      rt.set(i, new RoutingTable(nodes.get(name), dist));   
                                                                   }
                                           } 
                                          
                                          }             
                    }       
        }

                 
    }
      
          
 }



/*

       for (int j = 0; j < rt.size(); j++) { 
                              if(rt.get(j).node.getDist() == dist){
                                  //System.out.println("UPDATE 2" + rt.get(j).node.getDstid());
                                      RoutingTable existshop1 = rt.stream()
                                                              .filter(node -> name.equals(node.node.getDstid()))
                                                              .findAny()
                                                              .orElse(null);
                                                               System.out.println("EXIST " + existshop1.node.getDstid());              

                                                               if(existshop1.node.getDist() == dist){
                                                                 for (int i = 0; i < rt.size(); i++) {
                                                                      if(rt.get(i).node.getDstid().equals(existshop1.node.getDstid()))
                                                                     System.out.println("Update " + nodes.get(name).getDstid());  
                                                                      rt.set(i, new RoutingTable(nodes.get(name), dist));                                                      
                                                                    } 
                                                               }
                                               }

                       }






*/