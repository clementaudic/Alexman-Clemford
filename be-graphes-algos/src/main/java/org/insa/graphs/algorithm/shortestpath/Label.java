package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
    public Node current;
    public boolean visited;
    protected double realizedCost;
    public Arc father;

    public Label(Node node){
        this.current = node;
        this.father = null;
        this.realizedCost = 0;
        this.visited = false;
    }

    public Label(Arc arc,double cost){
        this.current = arc.getDestination();
        this.father = arc;
        this.realizedCost = cost;
        this.visited = false;
    }

    public double getCost(){
        return realizedCost;
    }

    public void setRealizedCost(double cost){
        this.realizedCost = cost;
    }

    public int compareTo(Label label){
        double a = this.realizedCost-label.realizedCost;
        return (a>0 ? 1 : (a<0) ? -1 : 0);
    }
}
