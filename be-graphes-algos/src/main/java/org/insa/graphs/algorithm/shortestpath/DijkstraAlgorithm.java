package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        int size = data.getGraph().getNodes().size();
        Label[] tab = new Label[size];
        /* for (Node node : data.getGraph().getNodes()){
            tab[node.getId()] = new Label(node);
        } */
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        Node father = data.getOrigin();
        int idFather = father.getId();
        tab[idFather] = new Label(father);
        heap.insert(tab[idFather]);
        while(!(heap.isEmpty())){
            for(Arc arc:father.getSuccessors()){
                int id = arc.getDestination().getId();
                if(tab[id]==null){
                    tab[id] = new Label(arc);
                    heap.insert(tab[id]);
                }
                else{
                    float bestCostSoFar = tab[id].getCost();
                    float costFromNewFather = tab[idFather].getCost() + arc.getLength();
                    if(bestCostSoFar>costFromNewFather){
                        tab[id].father = tab[idFather].current;
                        tab[id].setRealizedCost(costFromNewFather);
                    }
                }
            }
            heap.remove(tab[idFather]);
            idFather = heap.findMin().current.getId();
        }




        return solution;
    }

}
