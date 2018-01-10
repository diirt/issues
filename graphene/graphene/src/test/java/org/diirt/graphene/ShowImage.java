/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.diirt.util.array.ArrayDouble;

/**
 *
 * @author carcassi
 */
public class ShowImage extends javax.swing.JFrame {

    /**
     * Creates new form ShowImage
     */
    public ShowImage() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel = new org.diirt.graphene.ImagePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(imagePanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setImage(Image image) {
        //getContentPane().setSize(image.getWidth(this), image.getHeight(this));
        imagePanel.setImage(image);
        pack();
    }

    private static void showImage(final Image image) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ShowImage frame = new ShowImage();
                frame.setImage(image);
                frame.setVisible(true);
            }
        });

    }

    public static void main(String args[]) throws Exception {
        Cell1DDataset hist = Cell1DDatasets.linearRange(new ArrayDouble(30, 14, 150, 160, 180, 230, 220, 350, 400, 450, 500,
                                        350, 230, 180, 220, 170, 130, 80, 30, 40), 0.0, 2.0);
        AreaGraph2DRenderer renderer = new AreaGraph2DRenderer(300, 200);
        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        renderer.draw(graphics, hist);
        showImage(image);
//        ImageIO.write(image, "png", new File("hist1dtest.png"));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.diirt.graphene.ImagePanel imagePanel;
    // End of variables declaration//GEN-END:variables
}
