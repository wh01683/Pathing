package Pathing;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen extends JPanel {
    private int verticalLines = 800, horizontalLines = 1200;
    private int pixelSize = 3;





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




}