/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import static org.epics.pvmanager.formula.ExpressionLanguage.formula;
import org.epics.pvmanager.graphene.ExpressionLanguage;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;
import org.epics.pvmanager.graphene.Graph2DExpression;
import org.epics.pvmanager.graphene.Graph2DResult;
import org.epics.pvmanager.graphene.LineGraph2DExpression;
import org.epics.pvmanager.graphene.SparklineGraph2DExpression;
import static org.epics.pvmanager.sample.BaseGraphApp.main;
import static org.epics.pvmanager.util.Executors.swingEDT;
import static org.epics.util.time.TimeDuration.ofHertz;
import org.epics.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
public class SparklineApp extends javax.swing.JFrame {

    /**
     * Creates new form SimpleScatterGraph
     */
    public SparklineApp() {
        SetupUtil.defaultCASetupForSwing();
        initComponents();
        int nFields = 4;
        pvFields.add(pvField1);
        imagePanels.add(imagePanel1);
        pvFields.add(pvField2);
        imagePanels.add(imagePanel2);
        pvFields.add(pvField3);
        imagePanels.add(imagePanel3);
        pvFields.add(pvField4);
        imagePanels.add(imagePanel4);
        for (int i = 0; i < nFields; i++) {
            pvs.add(null);
            plots.add(null);
            bindResize(i);
            bindPvChange(i);
        }
    }
    
    private PVReader<Graph2DResult> pv;
    protected SparklineGraph2DExpression plot1;
    protected List<ImagePanel> imagePanels = new ArrayList<>();
    protected List<SparklineGraph2DExpression> plots = new ArrayList<>();
    private List<PVReader<Graph2DResult>> pvs = new ArrayList<>();
    private List<JTextField> pvFields = new ArrayList<>();
    
    private void bindResize(final int i) {
        imagePanels.get(i).addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (plots.get(i) != null) {
                    plots.get(i).update(plots.get(i).newUpdate().imageHeight(imagePanels.get(i).getHeight()).imageWidth(imagePanels.get(i).getWidth()));
                }
            }
        });
    }
    
    private void bindPvChange(final int i) {
        pvFields.get(i).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pvChange(i);
            }
        });
        pvChange(i);
    }
    
    private void pvChange(final int i) {
        if (pvs.get(i) != null) {
            pvs.get(i).close();
            imagePanels.get(i).setImage(null);
            plots.set(i, null);
        }
        
        if (pvFields.get(i).getText() == null || pvFields.get(i).getText().trim().isEmpty()) {
            return;
        }

        SparklineGraph2DExpression plot = sparklineGraphOf(formula(pvFields.get(i).getText()),
                    null,
                    null);
        
        plots.set(i, plot);
        
        plots.get(i).update(plots.get(i).newUpdate().imageHeight(imagePanels.get(i).getHeight())
                .imageWidth(imagePanels.get(i).getWidth()));
        pvs.set(i, PVManager.read(plots.get(i))
                .notifyOn(swingEDT())
                .readListener(new PVReaderListener<Graph2DResult>() {

                    @Override
                    public void pvChanged(PVReaderEvent<Graph2DResult> event) {
                        setLastError(event.getPvReader().lastException());
                        if (event.getPvReader().getValue() != null) {
                            BufferedImage image = ValueUtil.toImage(event.getPvReader().getValue().getImage());
                            imagePanels.get(i).setImage(image);
                        }
                    }
                })
                .maxRate(ofHertz(50)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lastErrorField = new javax.swing.JTextField();
        configureButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        pvField1 = new javax.swing.JTextField();
        imagePanel1 = new org.epics.pvmanager.sample.ImagePanel();
        pvField2 = new javax.swing.JTextField();
        imagePanel2 = new org.epics.pvmanager.sample.ImagePanel();
        pvField3 = new javax.swing.JTextField();
        imagePanel3 = new org.epics.pvmanager.sample.ImagePanel();
        pvField4 = new javax.swing.JTextField();
        imagePanel4 = new org.epics.pvmanager.sample.ImagePanel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lastErrorField.setEditable(false);

        configureButton.setText("Configure");
        configureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureButtonActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        pvField1.setText("sim://sineWaveform(1, 100, 100, 0.01)");
        pvField1.setMinimumSize(new java.awt.Dimension(200, 22));
        pvField1.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel1.add(pvField1, gridBagConstraints);

        javax.swing.GroupLayout imagePanel1Layout = new javax.swing.GroupLayout(imagePanel1);
        imagePanel1.setLayout(imagePanel1Layout);
        imagePanel1Layout.setHorizontalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePanel1Layout.setVerticalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(imagePanel1, gridBagConstraints);

        pvField2.setText("sim://squareWaveform(1.5, 300, 300, 0.01)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel1.add(pvField2, gridBagConstraints);

        javax.swing.GroupLayout imagePanel2Layout = new javax.swing.GroupLayout(imagePanel2);
        imagePanel2.setLayout(imagePanel2Layout);
        imagePanel2Layout.setHorizontalGroup(
            imagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        imagePanel2Layout.setVerticalGroup(
            imagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(imagePanel2, gridBagConstraints);

        pvField3.setText("sim://triangleWaveform(0.5, 30,200, 0.01)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel1.add(pvField3, gridBagConstraints);

        javax.swing.GroupLayout imagePanel3Layout = new javax.swing.GroupLayout(imagePanel3);
        imagePanel3.setLayout(imagePanel3Layout);
        imagePanel3Layout.setHorizontalGroup(
            imagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        imagePanel3Layout.setVerticalGroup(
            imagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(imagePanel3, gridBagConstraints);

        pvField4.setText("sim://gaussianWaveform(2,30,300,0.01)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanel1.add(pvField4, gridBagConstraints);

        javax.swing.GroupLayout imagePanel4Layout = new javax.swing.GroupLayout(imagePanel4);
        imagePanel4.setLayout(imagePanel4Layout);
        imagePanel4Layout.setHorizontalGroup(
            imagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        imagePanel4Layout.setVerticalGroup(
            imagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel1.add(imagePanel4, gridBagConstraints);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                    .addComponent(lastErrorField)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(configureButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(configureButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    protected void openConfigurationDialog() {
        
    }
    
    private void configureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureButtonActionPerformed
        openConfigurationDialog();
    }//GEN-LAST:event_configureButtonActionPerformed

    private void setLastError(Exception ex) {
        if (ex != null) {
            lastErrorField.setText(ex.getMessage());
            ex.printStackTrace();
        } else {
            lastErrorField.setText("");
        }
    }
    
    public static void main(String[] args) {
        main(SparklineApp.class);
    }
    
    public static void main(final Class<? extends SparklineApp> clazz) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SparklineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SparklineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SparklineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SparklineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    clazz.newInstance().setVisible(true);
                } catch (InstantiationException ex) {
                    Logger.getLogger(SparklineApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(SparklineApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton configureButton;
    private org.epics.pvmanager.sample.ImagePanel imagePanel1;
    private org.epics.pvmanager.sample.ImagePanel imagePanel2;
    private org.epics.pvmanager.sample.ImagePanel imagePanel3;
    private org.epics.pvmanager.sample.ImagePanel imagePanel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField lastErrorField;
    private javax.swing.JTextField pvField1;
    private javax.swing.JTextField pvField2;
    private javax.swing.JTextField pvField3;
    private javax.swing.JTextField pvField4;
    // End of variables declaration//GEN-END:variables
}
