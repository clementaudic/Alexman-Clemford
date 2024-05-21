package org.insa.graphs.gui.simple;

import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.Transient;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.junit.Before;
import org.junit.Test;

public class LaunchTest {

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
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

    @Before
    public void initAll() {

        // Visit these directory to see the list of available files on Commetud.
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        
        String[] mapTab = new String[]{"/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr",""};
        
        final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";


        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // TODO: Read the graph.
        final Graph graph = reader.read();

        // Create the drawing:
        //final Drawing drawing = createDrawing();

        // TODO: Draw the graph on the drawing.
        //drawing.drawGraph(graph);

        /* 
        for(int i=0;i<pathNameTab.length();i++){
            // TODO: Create a PathReader.
            final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathNameTab[i]))));
            // TODO: Read the path.
            final Path path = pathReader.readPath(graph);
            pathTab[i] = pathReader;
        }

        // Create random paths
        int britannyMax = 
        for(int i=0;i<150;i++){
            int origin = random();
            pathTab[i] = ShortestPathData(graph, Node origin, Node destination, ArcInspector arcInspector)
        }
    
        */

        // TODO: Draw the path.
        //drawing.drawPath(path);

    }

    @Test
    public void testTravelTimePath(){
        for(int i=0;i<pathTab.length();i++){
            assertEquals(benchmarkPathTab.getTravelTime(),pathTab.getTravelTime());
        }
    }

    public void testDistancePath(){
        for(int i=0;i<pathTab.length();i++){
            assertEquals(benchmarkPathTab.getDestination(),pathTab.getDistance());
        }
    }








}
