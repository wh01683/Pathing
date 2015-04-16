package Pathing;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robert on 4/3/2015.
 */
public class PathingScreen extends JPanel {

    private static Graph graph;
    private static PathingScreen pathingScreen;
    private int verticalLines = 800, horizontalLines = 1200;
    private JTextField width;
    private JTextField height;
    private JButton drawNodesButton;
    private JButton clearButton;
    private JTextField nodeNumber;
    private JButton drawEdgesButton;
    private JTextField delayInMilliseconds;
    private JButton delayedDjikstraSButton;
    private JPanel displayPanel;
    private JPanel dashboardPanel;
    private JPanel boundsPanel;
    private JLabel yLabel;
    private JLabel xLabel;
    private JPanel outerPanel;
    private JTextField nodeRadius;
    private JButton resizeButton;
    private JButton primsSpanningTree;
    private JButton eraseXtraEdgesButton;
    private JPanel tableTabPanel;
    private JTable edgeInfoTable;
    private JTable generalInfo;
    private JButton populateTableButton;
    private JPanel graphTabPanel;
    private JTabbedPane tabPane;

    private Random r = new Random();

    public PathingScreen() {

        try {

        displayPanel.setVisible(true);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                displayPanel.setBounds(e.getComponent().getX(), e.getComponent().getY(), e.getComponent().getWidth() - 20, e.getComponent().getHeight() - 20);
            }
        });
        displayPanel.setBounds(displayPanel.getX(), displayPanel.getY(), Integer.parseInt(width.getText()),
                Integer.parseInt(height.getText()));

            this.drawNodesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (graph == null) {
                        graph = new Graph(Integer.parseInt(nodeNumber.getText()), (displayPanel.getX() + displayPanel.getWidth() - Integer.parseInt(nodeRadius.getText())),
                                (displayPanel.getY() + displayPanel.getHeight() - 200), displayPanel.getX(), displayPanel.getY());
                    } else {
                        Graph.addRandomNodes(Integer.parseInt(nodeNumber.getText()), (displayPanel.getX() + displayPanel.getWidth() - Integer.parseInt(nodeRadius.getText())),
                                (displayPanel.getY() + displayPanel.getHeight() - 200), displayPanel.getX(), displayPanel.getY());
                    }

                    paintNodesToScreen(Color.WHITE);
                }
            });


        this.drawEdgesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent p) {
                graph.addEdges();
                paintEdgesToScreen(Color.WHITE);
            }
        });

        this.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent l) {
                displayPanel.getGraphics().clearRect(displayPanel.getX(), displayPanel.getY(),
                        displayPanel.getX() + displayPanel.getWidth(), displayPanel.getY() + displayPanel.getHeight());
                graph = null;
            }
        });

        this.resizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                displayPanel.setBounds(displayPanel.getX(), displayPanel.getY(), Integer.parseInt(width.getText()),
                        Integer.parseInt(height.getText()));
                graph = null;
            }
        });

        this.delayedDjikstraSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DijkstrasSP dijkstrasSP = new DijkstrasSP(Graph.getNodeSet().get(r.nextInt(Graph.getNodeSet().size())),
                        pathingScreen, Long.parseLong(delayInMilliseconds.getText()));


            }
        });

        this.primsSpanningTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PrimsMST primsMST = new PrimsMST(Graph.getNodeSet().get(r.nextInt(Graph.getNodeSet().size())),
                        pathingScreen, Long.parseLong(delayInMilliseconds.getText()));
            }

        });
            this.eraseXtraEdgesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eraseUnusedLines();
                }
            });

            this.populateTableButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    populateTableInformation();
                }
            });
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Djikstra Simulation");
        pathingScreen = new PathingScreen();
        frame.setContentPane(pathingScreen.outerPanel);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)displayPanel.getGraphics();
        paintNodesToScreen(Color.WHITE);
        paintEdgesToScreen(Color.WHITE);
        displayPanel.paint(g2);
    }
    /**
     * updates the size of the window
     *
     * @param width  new width of the window
     * @param height new height of the window
     */
    public void updateSize(int width, int height) {
        verticalLines = width;
        horizontalLines = height;
        repaint();
    }

    public void paintNodesToScreen(Color newColor) {
        for (Node n : Graph.getNodeSet()) {
            paintSingleNode(n, newColor);
        }
        repaint();
    }

    public void paintSingleNode(Node n, Color newColor) {
        Graphics2D graphics2D = (Graphics2D) displayPanel.getGraphics();
        graphics2D.setColor(newColor);
        graphics2D.fillOval(n.getX(), n.getY(), n.getRadius(), n.getRadius());
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawOval(n.getX(), n.getY(), n.getRadius(), n.getRadius());


    }

    public void paintSingleEdge(Edge e, Color newColor) {
        Graphics2D graphics2D = (Graphics2D) displayPanel.getGraphics();
        graphics2D.setColor(newColor);
        graphics2D.draw(new Line2D.Double((e.getFromNode().getX()) + (Integer.parseInt(nodeRadius.getText()) / 2),
                (e.getFromNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getToNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getToNode().getY()) + (Integer.parseInt(nodeRadius.getText()) / 2)));
    }

    public void paintEdgesToScreen(Color newColor) {
        for (Edge e : Graph.getEdgeSet()) {
            paintSingleEdge(e, newColor);
        }
        repaint();
    }

    public void eraseUnusedLines() {
        try {
            ArrayList<Edge> unusedEdges = Graph.getEdgeSet();
            unusedEdges.removeAll(Graph.getUsedEdgeSet().keySet());
            for (Edge e : unusedEdges) {
                paintSingleEdge(e, displayPanel.getBackground());
            }
            for (Edge k : Graph.getUsedEdgeSet().keySet()) {
                paintSingleEdge(k, Color.BLACK);
            }
        } catch (NullPointerException n) {

        }
    }

    public void populateTableInformation() {

        DefaultTableModel edgeTableModel = (DefaultTableModel) edgeInfoTable.getModel();
        String[] edgeInfoColumnHeaders = {"Used Edges", "Times Traversed"};
        for (String s : edgeInfoColumnHeaders) {
            edgeTableModel.addColumn(s);
        }
        edgeTableModel.addRow(edgeInfoColumnHeaders);
        String[][] edgeRowData = new String[Graph.getUsedEdgeSet().size()][2];
        int i = 0;
        for (Edge e : Graph.getUsedEdgeSet().keySet()) {
            int p = Graph.getUsedEdgeSet().get(e);
            edgeRowData[i][0] = "Edge " + i;
            edgeRowData[i][1] = p + "";
            edgeTableModel.addRow(edgeRowData[i]);
            i++;
        }

        edgeInfoTable.setModel(edgeTableModel);

        DefaultTableModel generalModel = (DefaultTableModel) generalInfo.getModel();

        String[] generalInfoColumnHeaders = {"Total Nodes", "Total Edges", "Traversed Edges", "Edges Used", "Weight of Graph", "Weight Used"};

        for (String s : generalInfoColumnHeaders) {
            generalModel.addColumn(s);
        }
        generalModel.addRow(generalInfoColumnHeaders);
        generalModel.addRow(Graph.getData());
        generalInfo.setModel(generalModel);


    }

}
