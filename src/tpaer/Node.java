/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author ccdann
 */
 public class Node {
    
  
    private String dstid;
    private String dstip;
    private String idNexthop;
    private String ipNexthop;
    private int dist;
    private long cost;
    private long ttl;
   
    public Node(){
    
    }
    public Node(String dstid, String dstip, String idNexthop, String ipNexthop, int dist, long cost, long ttl){
                this.dstid = dstid;
		this.dstip = dstip;
                this.idNexthop = idNexthop;
                this.ipNexthop = ipNexthop;
		this.dist = dist;
		this.cost = cost;
                this.ttl = ttl;
	}
    
        public boolean isReachable() {
		return this.idNexthop != null;
	}
    

        public String getDstid(){
		return this.dstid;
	}
        
	public String getDstip(){
		return this.dstip;
	}
        
        public void setNode(String dstid, String dstip) {
		this.dstid = dstid;
                this.dstip = dstip;
	}

	public String getIdnexthop(){
		return this.idNexthop;
	}
        
        public String getIpnexthop(){
		return this.ipNexthop;
	}
        
        public void setNextHop(String idNexthop, String ipNexthop) {
		this.idNexthop = idNexthop;
                this.ipNexthop = ipNexthop;
	}
        
        public void setDist(int dist){
		 this.dist = dist;
	}
        
        public void setCost(int cost){
		this.cost = cost;
	}


	public int getDist(){
		return this.dist;
	}
        
        public int getCost(){
		return (int) this.cost;
	}
        
        public long getTTL(){
		return this.ttl;
	}
            
        public int setTTL(){
		return (int) (long) this.ttl;
	}
                       
 }



                
               
            
     
    
