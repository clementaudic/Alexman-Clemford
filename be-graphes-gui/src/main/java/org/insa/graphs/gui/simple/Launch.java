package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */

    public static Path[] dijsktraPathTab;
    public static Path[] benchmarkPathTab;
    public static Path[] aStarPathTab;

    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // Visit these directory to see the list of available files on Commetud.
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        final Graph graph = reader.read();

        // Create the drawing:
        final Drawing drawingDijsktra = createDrawing();
        final Drawing drawingBellmanFord = createDrawing();
        final Drawing drawingAStar = createDrawing();

        // Draw the graph on the drawing
        drawingDijsktra.drawGraph(graph);
        drawingBellmanFord.drawGraph(graph);
        drawingAStar.drawGraph(graph);

        // Create a PathReader.
        final PathReader pathReader = new BinaryPathReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));;

        dijsktraPathTab = new Path[10];
        benchmarkPathTab = new Path[10];
        aStarPathTab = new Path[10];
        
        int i = 0;
        while(i<benchmarkPathTab.length){
            int origin = (int)(Math.random() * graph.size());
            int destination = (int)(Math.random() * graph.size());
            ShortestPathData data = new ShortestPathData(graph,graph.getNodes().get(origin),graph.getNodes().get(destination), ArcInspectorFactory.getAllFilters().get(0));
            ShortestPathSolution benchmarkResult = new BellmanFordAlgorithm(data).doRun();

            if(benchmarkResult.getStatus()!=Status.INFEASIBLE && benchmarkResult.getStatus()!=Status.UNKNOWN){
                benchmarkPathTab[i] = benchmarkResult.getPath();
                drawingBellmanFord.drawPath(benchmarkPathTab[i],Color.GREEN);
                dijsktraPathTab[i] = new DijkstraAlgorithm(data).doRun().getPath();
                drawingDijsktra.drawPath(dijsktraPathTab[i],Color.RED);
                aStarPathTab[i] = new AStarAlgorithm(data).doRun().getPath();
                drawingAStar.drawPath(aStarPathTab[i],true);
                i++;
            }

        }   

    }

}
