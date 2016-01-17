/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Classes.Consumo;
import Classes.Data_transfer;
import Classes.Produto;
import Classes.Usuario;
import DAO.ConsumoDAO;
import DAO.DAO;
import DAO.ProdutoDAO;
import DAO.UsuarioDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
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
public class Tela_editaProdutos extends javax.swing.JFrame {

    /**
     * Creates new form Tela_editaProdutos
     */
    List<Produto> lista_produtos;
    List<Produto> lista_table;
    List<Usuario> lista_usuarios;
    Data_transfer data_transfer;
    public Tela_editaProdutos(Data_transfer transfer_data) {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        data_transfer = new Data_transfer();
        data_transfer= transfer_data;
        buttonGroup1.add(radio_kilograma);
        buttonGroup1.add(radio_unidade);
        lista_produtos = data_transfer.getLista_produtos();
        lista_table = new ArrayList<>();
        initiateListaTable();
        bloqueia_tela();
    }

    private Tela_editaProdutos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void refreshTable (List<Produto> lista_tabela){
        
        int row_counter =0;
            DefaultTableModel model = new DefaultTableModel();
            model = (DefaultTableModel) table_produtos.getModel();
            model.setRowCount(lista_tabela.size()); 
            for (Produto p : lista_tabela){ 
              table_produtos.setValueAt(p.getNome(), row_counter, 0);   
              table_produtos.setValueAt(String.format("%.2f",p.getPreco()), row_counter, 1);  
              table_produtos.setValueAt(p.getBar_code(), row_counter, 2);  
              if(p.getPorkilo()==1){
                table_produtos.setValueAt("Kilograma", row_counter, 3); 
                String qtd = (String.format("%.3f"+"Kg",p.getQuantidade()));
                table_produtos.setValueAt(qtd, row_counter, 4);
              }else{
                table_produtos.setValueAt("Unidade", row_counter, 3);  
                String qtd = (String.format("%.0f",p.getQuantidade()));
                table_produtos.setValueAt(qtd, row_counter, 4);
              }
              
              data_transfer.setLista_produtos(lista_produtos);
              
              row_counter++; 
            }
          
        
    }
    
    public void addProduto (){
        
        
        if (verificaDigitos()){
            if(verificaProdutos(text_codigo.getText())){

                DAO dao;
                Produto produto = new Produto();
                produto.setBar_code(text_codigo.getText());
                produto.setNome(text_nome.getText());
                produto.setPreco(Float.valueOf(text_preco.getText()));
                produto.setQuantidade(Float.valueOf(text_qtd.getText()));
                if(radio_kilograma.isSelected()){
                    produto.setPorkilo(1);
                }else{
                    produto.setPorkilo(0);
                }
                try {
                    dao = new ProdutoDAO();
                    dao.salvar(produto);    
                    lista_produtos.add(produto);
                    initiateListaTable();
                    refreshTable(lista_produtos);
                    text_codigo.setText("");
                    text_nome.setText("");
                    text_preco.setText("");
                    text_produto.requestFocus();

                } catch (SQLException ex) {
                    Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "Código ja utilizado por outro produto!","ERRO",
                             JOptionPane.ERROR_MESSAGE);
                text_codigo.setText("");
                text_codigo.requestFocus();
            }
        }
    }
    
    public boolean verificaDigitos(){
            try{
                float preco = Float.valueOf(text_preco.getText());
            }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(rootPane, "Preço inválido, digite apenas números e ponto!","ERRO",
                         JOptionPane.ERROR_MESSAGE);
            text_preco.setText("");
            text_preco.requestFocus();
            return false;
        }
            try{
                int codigo = Integer.valueOf(text_codigo.getText());
            }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(rootPane, "Código inválido, digite apenas números!","ERRO",
                         JOptionPane.ERROR_MESSAGE);
            text_codigo.setText("");
            text_codigo.requestFocus();
            return false;
        }
            try{
                float qtd = Float.valueOf(text_qtd.getText());
            }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(rootPane, "Quantidade inválida, digite apenas números e ponto!","ERRO",
                         JOptionPane.ERROR_MESSAGE);
            text_qtd.setText("");
            text_qtd.requestFocus();
            return false;
        }
        return true;
    }
    
    public void initiateListaTable(){
        for(Produto p : lista_produtos){
            lista_table.add(p);
            }
    }
    public boolean verificaProdutos(String codigo){
        for(Produto prod : lista_produtos){
            if(codigo.equals(prod.getBar_code()))return false;
        }
        return true;
    }
    
    public void bloqueia_tela(){
         boolean autorizado = false;     
            
            lista_usuarios = new ArrayList<Usuario>();
            DAO dao;
            while (autorizado==false){
                String cod_atendente= JOptionPane.showInputDialog(rootPane, "Gerente:", "Login", JOptionPane.QUESTION_MESSAGE);
                try {
                    dao = new UsuarioDAO();
                    lista_usuarios = dao.listarTodos();

                    for (Usuario u : lista_usuarios)
                    {
                       if ((u.getCodigo().equals(cod_atendente))&&(u.getGerente()==1)){
                           autorizado = true;
                           break;
                       }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (autorizado== true){  
                    text_nome.requestFocus();
                    refreshTable(lista_produtos);

                }else{
                    JOptionPane.showMessageDialog(rootPane, "Acesso Não Autorizado!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
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
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        radio_unidade = new javax.swing.JRadioButton();
        radio_kilograma = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_produtos = new javax.swing.JTable();
        text_codigo = new javax.swing.JTextField();
        text_nome = new javax.swing.JTextField();
        text_preco = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        text_produto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        text_qtd = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Produtos");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setText("*Clique duplo para editar produtos.");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Medida", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Rounded MT Bold", 0, 12))); // NOI18N
        jPanel1.setName(""); // NOI18N

        radio_unidade.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        radio_unidade.setSelected(true);
        radio_unidade.setText("Und");
        radio_unidade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radio_unidadeMouseClicked(evt);
            }
        });
        radio_unidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_unidadeActionPerformed(evt);
            }
        });

        radio_kilograma.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        radio_kilograma.setText("Kg");
        radio_kilograma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radio_kilogramaMouseClicked(evt);
            }
        });
        radio_kilograma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_kilogramaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radio_kilograma)
                    .addComponent(radio_unidade))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radio_unidade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radio_kilograma)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Código:");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel3.setText("Descrição: ");

        jButton1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/voltar.png"))); // NOI18N
        jButton1.setText("Voltar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/confirm2.png"))); // NOI18N
        jButton2.setText("Cadastrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Editar Produtos");

        table_produtos.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        table_produtos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Preço", "Código", "Medida", "Quantidade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_produtos.setRowHeight(25);
        table_produtos.getTableHeader().setResizingAllowed(false);
        table_produtos.getTableHeader().setReorderingAllowed(false);
        table_produtos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_produtosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_produtos);
        if (table_produtos.getColumnModel().getColumnCount() > 0) {
            table_produtos.getColumnModel().getColumn(0).setMinWidth(300);
            table_produtos.getColumnModel().getColumn(0).setPreferredWidth(300);
            table_produtos.getColumnModel().getColumn(0).setMaxWidth(300);
            table_produtos.getColumnModel().getColumn(1).setResizable(false);
            table_produtos.getColumnModel().getColumn(2).setMinWidth(150);
            table_produtos.getColumnModel().getColumn(2).setPreferredWidth(150);
            table_produtos.getColumnModel().getColumn(2).setMaxWidth(150);
            table_produtos.getColumnModel().getColumn(3).setResizable(false);
        }

        text_codigo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        text_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_codigoKeyPressed(evt);
            }
        });

        text_nome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N

        text_preco.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        text_preco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_precoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel4.setText("Preço:");

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Filtrar: ");

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

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel5.setText("Quantidade:");

        text_qtd.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(text_nome))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(76, 76, 76)
                                                .addComponent(text_preco, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel5))
                                        .addGap(17, 17, 17)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_qtd, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(text_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_produto, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(jButton2)))
                        .addGap(0, 243, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(text_nome))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(text_preco, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(text_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(text_qtd, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(52, 52, 52)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(text_produto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radio_unidadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radio_unidadeMouseClicked

    }//GEN-LAST:event_radio_unidadeMouseClicked

    private void radio_unidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_unidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radio_unidadeActionPerformed

    private void radio_kilogramaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radio_kilogramaMouseClicked

    }//GEN-LAST:event_radio_kilogramaMouseClicked

    private void radio_kilogramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_kilogramaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radio_kilogramaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if((!text_codigo.getText().isEmpty())&&(!text_nome.getText().isEmpty())&&(!text_preco.getText().isEmpty())){
            addProduto();
        }else{
            JOptionPane.showMessageDialog(rootPane, "Preencha os campos corretamente!","ERRO",
                JOptionPane.ERROR_MESSAGE);
            text_nome.requestFocus();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void table_produtosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_produtosMouseClicked

        if (evt.getClickCount() == 2) {
            int row= table_produtos.getSelectedRow();
            for(Produto prod : lista_table){
                if (prod.getBar_code().equals(table_produtos.getValueAt(row, 2))){
                    Object[] options = {"Descrição","Preço","Quantidade","Excluir"};
                    int resposta = JOptionPane.showOptionDialog(rootPane, "Selecione a opção para editar! \nDescrição: "+prod.getNome()
                            , "Editar item", 0, 3, null, options, options[0]);
                    if(resposta==0){
                        try {
                            
                            String descricao = (JOptionPane.showInputDialog(rootPane, "Editar descrição:", prod.getNome()));  
                            if (descricao!=null){
                                ProdutoDAO produtoDAO = new ProdutoDAO();
                                prod.setNome(descricao);
                                produtoDAO.alterarProduto(prod);
                                for(Produto p: lista_produtos){
                                    if(p.getId_produto()==prod.getId_produto()){
                                        p.setNome(descricao);
                                    }
                                }
                                refreshTable(lista_produtos);
                                text_produto.setText("");
                            }else{
                                JOptionPane.showMessageDialog(rootPane, "Descrição inválida!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
                            }
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(Tela_usuarios.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(resposta==1){

                        try {
                            String precoAtual = String.format("%.2f",prod.getPreco());
                            String preco = (JOptionPane.showInputDialog(rootPane, "Editar preço:", precoAtual));  
                            if ((preco!=null)&&(verificaFloat(preco))){
                                ProdutoDAO produtoDAO = new ProdutoDAO();
                                prod.setPreco(Float.valueOf(preco));
                                produtoDAO.alterarProduto(prod);
                                for(Produto p: lista_produtos){
                                    if(p.getId_produto()==prod.getId_produto()){
                                        p.setPreco(Float.valueOf(preco));
                                    }
                                }
                                refreshTable(lista_produtos);
                                text_produto.setText("");
                            }else{
                                JOptionPane.showMessageDialog(rootPane, "Valor inválido!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
                            }
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(Tela_usuarios.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(resposta==2){

                       try {
                            String qtd = (JOptionPane.showInputDialog(rootPane, "Editar quantidade:", prod.getQuantidade()));  
                            if ((qtd!=null)&&(verificaFloat(qtd))){
                                ProdutoDAO produtoDAO = new ProdutoDAO();
                                prod.setQuantidade(Float.valueOf(qtd));
                                produtoDAO.alterarProduto(prod);
                                for(Produto p: lista_produtos){
                                    if(p.getId_produto()==prod.getId_produto()){
                                        p.setQuantidade(Float.valueOf(qtd));
                                    }
                                }
                                refreshTable(lista_produtos);
                                text_produto.setText("");
                            }else{
                                JOptionPane.showMessageDialog(rootPane, "Valor inválido!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
                            }
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(Tela_usuarios.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(resposta==3){

                        try {
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            produtoDAO.remover_produto(prod.getBar_code());
                            lista_produtos.remove(prod);
                            refreshTable(lista_produtos);
                            text_produto.setText("");
                        } catch (SQLException ex) {
                            Logger.getLogger(Tela_usuarios.class.getName()).log(Level.SEVERE, null, ex);
                        }    
                    }
                }
            }
        }
    }//GEN-LAST:event_table_produtosMouseClicked

    private boolean verificaFloat(String valor){
        try{
            float teste = Float.valueOf(valor);
            return true;
        } catch (NumberFormatException ex) {
              return false;
            }  
        
    }
    private void text_codigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_codigoKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            if((!text_codigo.getText().isEmpty())&&(!text_nome.getText().isEmpty())&&(!text_preco.getText().isEmpty())){
                addProduto();
            }else{
                JOptionPane.showMessageDialog(rootPane, "Preencha os campos corretamente!","ERRO",
                    JOptionPane.ERROR_MESSAGE);
                text_nome.requestFocus();
            }
        }
    }//GEN-LAST:event_text_codigoKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        data_transfer.getTela_login().setEnabled(true);
        data_transfer.getTela_login().setVisible(true);
        this.setAlwaysOnTop(false);
        data_transfer.getTela_login().setListaProdutos(lista_produtos);
    }//GEN-LAST:event_formWindowClosed

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

    private void text_produtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_produtoKeyPressed
        if(text_produto.getText()!=null){
            lista_table = new ArrayList<Produto>();
            for(Produto p : lista_produtos){
                if(p.getNome().toLowerCase().contains(text_produto.getText().toLowerCase())){
                    lista_table.add(p);
                }
            }
            refreshTable(lista_table);
        }
    }//GEN-LAST:event_text_produtoKeyPressed

    private void text_precoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_precoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_precoActionPerformed

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
            java.util.logging.Logger.getLogger(Tela_editaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_editaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_editaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_editaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_editaProdutos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton radio_kilograma;
    private javax.swing.JRadioButton radio_unidade;
    private javax.swing.JTable table_produtos;
    private javax.swing.JTextField text_codigo;
    private javax.swing.JTextField text_nome;
    private javax.swing.JTextField text_preco;
    private javax.swing.JTextField text_produto;
    private javax.swing.JTextField text_qtd;
    // End of variables declaration//GEN-END:variables
}
