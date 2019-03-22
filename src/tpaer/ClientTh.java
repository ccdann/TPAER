package tpaer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ccdann
 */
public class ClientTh implements Runnable{
    
	

	private BufferedReader inFromUser;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;

	/*
	 * Our byte arrays that we'll use to read in and send out to our UDP server
	 */
	private byte[] outData;
    private byte[] inData;

    /*
     * Our Client constructor which instantiates our clientSocket
     * and get's our IPAddress
     */
	public ClientTh() throws SocketException, UnknownHostException{
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName("ff02::1");
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
	}

	private void shutdown(){
		clientSocket.close();
	}

	public void run() {
		System.out.println("Client Started, Listening for Input:");
		/*
		 * Start a while loop that will run until we kill the program, this will continuously
		 * poll for user input and send it to the server.
		 */
		while(true){
			try {
				inData = new byte[1024];
				outData = new byte[1024];
				/*
				 * First we read in the users input from the console.
				 */
				System.out.print("> ");
				String sentence = inFromUser.readLine();
				outData = sentence.getBytes();

				/*
				 * Next we create a datagram packet which will allow us send our message back to our datagram server
				 */
				DatagramPacket out = new DatagramPacket(outData, outData.length, IPAddress, 10000);
				clientSocket.send(out);

				/*
				 * Once we've sent our message we create a second datagram packet which will
				 * let us receive a response.
				 */
				DatagramPacket in = new DatagramPacket(inData, inData.length);
				clientSocket.receive(in);

				/*
				 * Finally we log the response from the server using log4j
				 */
				String modifiedSentence = new String(in.getData());
				System.out.println("Server >" + modifiedSentence);

			} catch (IOException e) {
				/*
				 * Here we need to capture any exceptions thrown by our application
				 */
				System.out.println("Exception Thrown: " + e.getLocalizedMessage());
			}
		}
	}
}