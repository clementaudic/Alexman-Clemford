package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label{

    double totalCost;
    double distanceToDest;

    public LabelStar(Node node){
        super(node);
        this.totalCost = 0;
    }
    
    public LabelStar(Arc arc,double cost,Point destinationPoint,AbstractInputData data){
        super(arc,cost);
        this.distanceToDest = Point.distance(arc.getDestination().getPoint(),destinationPoint);
        if(data.getMode() == Mode.TIME) this.distanceToDest /= data.getGraph().getGraphInformation().getMaximumSpeed();
        this.totalCost = cost + this.distanceToDest;
    }

    public double getTotalCost(){
        return this.totalCost;
    }

    @Override
    public int compareTo(Label label){
        double a = this.totalCost-((LabelStar)label).totalCost;
        return (a>0 ? 1 : (a<0) ? -1 : super.compareTo(label));
    }

    @Override
    public void setRealizedCost(double cost){
        super.setRealizedCost(cost);
        this.totalCost = cost + this.distanceToDest;
    }
}
