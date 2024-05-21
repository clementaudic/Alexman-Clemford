package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.text.DefaultStyledDocument;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Arc;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public Label createLabel(Node node){
        return new Label(node);
    }

    public Label createLabel(Arc arc, double cost){
        return new Label(arc, cost);
    }



    @Override
    protected ShortestPathSolution doRun() {


        final ShortestPathData data = getInputData();
        int size = data.getGraph().getNodes().size();
        Label[] tab = new Label[size];
        /* for (Node node : data.getGraph().getNodes()){
            tab[node.getId()] = new Label(node);
        } */
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        Node dest = data.getDestination();
        int idFather = data.getOrigin().getId();
        tab[idFather] = this.createLabel(data.getOrigin());
        heap.insert(tab[idFather]);
        notifyOriginProcessed(tab[idFather].current);
        while(!(heap.isEmpty() || idFather == dest.getId())){
            idFather = heap.deleteMin().current.getId();
            notifyNodeMarked(tab[idFather].current);
            arcLoop: for(Arc arc:tab[idFather].current.getSuccessors()){
                if (!data.isAllowed(arc)) continue arcLoop;
                int id = arc.getDestination().getId();
                if(tab[id]==null){
                    tab[id] = this.createLabel(arc,this.data.getCost(arc) + tab[idFather].getCost());
                    heap.insert(tab[id]);
                    notifyNodeReached(tab[id].current);
                }
                else{
                    double bestCostSoFar = tab[id].getCost();
                    double costFromNewFather = tab[idFather].getCost() + data.getCost(arc);
                    //on pourrait aussi vérifier que le sommet est marqué ou non (pour que Dijkstra puisse tourner sans erreur dans le cas de circuit de longueur négative)
                    if(bestCostSoFar>costFromNewFather){
                        heap.remove(tab[id]);
                        tab[id].father = arc; 
                        tab[id].setRealizedCost(costFromNewFather);
                        heap.insert(tab[id]);
                    }
                }
            }
        }
        if(idFather == dest.getId()){
            notifyDestinationReached(tab[idFather].current);
        }


        List<Arc> pathListReverse = new ArrayList<Arc>();
        Arc arc;
        int lastIdFather = idFather;
        while ((arc=tab[idFather].father) != null){
            System.out.println(arc);
            pathListReverse.add(arc);
            idFather = arc.getOrigin().getId();
        }

        List<Arc> pathList = new ArrayList<Arc>();
        int n = pathListReverse.size();

        for (int i=0;i<n;i++){
            pathList.add(pathListReverse.get(n-i-1));
        }

        Path path = new Path(data.getGraph(), pathList);

        
        ShortestPathSolution solution = (lastIdFather!=dest.getId() ? new ShortestPathSolution(this.getInputData(), Status.INFEASIBLE) : new ShortestPathSolution(this.getInputData(), Status.OPTIMAL, path) );
        return solution;
    }

}
