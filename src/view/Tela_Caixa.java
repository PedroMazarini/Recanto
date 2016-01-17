/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Classes.Caixa;
import Classes.Data_transfer;
import Classes.MovimentoCaixa;
import Classes.Produto;
import Classes.Usuario;
import DAO.CaixaDAO;
import DAO.ComandaDAO;
import DAO.ConsumoDAO;
import DAO.DAO;
import DAO.MovimentoDAO;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PedroMazarini
 */
public class Tela_Caixa extends javax.swing.JFrame {

    /**
     * Creates new form Tela_Caixa
     */
    List<MovimentoCaixa> lista_movimento;
    List<MovimentoCaixa> movimento_entrada;
    List<Usuario> lista_usuarios;
    Caixa caixa;
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    Data_transfer data_transfer;
    String cod_responsavel;
    
    public Tela_Caixa(Data_transfer tranfer_data) {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        data_transfer = new Data_transfer();
        data_transfer = tranfer_data;
        lista_usuarios = new ArrayList<>();
        lista_usuarios = data_transfer.getLista_usuarios();
        bloqueia_tela();
        buscarCaixa();
        
    }

    private Tela_Caixa() {
        initComponents();
    }
    
    public void refreshTable (){
        
        lista_movimento = new ArrayList<MovimentoCaixa>();
        movimento_entrada = new ArrayList<>();
        MovimentoDAO movimentoDAO;
        int entrada=0;
        int retirada=0;
        int row_counter =0;
        int row_counter2 =0;
        float valor_entrada=0;
        float retirado=0;
        
        try {
            movimentoDAO = new MovimentoDAO();
            lista_movimento = movimentoDAO.listar(Date.valueOf(formato.format(dateChooserCombo1.getSelectedDate().getTime())));
            for(MovimentoCaixa mov : lista_movimento){
                if(mov.getValor()>=0){
                    entrada++;
                }else{
                    retirada++;
                }
            }
            DefaultTableModel model_entrada = new DefaultTableModel();
            model_entrada = (DefaultTableModel) table_entrada.getModel();
            model_entrada.setRowCount(entrada); 
            
            DefaultTableModel model_retirada = new DefaultTableModel();
            model_retirada = (DefaultTableModel) table_saida.getModel();
            model_retirada.setRowCount(retirada); 
            
            
            for (MovimentoCaixa mov : lista_movimento){ 
              
                   if(mov.getValor()>=0){
                       
                          table_entrada.setValueAt(mov.getDescricao(), row_counter, 0);
                          table_entrada.setValueAt(responsavel(mov), row_counter, 1);
                          String hora = new SimpleDateFormat("HH:mm").format(mov.getData());                      
                          table_entrada.setValueAt(hora, row_counter, 2);
                          table_entrada.setValueAt("R$ "+String.format("%.2f",mov.getValor()), row_counter, 3);
                          if((!mov.getDescricao().equals("Venda/Cartão"))){
                              valor_entrada = valor_entrada+mov.getValor();
                          }
                          movimento_entrada.add(mov);
                          row_counter++;
                    }else{
                          table_saida.setValueAt(mov.getDescricao(), row_counter2, 0);
                          table_saida.setValueAt(responsavel(mov), row_counter2, 1);
                          String hora = new SimpleDateFormat("HH:mm").format(mov.getData());                      
                          table_saida.setValueAt(hora, row_counter2, 2);
                          table_saida.setValueAt("R$ "+String.format("%.2f",mov.getValor()*-1), row_counter2, 3);
                          retirado = retirado + (mov.getValor()*-1);
                          row_counter2++;
                   }                     
                      
            }
            label_entrada.setText("R$ "+String.format("%.2f",valor_entrada));
            label_retirado.setText("R$ "+String.format("%.2f",retirado));
            label_saldo.setText("R$ "+String.format("%.2f",valor_entrada-retirado));
            
             
        } catch (SQLException ex) {
            Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void bloqueia_tela(){
         boolean autorizado = false;     
            
            while (autorizado==false){
                String cod_atendente= JOptionPane.showInputDialog(rootPane, "Gerente:", "Login", JOptionPane.QUESTION_MESSAGE);
                
                    for (Usuario u : lista_usuarios)
                    {
                       if ((u.getCodigo().equals(cod_atendente))&&(u.getGerente()==1)){
                           autorizado = true;
                           cod_responsavel= u.getCodigo();
                           break;
                       }

                    }
                
                if (autorizado== true){  
                    text_valor.requestFocus();
                    
                    refreshTable();

                }else{
                    JOptionPane.showMessageDialog(rootPane, "Acesso Não Autorizado!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
            }
            }
        
    }
    public String responsavel (MovimentoCaixa mov){
            for(Usuario u : lista_usuarios){
                if (u.getCodigo().equals(mov.getCod_atendente())){
                    return u.getNome();
                }
            }
        return null;
    }
    
    public void addMovimento(boolean positivo){
        try{
            float teste = Float.valueOf(text_valor.getText());
            DAO dao;
            MovimentoCaixa mov = new MovimentoCaixa();
            mov.setCod_atendente(cod_responsavel);            
            mov.setDescricao(text_descricao.getText());
            mov.setId_comanda(0);
            if(positivo){
                mov.setValor(Float.valueOf(text_valor.getText()));
            }else{
                mov.setValor(Float.valueOf(text_valor.getText())*-1);
            }
            try {
                dao = new MovimentoDAO();
                dao.salvar(mov);    
                lista_movimento.add(mov);
                refreshTable();
                text_descricao.setText("");
                text_valor.setText("");
                text_valor.requestFocus();
                alterarCaixa(mov.getValor());

            } catch (SQLException ex) {
                Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(rootPane, "Valor inválido, digite apenas números e ponto!","ERRO",
                         JOptionPane.ERROR_MESSAGE);
            text_valor.setText("");
            text_valor.requestFocus();
        }
    }
    
    public void buscarCaixa(){
       
            CaixaDAO dao;
            caixa = new Caixa();
            try {
                dao = new CaixaDAO();
                caixa = (Caixa) dao.buscar();   

            } catch (SQLException ex) {
                Logger.getLogger(Tela_login.class.getName()).log(Level.SEVERE, null, ex);
            }
            
       
            text_valor.setText("");
            text_valor.requestFocus();
        
    }
    public void alterarCaixa(Float valor){
      
            CaixaDAO dao;
            try {
                dao = new CaixaDAO();
                dao.alterar_total(caixa.getTotal()+valor);                    

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

        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_entrada = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_saida = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        text_valor = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        text_descricao = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        label_saldo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        label_retirado = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        label_entrada = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jLabel9 = new javax.swing.JLabel();

        dateChooserDialog1.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Movimento de Caixa");
    setResizable(false);
    setType(java.awt.Window.Type.POPUP);
    addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosed(java.awt.event.WindowEvent evt) {
            formWindowClosed(evt);
        }
    });

    jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jTabbedPane1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N

    table_entrada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
    table_entrada.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Descrição", "Responsável", "Hora", "Valor"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    table_entrada.setRowHeight(25);
    table_entrada.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            table_entradaMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(table_entrada);
    if (table_entrada.getColumnModel().getColumnCount() > 0) {
        table_entrada.getColumnModel().getColumn(0).setResizable(false);
        table_entrada.getColumnModel().getColumn(1).setResizable(false);
        table_entrada.getColumnModel().getColumn(2).setResizable(false);
        table_entrada.getColumnModel().getColumn(3).setResizable(false);
    }

    jTabbedPane1.addTab("Entrada", jScrollPane1);

    table_saida.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
    table_saida.setForeground(new java.awt.Color(255, 51, 51));
    table_saida.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Descrição", "Responsável", "Hora", "Valor"
        }
    ) {
        boolean[] canEdit = new boolean [] {
            false, false, false, false
        };

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }
    });
    table_saida.setRowHeight(25);
    jScrollPane2.setViewportView(table_saida);
    if (table_saida.getColumnModel().getColumnCount() > 0) {
        table_saida.getColumnModel().getColumn(0).setResizable(false);
        table_saida.getColumnModel().getColumn(1).setResizable(false);
        table_saida.getColumnModel().getColumn(2).setResizable(false);
        table_saida.getColumnModel().getColumn(3).setResizable(false);
    }

    jTabbedPane1.addTab("Saída", jScrollPane2);

    jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("Movimento de Caixa");

    jButton3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
    jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/voltar.png"))); // NOI18N
    jButton3.setText("Voltar");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });

    jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("Valor:");

    text_valor.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            text_valorActionPerformed(evt);
        }
    });

    jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
    jButton2.setForeground(new java.awt.Color(51, 153, 0));
    jButton2.setText("+ Adicionar Valor");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    jButton1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
    jButton1.setForeground(new java.awt.Color(255, 0, 51));
    jButton1.setText("- Retirar Valor");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Descrição:");

    text_descricao.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            text_descricaoActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jButton1)
                    .addGap(18, 18, 18)
                    .addComponent(jButton2))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                    .addGap(151, 151, 151)
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(text_valor, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(text_descricao, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4)
                .addComponent(text_valor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel5)
                .addComponent(text_descricao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(26, Short.MAX_VALUE))
    );

    jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel7.setText("(em caixa)");

    label_saldo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
    label_saldo.setForeground(new java.awt.Color(0, 153, 0));
    label_saldo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_saldo.setText("R$00.00");

    jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(0, 153, 0));
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setText("Saldo:");

    jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Entrada:");

    label_retirado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
    label_retirado.setForeground(new java.awt.Color(255, 0, 0));
    label_retirado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_retirado.setText("R$00.00");

    jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(255, 0, 0));
    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel3.setText("Retirado:");

    label_entrada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
    label_entrada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    label_entrada.setText("R$00.00");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel3)
                    .addGap(8, 8, 8)
                    .addComponent(label_retirado))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addGap(18, 18, 18)
                    .addComponent(label_entrada))
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_saldo))
                .addComponent(jLabel7))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(label_entrada))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(label_retirado)
                .addComponent(jLabel3))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(label_saldo)
                .addComponent(jLabel6))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7)
            .addContainerGap())
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
    jLabel8.setText("Selecione:");

    dateChooserCombo1.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dateChooserCombo1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null,
    (java.awt.Color)null));
    dateChooserCombo1.setCalendarPreferredSize(new java.awt.Dimension(350, 220));
    dateChooserCombo1.setFormat(2);
    dateChooserCombo1.setFieldFont(new java.awt.Font("Arial Rounded MT Bold", java.awt.Font.PLAIN, 21));
    dateChooserCombo1.setLocale(new java.util.Locale("pt", "BR", ""));
    dateChooserCombo1.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    dateChooserCombo1.setShowOneMonth(true);
    dateChooserCombo1.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
        public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
            dateChooserCombo1OnSelectionChange(evt);
        }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel8)
            .addGap(18, 18, 18)
            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8))
            .addContainerGap())
    );

    jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
    jLabel9.setText("*Clique duplo no movimento para detalhe.");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(386, 386, 386)
                    .addComponent(jButton3)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(32, 32, 32)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 12, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(161, 161, 161))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())))
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel9)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
                .addContainerGap()))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(60, 60, 60)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(29, 29, 29)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel9)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2)
                .addContainerGap(750, Short.MAX_VALUE)))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_valorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_valorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_valorActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String dia_selecionado =formato.format(dateChooserCombo1.getSelectedDate().getTime() );
        String hoje = formato.format( Calendar.getInstance().getTime());
        if((!text_valor.getText().isEmpty()&&(!text_descricao.getText().isEmpty()))){
            if(dia_selecionado.equals(hoje)){
                addMovimento(true);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Selecione a data atual para realizar a operação!","ERRO",
                JOptionPane.ERROR_MESSAGE);
                text_valor.requestFocus();
            }
            
        }else{
            JOptionPane.showMessageDialog(rootPane, "Preencha o valor corretamente!","ERRO",
                JOptionPane.ERROR_MESSAGE);
            text_valor.requestFocus();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String dia_selecionado =formato.format(dateChooserCombo1.getSelectedDate().getTime() );
        String hoje = formato.format( Calendar.getInstance().getTime());
        if((!text_valor.getText().isEmpty()&&(!text_descricao.getText().isEmpty()))){
            if(dia_selecionado.equals(hoje)){
                addMovimento(false);
            }else{
                JOptionPane.showMessageDialog(rootPane, "Selecione a data atual para realizar a operação!","ERRO",
                JOptionPane.ERROR_MESSAGE);
                text_valor.requestFocus();
            }
            
        }else{
            JOptionPane.showMessageDialog(rootPane, "Preencha o valor corretamente!","ERRO",
                JOptionPane.ERROR_MESSAGE);
            text_valor.requestFocus();
        }
              
              
    }//GEN-LAST:event_jButton1ActionPerformed

    private void text_descricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_descricaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_descricaoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        data_transfer.getTela_login().setEnabled(true);
        data_transfer.getTela_login().setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void dateChooserCombo1OnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo1OnSelectionChange
        refreshTable();
        text_valor.requestFocus();
    }//GEN-LAST:event_dateChooserCombo1OnSelectionChange

    private void table_entradaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_entradaMouseClicked
        Point p = evt.getPoint();
        int row = table_entrada.getSelectedRow();
        if (evt.getClickCount() == 2) {
            int idComanda = movimento_entrada.get(row).getId_comanda();
            String responsavel = (String) table_entrada.getValueAt(row, 1);
            if (idComanda!=0){
                this.hide();
                try {
                    Tela_detalheMovimento tela_detalhe = new Tela_detalheMovimento(idComanda, data_transfer, responsavel,this);
                    tela_detalhe.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Tela_Caixa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        
    }//GEN-LAST:event_table_entradaMouseClicked

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
            java.util.logging.Logger.getLogger(Tela_Caixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_Caixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_Caixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_Caixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_Caixa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel label_entrada;
    private javax.swing.JLabel label_retirado;
    private javax.swing.JLabel label_saldo;
    private javax.swing.JTable table_entrada;
    private javax.swing.JTable table_saida;
    private javax.swing.JTextField text_descricao;
    private javax.swing.JTextField text_valor;
    // End of variables declaration//GEN-END:variables
}
