/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ccdann
 */
public class Client {
    
    private MulticastSocket socket;
 
    
    public Client(String ip, int port) throws IOException {
        
        // important that this is a multicast socket
        socket = new MulticastSocket(port);
        
        // join by ip
        socket.joinGroup(InetAddress.getByName(ip));
        
    }
    
    public void printMessage() throws IOException{
        // make datagram packet to recieve
        byte[] message = new byte[256];
        DatagramPacket packet = new DatagramPacket(message, message.length);
        
        // recieve the packet
        socket.receive(packet);
           long t1 = System.nanoTime();
        
        System.out.println("Received");
        System.out.println("time to receive "+ (System.nanoTime() - t1));

        System.out.println(new String(packet.getData()) +" HOST: "
        + packet.getAddress().getHostAddress()+"CANOLICALHOST: "
        + Arrays.toString(packet.getAddress().getAddress()));
    }
    
    public void close(){
        socket.close();
    }
}