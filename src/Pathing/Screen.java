package Pathing;

import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

public class Screen extends JPanel {
    private int verticalLines = 300, horizontalLines = 300;
    private Vector<Node> nodesOnScreen = new Vector<Node>(5);
    private Vector<Edge> edgesOnScreen = new Vector<Edge>(5);
    private Random r = new Random();
    private int pixelSize = 3;


    public static void main(String[] args){
        new Screen();
    }



    public Screen(){
        JFrame contentFrame = new JFrame("Screen Simulator");
        GroupLayout layout = new GroupLayout(contentFrame.getContentPane());
        JScrollPane scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        final JTextField width = new JTextField("200");
        final JTextField height = new JTextField("200");
        final JSlider radius = new JSlider(1, 25, 3);
        radius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider radius = (JSlider) changeEvent.getSource();
                pixelSize = radius.getValue();
                repaint();
            }
        });
        radius.setMajorTickSpacing(1);
        radius.setMinorTickSpacing(1);
        radius.setPaintTicks(true);
        radius.setPaintLabels(true);

        JButton redraw = new JButton("Redraw");
        redraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateSize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
                addRandomNodes(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()), 0, 0, 30);
                addRandomEdges();
            }
        });
        contentFrame.setSize(new Dimension(800, 600));
        contentFrame.setLocation(0, 0);

        JLabel x = new JLabel("x");
        JLabel widthLabel = new JLabel("width");
        JLabel heighLabel = new JLabel("height");
        JLabel radiusLabel = new JLabel("pixel radius");

        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup()
                                                        .addGroup(layout.createSequentialGroup()
                                                                        .addComponent(widthLabel)
                                                                        .addComponent(width, 25, 50, 150)
                                                                        .addComponent(x)
                                                                        .addComponent(heighLabel)
                                                                        .addComponent(height, 25, 50, 150)
                                                        )
                                                        .addComponent(radiusLabel)
                                                        .addComponent(radius)
                                        )
                                        .addComponent(redraw)
                        )

        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createParallelGroup()
                                        .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup()
                                                                        .addComponent(widthLabel)
                                                                        .addComponent(width, 5, 10, 20)
                                                                        .addComponent(x)
                                                                        .addComponent(heighLabel)
                                                                        .addComponent(height, 5, 10, 20)
                                                        )
                                                        .addComponent(radiusLabel)
                                                        .addComponent(radius)
                                        )
                                        .addComponent(redraw)
                        )

        );

        contentFrame.getContentPane().setLayout(layout);
        contentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentFrame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintNodesToScreen((Graphics2D)g);
        paintEdgesToScreen((Graphics2D)g);
    }

    /**
     * updates the size of the window
     *
     * @param width new width of the window
     * @param height new height of the window
     * */
    public void updateSize(int width, int height){
        verticalLines = width;
        horizontalLines = height;
        repaint();
    }

    /**
     * adds a random number of nodes to the screen's vector of nodes
     *
     * @param xHBounds x coordinates of the new node will be lesser than this value
     * @param yBounds y coordinates of the new node will be lesser than this value
     * @param xLBounds x coordinates of the new node will be greater than this value
     * @param yLBounds y coordinates of the new node will be greater than this value
     * @param amount number of Nodes the method will generate and add to the vector
     *
     * ToDo: add graphical representation for the nodes.
     * */
    public void addRandomNodes(int xHBounds, int yBounds, int xLBounds, int yLBounds, int amount){
        Node temp = new Node(1,1);
        for (int i = 0; i <amount; i++){
            this.nodesOnScreen.add(temp.getRandomNode(xHBounds, yBounds, xLBounds, yLBounds));
        }
    }

    public void paintNodesToScreen(Graphics2D g2){

        for(Node n : nodesOnScreen){
            g2.drawOval(n.getX(), n.getY(), 15, 15);
        }
        repaint();
    }

    public void paintEdgesToScreen(Graphics2D g2){
        for(Edge e: edgesOnScreen){
            g2.drawLine(e.getFrontNode().getX(), e.getFrontNode().getY(),
                    e.getBackNode().getX(), e.getBackNode().getY());
        }
        repaint();
    }
    /**
     * adds one to 3 edges to each node currently on the screen. The edge connected to the currently selected node is
     * chosen at random from the current nodes on screen vector by retrieving the node at a randomly generated index
     * between the beginning and the last index containing a node.
     *
     * ToDo: add graphic representation for the edges.
     * */
    public void addRandomEdges(){
try {
    for (Node node : nodesOnScreen) {

        for (int i = 0; i < r.nextInt(3) + 1; i++) {

            edgesOnScreen.add(new Edge(node, nodesOnScreen.elementAt(r.nextInt(nodesOnScreen.lastIndexOf(node)))));
        }
    }
}catch (NullPointerException p){
    System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
            " a node which does not exist.");
}
    }
}