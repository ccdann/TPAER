/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

/**
 *
 * @author ccdann
 */
public interface PeerDetectionListener {
   
   // add peer with given name and ip address 
   public void addPeer( String name, String peerStubAddress );

   // remove peer with given name
   public void removePeer( String name );

}
    
