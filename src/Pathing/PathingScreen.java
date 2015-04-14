package Pathing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
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
                    graph = new Graph(Integer.parseInt(nodeNumber.getText()), (displayPanel.getX() + displayPanel.getWidth() - Integer.parseInt(nodeRadius.getText())),
                            (displayPanel.getY() + displayPanel.getHeight() - 200), displayPanel.getX(), displayPanel.getY());
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
                DijkstrasSP dijkstrasSP = new DijkstrasSP(graph, Graph.getNodeSet().get(r.nextInt(Graph.getNodeSet().size())),
                        pathingScreen, Long.parseLong(delayInMilliseconds.getText()));
            }
        });

        this.primsSpanningTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PrimsMST primsMST = new PrimsMST(graph, Graph.getNodeSet().get(r.nextInt(Graph.getNodeSet().size())),
                        pathingScreen, Long.parseLong(delayInMilliseconds.getText()));
            }

        });
            this.eraseXtraEdgesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eraseUnusedLines();
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
        for (Edge e : Graph.getEdgeSet()) {
            if (e.getColor().equalsIgnoreCase("WHITE")) {
                paintSingleEdge(e, displayPanel.getBackground());
            }
        }
    }

}
