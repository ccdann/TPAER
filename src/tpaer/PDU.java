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
    ArrayList<String> neighbors;    
    
    public PDU(){
    
    }
    
    public PDU(String type, String idnode, ArrayList<String> neighbors){              
        this.type = type;
        this.idnode = idnode;    
        this.neighbors = neighbors;
    }
    
    public void setType(String type) {
		this.type = type;
    }
    
    public void setIdnode(String id) {
		this.idnode = id;
    }
    
    public void setNeighbors(ArrayList<String> neighbors) {
		this.neighbors = neighbors;
    }
    
    public String getType(){
                return this.type;
    }
    
    public String getIdnode() {
		return this.idnode;
	}
    
    public ArrayList getNeighbors() {
                return this.neighbors;
    }

    


    
    
}
