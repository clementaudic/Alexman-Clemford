package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
    public Node current;
    public boolean visited;
    private float realizedCost;
    public Node father;

    public Label(Node node){
        this.current = node;
        this.father = null;
        this.realizedCost = 0;
        this.visited = false;
    }

    public Label(Arc arc){
        this.current = arc.getDestination();
        this.father = arc.getOrigin();
        this.realizedCost = arc.getLength();
        this.visited = false;
    }

    public float getCost(){
        return realizedCost;
    }

    public void setRealizedCost(float cost){
        this.realizedCost = cost;
    }

    public int compareTo(Label label){
        float a = this.realizedCost-label.realizedCost;
        return (a>0 ? 1 : (a<0) ? -1 : 0);
    }
}
