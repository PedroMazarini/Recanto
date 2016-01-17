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
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PedroMazarini
 */
public class Tela_fecha_cliente extends javax.swing.JFrame {

    /**
     * Creates new form Tela_fecha_comanda
     */
    List<Consumo> lista_consumo;
    List<Consumo> lista_consumoTotal;
    List<Produto> lista_produtos;
    List<Comanda> comandas_aFechar;
    Data_transfer data_transfer;
    Tela_comanda tela_comanda;
    float total=0;
    boolean fechada;
    
    public Tela_fecha_cliente() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        
    }
    
    public void por_Pessoa(String porPessoa){
        label_porPessoa.setText(porPessoa);
    }
    
    public void initiate_variables(Tela_comanda comanda_tela,List<Consumo> consumo_list,Data_transfer dados){
        lista_consumo = new ArrayList<>();
        fechada=false;
        lista_consumo= consumo_list;
        data_transfer = new Data_transfer();
        data_transfer = dados;
        tela_comanda = comanda_tela;        
        lista_consumoTotal = new ArrayList<>();
        comandas_aFechar = new ArrayList<>();
        junta_consumo();
        refresh_table();
        add_fechaComanda(data_transfer.getComanda());
        
    }
    
    public void add_fechaComanda(Comanda comanda){
        comandas_aFechar.add(comanda);
    }
    
    public List<Comanda> comandas_aFechar(){
        return comandas_aFechar;
    }
    public void adicionar_comanda(List<Consumo> add_comanda){
        lista_consumo.addAll(add_comanda);
        lista_consumoTotal = new ArrayList<>();
        total=0;
        junta_consumo();
        refresh_table();
    }    
    
    private void junta_consumo(){
        for(Consumo c: lista_consumo){
            boolean existe=false;
            for(Consumo c_total: lista_consumoTotal){
                if(c.getIdproduto() == c_total.getIdproduto()) {
                  c_total.setQuantidade(c_total.getQuantidade()+c.getQuantidade());
                  existe = true;
                }                
            }
            if (!existe){
                Consumo novo_consumo = new Consumo();
                novo_consumo.setHr_consumo(c.getHr_consumo());
                novo_consumo.setIdcomanda(c.getIdcomanda());
                novo_consumo.setIdproduto(c.getIdproduto());
                novo_consumo.setQuantidade(c.getQuantidade());
                lista_consumoTotal.add(novo_consumo);
            }
            
        }
        
    }
    
    

    private void refresh_table() {
        lista_produtos = new ArrayList<Produto>();
        lista_produtos = data_transfer.getLista_produtos();        
        int row_counter =0;
            for (Consumo c : lista_consumoTotal){               
               for (Produto p : lista_produtos){ 
                   if(p.getId_produto()==c.getIdproduto()){
                      DefaultTableModel model = new DefaultTableModel();
                      model = (DefaultTableModel) table_consumo.getModel();
                      model.setRowCount(lista_consumoTotal.size()); 
                      if(p.getPorkilo()==1){
                          table_consumo.setValueAt(String.format("%.3f",c.getQuantidade())+"Kg", row_counter, 1);
                          table_consumo.setValueAt("R$ "+String.format("%.2f",p.getPreco())+"/Kg", row_counter, 2);
                      } else{
                          table_consumo.setValueAt(String.format("%.0f",c.getQuantidade()), row_counter, 1);
                          table_consumo.setValueAt("R$ "+String.format("%.2f",p.getPreco()), row_counter, 2);
                      }
                      table_consumo.setValueAt(p.getNome(), row_counter, 0); 
                      table_consumo.setValueAt("R$ "+String.format("%.2f",p.getPreco()*c.getQuantidade()), row_counter, 3);
                      total = total +(Float.valueOf(c.getQuantidade())*p.getPreco());                     
                      label_total.setText("R$ "+ String.format("%.2f",total));
                      row_counter++;
                   }
               }
                    
               
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        label_porPessoa = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_consumo = new javax.swing.JTable();
        label_total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        label_codigo = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        label_nome = new javax.swing.JLabel();
        label_chegada = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Comanda");
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        label_porPessoa.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel3.setText("Chegada:");

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel2.setText("Nome:");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        table_consumo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        table_consumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produto", "Qtd", "Preço Unit.", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_consumo.setEnabled(false);
        table_consumo.setRowHeight(25);
        table_consumo.getTableHeader().setResizingAllowed(false);
        table_consumo.getTableHeader().setReorderingAllowed(false);
        table_consumo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                table_consumoFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(table_consumo);
        if (table_consumo.getColumnModel().getColumnCount() > 0) {
            table_consumo.getColumnModel().getColumn(0).setMinWidth(350);
            table_consumo.getColumnModel().getColumn(0).setPreferredWidth(350);
            table_consumo.getColumnModel().getColumn(0).setMaxWidth(350);
            table_consumo.getColumnModel().getColumn(1).setMinWidth(100);
            table_consumo.getColumnModel().getColumn(1).setPreferredWidth(100);
            table_consumo.getColumnModel().getColumn(1).setMaxWidth(100);
            table_consumo.getColumnModel().getColumn(2).setMinWidth(130);
            table_consumo.getColumnModel().getColumn(2).setPreferredWidth(130);
            table_consumo.getColumnModel().getColumn(2).setMaxWidth(130);
        }

        label_total.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        label_total.setText("R$ 00,00");

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel4.setText("TOTAL:");

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("COMSUMO:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(425, 425, 425)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_total)))
                .addContainerGap())
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_total)
                    .addComponent(jLabel4)))
        );

        label_codigo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        label_codigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        label_nome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        label_chegada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FECHAR COMANDA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_chegada, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(label_porPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(label_codigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(label_chegada, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_porPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_consumoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_consumoFocusGained
        
    }//GEN-LAST:event_table_consumoFocusGained

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        
    }//GEN-LAST:event_formWindowClosed

   
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        label_codigo.setText(String.valueOf(data_transfer.getComanda().getBar_code()));
        label_nome.setText(data_transfer.getComanda().getNome());
        label_chegada.setText(String.valueOf(data_transfer.getChegada()));
        
    }//GEN-LAST:event_formWindowOpened

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
    }//GEN-LAST:event_formWindowStateChanged

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
            java.util.logging.Logger.getLogger(Tela_fecha_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_fecha_cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_chegada;
    private javax.swing.JLabel label_codigo;
    private javax.swing.JLabel label_nome;
    private javax.swing.JLabel label_porPessoa;
    private javax.swing.JLabel label_total;
    private javax.swing.JTable table_consumo;
    // End of variables declaration//GEN-END:variables

    
}
