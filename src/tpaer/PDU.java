/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccdann
 */
 class PDU {
    
    String type;
    String idnode;
    String ipnode;
    ArrayList<String> neighbors;    
    ArrayList<String> neighborsip;  
    //List<RoutingTable> rt = new ArrayList<>();
   
    
    
    public PDU(){
    
    }
    
    public PDU(String type, String idnode, ArrayList<String> neighbors, ArrayList<String> neighborsip){              
        this.type = type;
        this.idnode = idnode;    
        this.neighbors = neighbors;
        this.neighborsip = neighborsip;

    }
    
    public void setType(String type) {
		this.type = type;
    }
    
    public void setIdnode(String id) {
		this.idnode = id;
    }
    
     public void setIpnode(String ip) {
		this.ipnode = ip;
    }
    
    public void setNeighbors(ArrayList<String> neighbors) {
		this.neighbors = neighbors;
    }
    
    public void setNeighborsip(ArrayList<String> neighborsip) {
		this.neighborsip = neighborsip;
    }
    
    public String getType(){
                return this.type;
    }
    
    public String getIdnode() {
		return this.idnode;
    }
    
    public String getIpnode() {
		return this.ipnode;
    }
    
    public ArrayList getNeighbors() {
                return this.neighbors;
    }
    
     public ArrayList getNeighborsip() {
                return this.neighborsip;
    }
     
     public void removeNeighbors(){
        neighbors.clear();
        neighborsip.clear();
     }
     

}
