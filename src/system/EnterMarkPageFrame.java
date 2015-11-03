/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import system.Mark.DataTableMark;

/**
 *
 * @author super
 */
public class EnterMarkPageFrame extends javax.swing.JFrame {
    DefaultTableModel tblModel;
    public List<DataTableMark> lstData;

    public EnterMarkPageFrame() throws HeadlessException {
    }
    /**
     * Creates new form EnterMarkPageFrame
     * @param CourseID
     */
    
    
    public EnterMarkPageFrame(int CourseID) {
        try {
            initComponents();
            tblModel=new DefaultTableModel();
            tblModel.addColumn("Subject");
            tblModel.addColumn("Mark");
            Connection conn=DBConnect.connectDatabase();
            try {
                PreparedStatement pre=conn.prepareStatement("select * from subject where courseid=?");
                pre.setInt(1, CourseID);
                ResultSet rs=pre.executeQuery();
                while(rs.next()){
                    String[] tempRow={rs.getString("name"),""};
                    tblModel.addRow(tempRow);
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            jTable1.setModel(tblModel);
            jTable1.getColumnModel().getColumn(0).setMinWidth(300);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
        } catch (SQLException ex) {
            Logger.getLogger(EnterMarkPageFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<DataTableMark> getMarkInformation(){
           List<DataTableMark> lstDataTableMark=new ArrayList<>();
           List<String> lstSubjectName=new ArrayList<>();
           List<String> lstMark=new ArrayList<>();
           for (int i = 0; i < jTable1.getModel().getRowCount(); i++) {
            lstSubjectName.add((String)jTable1.getValueAt(i, 0));
        }
           for (int i = 0; i < jTable1.getModel().getRowCount(); i++) {
            lstMark.add((String)jTable1.getValueAt(i, 1));
        }
           for (int i=0;i<lstSubjectName.size();i++) {
            int tempID=getIdFromName(lstSubjectName.get(i));            
            DataTableMark markData=new DataTableMark(tempID,lstMark.get(i) );
            lstDataTableMark.add(markData);
        }
           
           return lstDataTableMark;
    }

    private int getIdFromName(String name){
        Connection conn= null;
        try {
            conn = DBConnect.connectDatabase();
            PreparedStatement pre=conn.prepareStatement("select * from subject where name=?");
            pre.setString(1, name);
            ResultSet rs=pre.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Verify25.png"))); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //this.setVisible(false);
        lstData=getMarkInformation();
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(EnterMarkPageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnterMarkPageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnterMarkPageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnterMarkPageFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EnterMarkPageFrame(2).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
