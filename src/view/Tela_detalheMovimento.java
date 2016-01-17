/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Classes.Comanda;
import Classes.Consumo;
import Classes.Data_transfer;
import Classes.Produto;
import DAO.ComandaDAO;
import DAO.ConsumoDAO;
import DAO.DAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PedroMazarini
 */
public class Tela_detalheMovimento extends javax.swing.JFrame {

    /**
     * Creates new form Tela_detalheMovimento
     */
    Data_transfer transfer_data;
    List<Produto> lista_produtos;
    List<Consumo> lista_consumo;
    Tela_Caixa telaCaixa;
    public Tela_detalheMovimento(int idComanda, Data_transfer data_transfer, String responsavel, Tela_Caixa tela_caixa) throws SQLException {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        transfer_data = new Data_transfer();
        transfer_data = data_transfer;
        label_responsavel.setText(responsavel);
        telaCaixa = tela_caixa;
        buscaComanda(idComanda);
        refreshTable();
    }

    private Tela_detalheMovimento() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void refreshTable (){
        
        lista_produtos = new ArrayList<Produto>();
        lista_produtos = transfer_data.getLista_produtos();
        DAO dao;
        float total=0;
        int row_counter =0;
        lista_consumo = new ArrayList<Consumo>();
        try {
            dao = new ConsumoDAO();
            lista_consumo = dao.listarConsumo(transfer_data.getComanda().getId());
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) table_consumo.getModel();
            model.setRowCount(lista_consumo.size()); 
            for (Consumo c : lista_consumo){ 
              
               for (Produto p : lista_produtos){ 
                   if(p.getId_produto()==c.getIdproduto()){
                      if(p.getPorkilo()==1){
                          table_consumo.setValueAt(String.format("%.3f",c.getQuantidade())+"Kg", row_counter, 1);
                          table_consumo.setValueAt("R$ "+String.format("%.2f",p.getPreco())+"/Kg", row_counter, 3);
                      } else{
                          table_consumo.setValueAt(String.format("%.0f",c.getQuantidade()), row_counter, 1);
                          table_consumo.setValueAt("R$ "+String.format("%.2f",p.getPreco()), row_counter, 3);
                      }
                      table_consumo.setValueAt(p.getNome(), row_counter, 0);                       
                      String hora = new SimpleDateFormat("HH:mm").format(c.getHr_consumo());
                      table_consumo.setValueAt(hora, row_counter, 2);                      
                      table_consumo.setValueAt("R$ "+String.format("%.2f",(p.getPreco()*c.getQuantidade())), row_counter, 4);
                      total = total +(Float.valueOf(c.getQuantidade())*p.getPreco());                     
                      label_total.setText("R$ "+ String.format("%.2f",total));
                      row_counter++;
                   }
               }
                    
               
            }
             
        } catch (SQLException ex) {
            Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscaComanda(int idComanda) throws SQLException{
        ComandaDAO comandaDAO = new ComandaDAO();
        Comanda comanda = new Comanda();
        try {
            comanda = comandaDAO.buscarID(idComanda);
            label_codigo.setText(String.valueOf(comanda.getBar_code()));
            label_nome.setText(comanda.getNome());
            String chegada = new SimpleDateFormat("HH:mm").format(comanda.getChegada());
            label_chegada.setText(chegada);
            String saida = new SimpleDateFormat("HH:mm").format(comanda.getSaida());
            label_saida.setText(saida);
            label_pagamento.setText(comanda.getPagamento());
            transfer_data.setComanda(comanda);
            label_desconto.setText("R$ "+ String.format("%.2f",comanda.getDesconto()));
            label_liquido.setText("R$ "+ String.format("%.2f",comanda.getTotal()-comanda.getDesconto()));
            
            } catch (SQLException ex) {
            Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        label_codigo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        label_chegada = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        label_pagamento = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        label_nome = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        label_saida = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_consumo = new javax.swing.JTable();
        label_codigo1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        label_total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        label_desconto = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        label_liquido = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        label_responsavel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes da Comanda");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Detalhes da Comanda");

        label_codigo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_codigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_codigo.setText("Nº: 1111");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        label_chegada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_chegada.setText("10:00");

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel8.setText("Nome:");

        label_pagamento.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_pagamento.setText("Dinheiro");

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel9.setText("Chegada:");

        label_nome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_nome.setText("Pedro");

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel10.setText("Pagamento:");

        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel11.setText("Saída:");

        label_saida.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_saida.setText("16:20");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_nome)
                    .addComponent(label_pagamento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_chegada)
                    .addComponent(label_saida))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label_nome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_pagamento))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label_chegada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_saida))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        table_consumo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        table_consumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produto", "Qtd", "Hr. Pedido", "Preço Unit.", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_consumo.setRowHeight(25);
        table_consumo.getTableHeader().setResizingAllowed(false);
        table_consumo.getTableHeader().setReorderingAllowed(false);
        table_consumo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_consumoMouseClicked(evt);
            }
        });
        table_consumo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                table_consumoFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(table_consumo);
        if (table_consumo.getColumnModel().getColumnCount() > 0) {
            table_consumo.getColumnModel().getColumn(0).setMinWidth(420);
            table_consumo.getColumnModel().getColumn(0).setPreferredWidth(420);
            table_consumo.getColumnModel().getColumn(0).setMaxWidth(420);
            table_consumo.getColumnModel().getColumn(1).setMinWidth(100);
            table_consumo.getColumnModel().getColumn(1).setPreferredWidth(100);
            table_consumo.getColumnModel().getColumn(1).setMaxWidth(100);
            table_consumo.getColumnModel().getColumn(2).setMinWidth(100);
            table_consumo.getColumnModel().getColumn(2).setPreferredWidth(100);
            table_consumo.getColumnModel().getColumn(2).setMaxWidth(100);
            table_consumo.getColumnModel().getColumn(3).setMinWidth(150);
            table_consumo.getColumnModel().getColumn(3).setPreferredWidth(150);
            table_consumo.getColumnModel().getColumn(3).setMaxWidth(150);
            table_consumo.getColumnModel().getColumn(4).setMinWidth(120);
            table_consumo.getColumnModel().getColumn(4).setPreferredWidth(120);
            table_consumo.getColumnModel().getColumn(4).setMaxWidth(120);
        }

        label_codigo1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_codigo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_codigo1.setText("Consumo:");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("TOTAL: ");

        label_total.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        label_total.setText("R$10.00");

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Desconto:");

        label_desconto.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        label_desconto.setText("R$10.00");

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Líquido:");

        label_liquido.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        label_liquido.setText("R$10.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                    .addComponent(label_codigo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                .addComponent(label_total, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(44, 44, 44)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(label_desconto, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                                    .addComponent(label_liquido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label_codigo1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(label_total))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(label_desconto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(label_liquido))
                .addGap(39, 39, 39))
        );

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setText("Responsável:");

        label_responsavel.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        label_responsavel.setText("Rick");

        jButton1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/voltar.png"))); // NOI18N
        jButton1.setText("Voltar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_codigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_responsavel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(366, 366, 366)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_codigo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(label_responsavel))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_consumoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_consumoMouseClicked
        
    }//GEN-LAST:event_table_consumoMouseClicked

    private void table_consumoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_consumoFocusGained
        
    }//GEN-LAST:event_table_consumoFocusGained

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        telaCaixa.setVisible(true);
        
    }//GEN-LAST:event_formWindowClosed

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
            java.util.logging.Logger.getLogger(Tela_detalheMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_detalheMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_detalheMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_detalheMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_detalheMovimento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel label_chegada;
    private javax.swing.JLabel label_codigo;
    private javax.swing.JLabel label_codigo1;
    private javax.swing.JLabel label_desconto;
    private javax.swing.JLabel label_liquido;
    private javax.swing.JLabel label_nome;
    private javax.swing.JLabel label_pagamento;
    private javax.swing.JLabel label_responsavel;
    private javax.swing.JLabel label_saida;
    private javax.swing.JLabel label_total;
    private javax.swing.JTable table_consumo;
    // End of variables declaration//GEN-END:variables
}
