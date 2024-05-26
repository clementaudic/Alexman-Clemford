package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    public Label createLabel(Node node){
        return new LabelStar(node);
    }

    @Override
    public Label createLabel(Arc arc, double cost){
        return new LabelStar(arc, cost, getInputData().getDestination().getPoint(),this.data);
    }

}
