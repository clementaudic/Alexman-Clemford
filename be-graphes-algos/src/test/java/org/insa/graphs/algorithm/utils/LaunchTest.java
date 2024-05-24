package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.Transient;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.junit.Before;
import org.junit.Test;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;


public class LaunchTest {

    public Path[] dijsktraPathTab;
    public Path[] benchmarkPathTab;
    public Path[] aStarPathTab;


    @Before
    public void initAll() throws IOException {

        dijsktraPathTab = new Path[150];
        benchmarkPathTab = new Path[150];
        aStarPathTab = new Path[150];

        String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        Graph graph = reader.read();
        
        int i = 0;
        while(i<150){
            int origin = (int)(Math.random() * graph.size());
            int destination = (int)(Math.random() * graph.size());
            ShortestPathData data = new ShortestPathData(graph,graph.getNodes().get(origin),graph.getNodes().get(destination), ArcInspectorFactory.getAllFilters().get(0));
            ShortestPathSolution benchmarkResult = new BellmanFordAlgorithm(data).doRun();

            if(benchmarkResult.getStatus()!=Status.INFEASIBLE && benchmarkResult.getStatus()!=Status.UNKNOWN){
                benchmarkPathTab[i] = benchmarkResult.getPath();
                dijsktraPathTab[i] = new DijkstraAlgorithm(data).doRun().getPath();
                aStarPathTab[i] = new AStarAlgorithm(data).doRun().getPath();
                i++;
            }

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
}
