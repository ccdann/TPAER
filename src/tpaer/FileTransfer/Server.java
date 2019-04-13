/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author ccdann
 */
public class Server {

    public Server() throws SocketException, IOException{

    }

    byte b[] = new byte[3072];
    DatagramSocket dsoc = new DatagramSocket(2000);
    FileOutputStream f = new FileOutputStream("/home/ccdann/teste2.txt");

    
    
    public void receivefile() throws IOException {
        while (true) {
            DatagramPacket dp = new DatagramPacket(b, b.length);
            dsoc.receive(dp);
            System.out.println(new String(dp.getData(), 0, dp.getLength()));

        }

    }

}


    
    
    
    

