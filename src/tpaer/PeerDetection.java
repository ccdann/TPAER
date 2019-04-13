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
import java.util.Set;
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

    HashMap<String, Node> nodes = new HashMap<String, Node>();
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
    
    public PeerDetection(String ip, int port, String localnode, ArrayList<String> neighbors, List<RoutingTable> rt) throws SocketException, UnknownHostException, IOException {
        socket = new MulticastSocket(port);
        socket.joinGroup(InetAddress.getByName(ip));
        this.localnode = localnode;
        this.neighbors = neighbors;
        this.rt = rt;
    }

    private void shutdown() {
        clientSocket.close();
    }

    @Override
    public void run() {
        System.out.println("PeerDiscovery started....");

        while (true) {
            try {

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
                if (!localnode.contains(pdu.getIdnode())) {

                    System.out.println("MSG " + msg);

                    /*
                                <!---Atenção !! Verificar os neighbors, porque quando estao 4 ou mais vizinhos de hop 1 há problema !!
                                    
                     */
                    //Se nao contiver nos vizinhos insere o pduid
                    if (!neighbors.contains(pdu.getIdnode())) {
                        neighbors.add(pdu.getIdnode());
                        //30 Seconds ttl
                        
                    }

                    //Inserir primeiro os de distancia 1    
                    //Verifica na tabela 
                    //{"type":"Hello","idnode":"n2","neighbors":["n1","n4"]
                    /*
                                <!--- Em primeiro adiciona o "idnode" com dist(hop) = 1
                                <!--- Todos os restastes (Ex:neigbors têm de ser verificados!! -->
                                <!--- É preciso verificar primeiro se o nó está na RT! 
                     */
                    
                    
                    
                    for (int i = 0; i < rt.size(); i++) {

                    }
                     
                                
                                
                                    
                                    
                    if (!nodes.containsKey(pdu.getIdnode())) {
                        nodes.put(pdu.getIdnode(), new Node(pdu.getIdnode(), "", pdu.getIdnode(), "", 1, 1));
                        rt.add(new RoutingTable(nodes.get(pdu.getIdnode()), 1));
                    }

                    //N1
                    //{"type":"Hello","idnode":"n2","neighbors":["n1","n4"]}
                    //Procura em todos os neighbors e adiciona em falta
                    for (Object neighbor : pdu.getNeighbors()) {
                        if (!nodes.containsKey(neighbor)) {
                            //Para nao adicionar com distancia 2 o proprio no
                            if (!localnode.equals(neighbor)) {
                                nodes.put(neighbor.toString(), new Node(neighbor.toString(), "", pdu.getIdnode(), "", 2, 1));
                                //se na tabela n contiver o X insere
                                boolean exists = verifyTable(nodes.get(neighbor.toString()));

                                System.out.println(exists);

                                //if(!exists){
                                //rt.add(new RoutingTable(nodes.get(neighbor.toString()),2)); 
                                //}else{
                                //System.out.println("Existe na tabela");
                                //}
                            }
                        }
                    }

                    //Add node
                    //Inserir hop2, se na tabela contiver insere!!
                    Set<Map.Entry<String, Node>> nodeset = nodes.entrySet();
                    for (int i = 0; i < rt.size(); i++) {
                        for (Map.Entry<String, Node> nodesfor : nodeset) {

                            if (!rt.get(i).node.getDstid().contains(nodesfor.getKey())) {

                                //System.out.println("Node" +rt.get(i).node.getDstid());
                                //System.out.println("Nodes key" +nodesfor.getKey());
                                //rt.add(new RoutingTable(nodesfor.getValue(),2)); 
                            }

                            //System.out.print(nodesfor.getKey() + ": ");
                        }

                    }

                }

                //System.out.println("Neigbors hop1" + neighbors);
                // System.out.println("Neigbors hop2" + pdu.getNeighbors());
                /*
                                for(int i=0; i<rt.size();i++){
                                if(!rt.get(i).node.getDstid().contains(nodes.getDstid())){
                                    System.out.println("ADD repetido");
                                    System.out.println("RT "+ rt.get(i).node.getDstid());
                                    System.out.println("Node "+ node.getDstid());
      
                                    rt.add(new RoutingTable(node,1)); 
                                }}

                 */
            } catch (IOException e) {
                System.out.println("Exception Thrown: " + e.getLocalizedMessage());
            }
        }
    }

    public Boolean verifyTable(Node node) {
        Boolean value = false;
        for (int i = 0; i < rt.size(); i++) {
            if (rt.get(i).node.getDstid().equals(node.getDstid())) {
                value = true;
            }
        }
        return value;

    }

}
