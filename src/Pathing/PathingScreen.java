package Pathing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

/**
 * Created by robert on 4/3/2015.
 */
public class PathingScreen extends JPanel {
    private int verticalLines = 800, horizontalLines = 1200;

    private JTextField width;
    private JTextField height;
    private JButton drawNodesButton;
    private JButton clearButton;
    private JButton steppingDjikstraSButton;
    private JTextField nodeNumber;
    private JButton drawEdgesButton;
    private JTextField delayInMilliseconds;
    private JButton delayedDjikstraSButton;
    private JPanel displayPanel;

    private Vector<Node> nodesOnScreen = new Vector<Node>(5);
    private Vector<Edge> edgesOnScreen = new Vector<Edge>(5);
    private Random r = new Random();

    public PathingScreen() {

        this.add(clearButton);
        this.add(width);
        this.add(height);
        this.add(drawNodesButton);
        this.add(drawEdgesButton);
        this.add(steppingDjikstraSButton);
        this.add(nodeNumber);
        this.add(delayInMilliseconds);
        this.add(delayedDjikstraSButton);
        this.add(displayPanel);
        this.setBounds(100, 100, 1200, 800);


        this.drawNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRandomNodes(Integer.parseInt(height.getText()),
                        Integer.parseInt(width.getText()), 0, 0, Integer.parseInt(nodeNumber.getText()));
                repaint();
            }
        });

        this.drawEdgesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRandomEdges();
                repaint();

            }
        });

        this.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("bank_gui");
        frame.setContentPane(new PathingScreen());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintNodesToScreen((Graphics2D) g);
        paintEdgesToScreen((Graphics2D) g);

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

    /**
     * adds a random number of nodes to the screen's vector of nodes
     *
     * @param xHBounds x coordinates of the new node will be lesser than this value
     * @param yBounds  y coordinates of the new node will be lesser than this value
     * @param xLBounds x coordinates of the new node will be greater than this value
     * @param yLBounds y coordinates of the new node will be greater than this value
     * @param amount   number of Nodes the method will generate and add to the vector
     *                 <p>
     *                 ToDo: add graphical representation for the nodes.
     */
    public void addRandomNodes(int xHBounds, int yBounds, int xLBounds, int yLBounds, int amount) {
        Node temp = new Node(1, 1);
        for (int i = 0; i < amount; i++) {
            this.nodesOnScreen.add(temp.getRandomNode(xHBounds, yBounds, xLBounds, yLBounds));
        }
    }

    public void paintNodesToScreen(Graphics2D g2) {

        for (Node n : nodesOnScreen) {
            g2.drawOval(n.getX(), n.getY(), 15, 15);
        }
        repaint();
    }

    public void paintEdgesToScreen(Graphics2D g2) {
        for (Edge e : edgesOnScreen) {
            g2.drawLine(e.getFrontNode().getX(), e.getFrontNode().getY(),
                    e.getBackNode().getX(), e.getBackNode().getY());
        }
        repaint();
    }

    /**
     * adds one to 3 edges to each node currently on the screen. The edge connected to the currently selected node is
     * chosen at random from the current nodes on screen vector by retrieving the node at a randomly generated index
     * between the beginning and the last index containing a node.
     * <p>
     * ToDo: add graphic representation for the edges.
     */
    public void addRandomEdges() {
        try {
            for (Node node : nodesOnScreen) {

                for (int i = 0; i < r.nextInt(3) + 1; i++) {
                    edgesOnScreen.add(new Edge(node, nodesOnScreen.elementAt(r.nextInt(nodesOnScreen.size()))));
                }
            }
        } catch (NullPointerException p) {
            System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
                    " a node which does not exist.");
        }
    }
}
