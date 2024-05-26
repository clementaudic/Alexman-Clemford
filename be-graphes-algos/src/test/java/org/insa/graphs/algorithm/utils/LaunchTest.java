package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

import org.junit.Before;
import org.junit.Test;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;


public class LaunchTest {

    public int numberOfTests;

    public Path[] benchmarkPathTab;
    public Path[] dijsktraPathTab;
    public Path[] aStarPathTab;

    public int numberOfTestsBis;

    public Status[] benchmarkUnfeasibleTab;
    public Status[] dijsktraUnfeasibleTab;
    public Status[] aStarUnfeasibleTab;

    @Before
    public void initAll() throws IOException {

        numberOfTests = 10;
        dijsktraPathTab = new Path[numberOfTests];
        benchmarkPathTab = new Path[numberOfTests];
        aStarPathTab = new Path[numberOfTests];

        String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        Graph graph = reader.read();
        
        int i = 0;
        while(i<benchmarkPathTab.length){
            int origin = (int)(Math.random() * graph.size());
            int destination = (int)(Math.random() * graph.size());

            ShortestPathData data = new ShortestPathData(graph,graph.getNodes().get(origin),graph.getNodes().get(destination), ArcInspectorFactory.getAllFilters().get(i%5));
            ShortestPathSolution benchmarkResult = new BellmanFordAlgorithm(data).doRun();

            if(benchmarkResult.getStatus()!=Status.INFEASIBLE && benchmarkResult.getStatus()!=Status.UNKNOWN){
                benchmarkPathTab[i] = benchmarkResult.getPath();
                dijsktraPathTab[i] = new DijkstraAlgorithm(data).doRun().getPath();
                aStarPathTab[i] = new AStarAlgorithm(data).doRun().getPath();
                i++;
            }

        }   

        numberOfTestsBis = 5;
        benchmarkUnfeasibleTab = new Status[numberOfTestsBis];
        dijsktraUnfeasibleTab = new Status[numberOfTestsBis];
        aStarUnfeasibleTab = new Status[numberOfTestsBis];
        
        String mapNameBis = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/guadeloupe.mapgr";
        
        GraphReader readerBis = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapNameBis))));

        Graph graphBis = readerBis.read();

        for(int j=0;j<numberOfTestsBis;j++){
            ShortestPathData data = new ShortestPathData(graphBis,graphBis.getNodes().get(34593),graphBis.getNodes().get(93), ArcInspectorFactory.getAllFilters().get(i%5));
            benchmarkUnfeasibleTab[j] = new BellmanFordAlgorithm(data).doRun().getStatus();
            dijsktraUnfeasibleTab[j] = new DijkstraAlgorithm(data).doRun().getStatus();
            aStarUnfeasibleTab[j] = new AStarAlgorithm(data).doRun().getStatus();
        }
    }

    @Test
    public void testTravelTimePathDijsktra(){
        for(int i=0;i<dijsktraPathTab.length;i++){
            assertEquals(benchmarkPathTab[i].getMinimumTravelTime(),dijsktraPathTab[i].getMinimumTravelTime(),0.1);
        }
    }

    @Test
    public void testDistancePathDijsktra(){
        for(int i=0;i<dijsktraPathTab.length;i++){
            assertEquals(benchmarkPathTab[i].getLength(),dijsktraPathTab[i].getLength(),0.1);
        }
    }

    @Test
    public void testTravelTimePathAStar(){
        for(int i=0;i<aStarPathTab.length;i++){
            assertEquals(benchmarkPathTab[i].getMinimumTravelTime(),aStarPathTab[i].getMinimumTravelTime(),0.1);
        }
    }

    @Test
    public void testDistancePathAStar(){
        for(int i=0;i<aStarPathTab.length;i++){
            assertEquals(benchmarkPathTab[i].getLength(),aStarPathTab[i].getLength(),0.1);
        }
    }

    @Test
    public void testUnfeasibleDijsktra(){
        for(int i=0;i<dijsktraUnfeasibleTab.length;i++){
            assertEquals(Status.INFEASIBLE,dijsktraUnfeasibleTab[i]);
        }
    }

    @Test
    public void testUnfeasibleAStar(){
        for(int i=0;i<aStarUnfeasibleTab.length;i++){
            assertEquals(Status.INFEASIBLE,aStarUnfeasibleTab[i]);
        }
    }
  
}
