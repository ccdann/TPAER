/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpaer;

import java.util.List;

/**
 *
 * @author ccdann
 */
 class RoutingTable {
    
    Node node;
    int dist;
    
    
    public RoutingTable(){
    }
    
    public RoutingTable(Node node, int dist){
    this.node = node;
    this.dist = dist;
    }
      
    void addNode(Node node){
    this.node = node;
    }
    
    public boolean verifyNode(Node node){
        return this.node != null;
    }
    
    public Node getNode(Node node){
        return this.node;
    }
    
    void printTable(){
        
        System.out.println("Dest: " + node.getDstid() + " Dist: " + dist);
       
    
    }
    
    void setNeighbor(int dist){
    
    }
    
    void addNeighbor(int dist){
    
    }
    
    public RoutingTable findUsingEnhancedForLoop(
        String name, List<RoutingTable> routingt) {

          for (RoutingTable rt : routingt) {
              if (rt.getNode(node).getDstid().equals(name)) {
                  return rt;
              }
          }
          return null;
      }
    
    
    
    //add neighbor
    //set netigh
    
}


