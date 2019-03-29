/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author ccdann
 */
public class PeerDetectionListener {
    
    //HOP1
    
    
	private static class Pair {
		final String name;
		final String number;
                
		public Pair(String name, String peerStubAddress) {
			this.name = name;
			this.number = peerStubAddress;
		}
	}
        
        
        
	private final static HashMap constants = new HashMap();
        	private static HashMap constants1 = new HashMap();

 
	/* Rest of your class that needs to know the consts */
            // add peer with given name and ip address 
             public void addPeer( String nodename, String nodeAddress, HashMap peerList){
              // constants.put(name, new Pair(name, peerStubAddress));
              constants.put(nodename, nodeAddress);
              
              
               constants1 = peerList;
             }
                
             
           public void showPeers(){        
            System.out.println(Arrays.asList(constants)); 
            System.out.println(constants1);// method 1" " + value);        
             }
           
           /*
           
           public HashMap verifyPeer(HashMap peerList){
               if(peerList.containsValue(constants)){
              
                   peerList.remove(constants.)
               }
           
            return 
           }
            */

   // remove peer with given name
  // public void removePeer( String name );

}
    
