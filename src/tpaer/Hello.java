/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 *
 * @author ccdann
 */
public class Hello {
    
    private DatagramSocket serverSocket;
    
    private String ip;
    
    private int port;
    
   
    
    public Hello(String ip, int port) throws SocketException, IOException{
        this.ip = ip;
        this.port = port;
        // socket used to send
        serverSocket = new DatagramSocket();
    }
    
    public void send() throws IOException{
       
        // make datagram packet
        byte[] message = ("Hello").getBytes();
        
        
        DatagramPacket packet = new DatagramPacket(message, message.length, 
            InetAddress.getByName(ip), port);
        // send packet
        serverSocket.send(packet);
    }
    
    public void close(){
        serverSocket.close();
    }

}