package org.insa.graphs.algorithm.marathon;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.AccessRestrictions.AccessRestriction;

import java.lang.invoke.VarHandle.AccessMode;
import java.util.EnumSet;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.AccessRestrictions;


public class MarathonAlgorithm {
    Point origin;
    Node endNode;
    float dist;
    Graph graph;
    float maxLength =0;

    public MarathonAlgorithm(Point origin, Graph graph, float dist){
        this.dist = dist;
        this.origin = origin;
        this.graph = graph;
        for (Node node:this.graph.getNodes()){
            for (Arc arc:node.getSuccessors()){
                if (arc.getLength()>maxLength) maxLength = arc.getLength();
            }
        }
    }

    public Path doRun(){
        Node startNode = this.findNearestNode(origin);
        DijkstraAlgorithm algo1;
        DijkstraAlgorithm algo2;
        double bestPathScore = Double.NEGATIVE_INFINITY;
        Path bestPath = null;
        for (int i=0;i<1000;i++){
            Node dest = this.findRandomNodeAtDistance(startNode.getPoint(), MarathonAlgorithm.getRandomFloatBetween((float)(0.60*this.dist/2), this.dist/2));
            algo1 = new DijkstraAlgorithm(new ShortestPathData(this.graph, startNode, dest, new ArcInspector () {
                @Override
                public boolean isAllowed(Arc arc) {
                    return MarathonAlgorithm.allowedForPedestrians(arc.getRoadInformation().getAccessRestrictions());
                            
                }
                @Override
                public double getCost(Arc arc) {
                    //we favour longer arcs cuz more comfortable to run/walk in
                    return MarathonAlgorithm.this.maxLength - arc.getLength();
                }
                
                @Override
                public Mode getMode() {
                    return Mode.LENGTH;
                }
            }));
            ShortestPathSolution halfway = algo1.doRun();
            List<Arc> halfwayArcs = halfway.getPath().getArcs();
            if (!halfway.isFeasible()) continue;

            algo2 = new DijkstraAlgorithm(new ShortestPathData(this.graph, dest, startNode, new ArcInspector () {
                @Override
                public boolean isAllowed(Arc arc) {
                    //ban already used arcs
                    return MarathonAlgorithm.allowedForPedestrians(arc.getRoadInformation().getAccessRestrictions()) && !(halfwayArcs.contains(arc));
                            
                }

                @Override
                public double getCost(Arc arc) {
                    //we favour longer arcs cuz more comfortable to run/walk in
                    return MarathonAlgorithm.this.maxLength - arc.getLength();
                }

                @Override
                public Mode getMode() {
                    return Mode.LENGTH;
                }
            }));

            ShortestPathSolution wayBack = algo2.doRun();
            if (!wayBack.isFeasible()) continue;
            Path finalPath = Path.concatenate(halfway.getPath(),wayBack.getPath());

            double pathScore = this.calculateScore(finalPath);
            if (pathScore > bestPathScore){
                bestPathScore = pathScore;
                bestPath = finalPath;
            }

        }

        return bestPath;
    }

    public Node findNearestNode(Point point){
        double maxDistance = this.dist;
        Node nearestNode = null;
        nodeLoop : for (Node node:this.graph.getNodes()){
            double distance;
            if ((distance=node.getPoint().distanceTo(point)) > maxDistance) continue;
            for (Arc arc:node.getSuccessors()){
                AccessRestrictions access = arc.getRoadInformation().getAccessRestrictions();
                if (MarathonAlgorithm.allowedForPedestrians(access)){
                    maxDistance = distance;
                    nearestNode = node;
                    continue nodeLoop;
                }
            }
        }
        return nearestNode;
    }

    public static boolean allowedForPedestrians(AccessRestrictions access){
        return access.isAllowedForAny(AccessRestrictions.AccessMode.FOOT, EnumSet.complementOf(EnumSet
        .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
    }

    public double calculateScore(Path path){
        //the closer to the planned distance, the better
        double score = this.dist - Math.abs(this.dist - path.getLength());
        return score;
    }

    public Node findRandomNodeAtDistance(Point point, float distance){
        double theta = MarathonAlgorithm.getRandomFloatBetween((float)0.0, (float)Math.PI*2);
        Point newPoint = new Point(point.getLongitude()+(float)Math.cos(theta)*distance, point.getLatitude()+(float)Math.sin(theta)*distance);
        return this.findNearestNode(newPoint);

    }

    public static float getRandomFloatBetween(float a, float b){
        return a + ((float)Math.random())*(b-a);
    }


    
}
