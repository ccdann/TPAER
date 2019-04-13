/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer.FileTransfer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author ccdann
 */
public class Client {
    
         String ip ;


    public Client(String ip) throws SocketException, IOException {
        this.ip = ip;
    }
    

    byte b[] = new byte[1024];
    FileInputStream f = new FileInputStream("/home/ccdann/Desktop/teste.txt");
    DatagramSocket dsoc = new DatagramSocket(2000);
    int i = 0;

    public void tranferfile() throws IOException {

        while (f.available() != 0) {
            b[i] = (byte) f.read();
            i++;
        }

        f.close();

        dsoc.send(new DatagramPacket(b, i, InetAddress.getByName(ip), 1000));

    }

}
