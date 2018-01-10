/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import static org.diirt.datasource.timecache.ExpressionLanguage.timeTableOf;
import static org.diirt.datasource.ExpressionLanguage.resultOf;
import static org.diirt.vtype.ValueFactory.alarmNone;
import static org.diirt.vtype.ValueFactory.newVString;
import static org.diirt.vtype.ValueFactory.timeNow;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.diirt.datasource.timecache.query.QueryParameters;
import org.diirt.datasource.ExpressionLanguage.OneArgFunction;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.loc.LocalDataSource;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeRelativeInterval;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.diirt.vtype.io.CSVIO;

/**
 *
 * @author carcassi
 */
@SuppressWarnings("serial")
public class SampleTableClient extends javax.swing.JFrame {

    private PVReader<VString> pvReader = null;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static Instant start;
    private static Instant end;

    /**
     * Creates new form SampleTableClient
     */
    public SampleTableClient() {
        initComponents();
        try {
            // TODO: these can become different fields
            start = dateFormat.parse("2014-04-03 09:00").toInstant();
            end = dateFormat.parse("2014-04-04 09:00").toInstant();
        } catch (ParseException ex) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        channelNameField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultField = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("channelName");

        channelNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                channelNameFieldActionPerformed(evt);
            }
        });

        resultField.setColumns(20);
        resultField.setRows(5);
        jScrollPane1.setViewportView(resultField);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(channelNameField)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(channelNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void channelNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_channelNameFieldActionPerformed
        reconnect();
    }//GEN-LAST:event_channelNameFieldActionPerformed

    private void reconnect() {
        if (pvReader != null) {
            pvReader.close();
            pvReader = null;
        }

        resultField.setText("");

        pvReader = PVManager.read(resultOf(new OneArgFunction<VString, VTable>() {

            private VTable oldArg;
            private VString oldResult;

            @Override
            public VString calculate(VTable arg) {
                if (arg == null) {
                    return null;
                }

                // Don't reformat if same result
                if (oldArg == arg) {
                    return oldResult;
                }

                oldArg = arg;

                CSVIO io = new CSVIO();
                StringWriter out = new StringWriter();
                io.export(arg, out);
                oldResult = newVString(out.toString(), alarmNone(), timeNow());
                return oldResult;
            }
        }, timeTableOf(channelNameField.getText(), new QueryParameters().timeInterval(TimeRelativeInterval.of(start, end)))))
                .readListener(new PVReaderListener<VString>() {

            @Override
            public void pvChanged(PVReaderEvent<VString> event) {
                if (event.isValueChanged()) {
                    resultField.setText(event.getPvReader().getValue().getValue());
                }
            }
        }).maxRate(TimeDuration.ofHertz(10));

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(SampleTableClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SampleTableClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SampleTableClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SampleTableClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Just to make things work
        PVManager.setDefaultDataSource(new LocalDataSource());

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SampleTableClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField channelNameField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea resultField;
    // End of variables declaration//GEN-END:variables
}
