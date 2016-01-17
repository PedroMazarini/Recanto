/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Classes.Consumo;
import Classes.Produto;
import DAO.ConsumoDAO;
import DAO.DAO;
import java.awt.Component;
import java.awt.Dimension;
import static java.awt.Event.KEY_PRESS;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author PedroMazarini
 */
public class Tela_produtos extends javax.swing.JFrame {

    /**
     * Creates new form Tela_produtos
     */
    List<Produto> lista_produtos;
    List<Produto> lista_table;
    Tela_comanda tela;
    public Tela_produtos(List<Produto> prod_lista, Tela_comanda tela_comanda) {
        initComponents();
        lista_produtos = new ArrayList<Produto>();
        lista_produtos = prod_lista;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        refreshTable(lista_produtos);
        tela = tela_comanda;
        
    }

    private Tela_produtos() {
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

        jLabel1 = new javax.swing.JLabel();
        text_produto = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_produtos = new javax.swing.JTable();
        combobox_qtd = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Produtos");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PRODUTOS");

        text_produto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_produtoActionPerformed(evt);
            }
        });
        text_produto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_produtoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                text_produtoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                text_produtoKeyTyped(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Quantidade:");

        table_produtos.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        table_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Preço", "Código"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_produtos.setRowHeight(22);
        table_produtos.getTableHeader().setResizingAllowed(false);
        table_produtos.getTableHeader().setReorderingAllowed(false);
        table_produtos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_produtosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_produtos);
        if (table_produtos.getColumnModel().getColumnCount() > 0) {
            table_produtos.getColumnModel().getColumn(0).setResizable(false);
            table_produtos.getColumnModel().getColumn(1).setMinWidth(100);
            table_produtos.getColumnModel().getColumn(1).setPreferredWidth(100);
            table_produtos.getColumnModel().getColumn(1).setMaxWidth(100);
            table_produtos.getColumnModel().getColumn(2).setMinWidth(180);
            table_produtos.getColumnModel().getColumn(2).setPreferredWidth(180);
            table_produtos.getColumnModel().getColumn(2).setMaxWidth(180);
        }

        combobox_qtd.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        combobox_qtd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        combobox_qtd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                combobox_qtdMouseClicked(evt);
            }
        });
        combobox_qtd.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                combobox_qtdPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combobox_qtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(268, 268, 268)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addComponent(combobox_qtd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel2.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("*Clique duplo no item para adiciona-lo.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(text_produto, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(265, 265, 265))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(72, 72, 72)
                                .addComponent(jButton2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text_produto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4)))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_produtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_produtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_produtoActionPerformed
  private void refreshTable (List<Produto> lista_tabela){
      
    int row_counter=0;
    DefaultTableModel model = new DefaultTableModel();
    model = (DefaultTableModel) table_produtos.getModel();
    model.setRowCount(lista_tabela.size()); 
        for (int i =table_produtos.getRowCount()-1;i>=0;i--){
            table_produtos.setValueAt("", i, 0);
            table_produtos.setValueAt("", i, 1);
            table_produtos.setValueAt("", i, 2);
        }
        for (Produto p : lista_tabela){ 
               table_produtos.setValueAt(p.getNome(), row_counter, 0); 
               table_produtos.setValueAt("R$ "+p.getPreco(), row_counter, 1);
               table_produtos.setValueAt(p.getBar_code(), row_counter, 2);           
               row_counter++;        
        }    
        if (lista_tabela.size() < 1){
            table_produtos.setEnabled(false);
        }else{
            table_produtos.setEnabled(true);
        }
     
    }
  
    private void text_produtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_produtoKeyPressed
        
    }//GEN-LAST:event_text_produtoKeyPressed

    private void text_produtoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_produtoKeyTyped
        if(text_produto.getText()!=null){
            lista_table = new ArrayList<Produto>();
            for(Produto p : lista_produtos){
                if(p.getNome().toLowerCase().contains(text_produto.getText().toLowerCase())){
                    lista_table.add(p);
                }
            }
            refreshTable(lista_table);
        }
    }//GEN-LAST:event_text_produtoKeyTyped

    private void table_produtosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_produtosMouseClicked
        JTable table =(JTable) evt.getSource();
        Point p = evt.getPoint();
        text_produto.requestFocus();
        if(table_produtos.isEnabled()){
            if (evt.getClickCount() == 2) {
                int row = table.getSelectedRow();
                tela.set_combo_qtd((String) combobox_qtd.getSelectedItem());
                tela.add_Produto(String.valueOf(table_produtos.getValueAt(row, 2)));
                tela.setEnabled(true);
                tela.setVisible(true);
                this.dispose();
            }
        }
            
    }//GEN-LAST:event_table_produtosMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        tela.setEnabled(true);
        tela.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void combobox_qtdPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_combobox_qtdPopupMenuWillBecomeInvisible
        text_produto.requestFocus();
    }//GEN-LAST:event_combobox_qtdPopupMenuWillBecomeInvisible

    private void combobox_qtdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_combobox_qtdMouseClicked
        text_produto.requestFocus();
    }//GEN-LAST:event_combobox_qtdMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void text_produtoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_produtoKeyReleased
        if(text_produto.getText()!=null){
            lista_table = new ArrayList<Produto>();
            for(Produto p : lista_produtos){
                if(p.getNome().toLowerCase().contains(text_produto.getText().toLowerCase())){
                    lista_table.add(p);
                }
            }
            refreshTable(lista_table);
        }
    }//GEN-LAST:event_text_produtoKeyReleased

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
            java.util.logging.Logger.getLogger(Tela_produtos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_produtos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_produtos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_produtos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_produtos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combobox_qtd;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_produtos;
    private javax.swing.JTextField text_produto;
    // End of variables declaration//GEN-END:variables
}