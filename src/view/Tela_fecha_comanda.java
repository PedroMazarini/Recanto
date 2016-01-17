/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Classes.Comanda;
import Classes.Consumo;
import Classes.Data_transfer;
import Classes.MovimentoCaixa;
import Classes.Produto;
import DAO.ComandaDAO;
import DAO.DAO;
import DAO.MovimentoDAO;
import DAO.ProdutoDAO;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PedroMazarini
 */
public class Tela_fecha_comanda extends javax.swing.JFrame {

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
    Tela_fecha_cliente fecha_cliente;
    
    public Tela_fecha_comanda() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        buttonGroup1.add(radio_dinheiro);
        buttonGroup1.add(radio_cartao);
        
       
        
    }
    
    public static void showFechaComandaCliente( int screen, JFrame frame )
    {
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        
        if( screen > -1 && screen < gs.length )
        {
            gs[screen].setFullScreenWindow( frame );
        }
        else if( gs.length > 0 )
        {
            //gs[0].setFullScreenWindow( frame );
        }
        else
        {
            throw new RuntimeException( "No Screens Found" );
        }
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
        
        data_transfer.getTela_login().getFundo().hide();
        fecha_cliente = new Tela_fecha_cliente();
        fecha_cliente.initiate_variables(tela_comanda, lista_consumoTotal, data_transfer);        
        showFechaComandaCliente(1, fecha_cliente);
        
    }
    
    public void add_fechaComanda(Comanda comanda){
        comandas_aFechar.add(comanda);
    }
    
    public List<Comanda> comandas_aFechar(){
        return comandas_aFechar;
    }
    public void adicionar_comanda(List<Consumo> add_comanda){
        lista_consumo.addAll(add_comanda);
        total=0;
        lista_consumoTotal = new ArrayList<>();
        junta_consumo();
        refresh_table();
        fecha_cliente.comandas_aFechar = comandas_aFechar();
        fecha_cliente.adicionar_comanda(add_comanda);
    }    
    
    private void junta_consumo(){
        
        for(Consumo c: lista_consumo){
            boolean existe=false;
            for(int i=0; i< lista_consumoTotal.size();i++){
                
                if(c.getIdproduto() == lista_consumoTotal.get(i).getIdproduto()) {
                  lista_consumoTotal.get(i).setQuantidade(lista_consumoTotal.get(i).getQuantidade()+c.getQuantidade());                  
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
    
    private void fechar_commanda() throws SQLException{
        ComandaDAO comandaDAO = new ComandaDAO(); 
       try{
           float desconto = Float.valueOf(text_desconto.getText());
           if(Float.valueOf(text_recebido.getText())>=total-desconto){
               
               float troco = Float.valueOf(text_recebido.getText())-total+desconto;               
               if(radio_dinheiro.isSelected()){
                   comandaDAO.fechar_comanda(comandas_aFechar,"Dinheiro",desconto);
                   registraMovimento("Venda/Dinheiro",desconto);
                   JOptionPane.showMessageDialog(rootPane, "Comanda fechada!\n Troco: R$"+ String.format("%.2f",troco),"Fechada",
                          JOptionPane.INFORMATION_MESSAGE);
               }else{
                   comandaDAO.fechar_comanda(comandas_aFechar,"Cartão",desconto);
                   registraMovimento("Venda/Cartão",desconto);
                   JOptionPane.showMessageDialog(rootPane, "Comanda fechada!","Fechada",
                          JOptionPane.INFORMATION_MESSAGE);
               }
               ProdutoDAO produtoDAO = new ProdutoDAO();
               for(Consumo c : lista_consumoTotal){
                   for(Produto p : lista_produtos){
                       if(c.getIdproduto()==p.getId_produto()){
                           p.setQuantidade(p.getQuantidade()-c.getQuantidade());
                           produtoDAO.alterarProduto(p);
                       }
                   }
               }
               
               tela_comanda.dispose();
               data_transfer.getTela_login().initiate_table();
               fechada = true;
               this.dispose();
            }else{
               JOptionPane.showMessageDialog(rootPane, "Valor incorreto, digite novamente!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
                text_recebido.requestFocus();
                text_recebido.setText("");
           }
           
       }catch(NumberFormatException ex){
           JOptionPane.showMessageDialog(rootPane, "Valor incorreto, digite novamente!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
           text_recebido.requestFocus();
           text_recebido.setText("");
       }
              
    }

    private void registraMovimento(String descricao,float desconto) throws SQLException{
        DAO dao;
        dao = new MovimentoDAO();
        MovimentoCaixa movimentoCaixa = new MovimentoCaixa();
        try{
            for(Comanda c : comandas_aFechar){
                movimentoCaixa.setCod_atendente(data_transfer.getUsuario().getCodigo());
                movimentoCaixa.setDescricao(descricao);
                movimentoCaixa.setId_comanda(c.getId());
                movimentoCaixa.setValor(c.getTotal()-desconto);
                dao.salvar(movimentoCaixa);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Tela_fecha_comanda.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        label_nome = new javax.swing.JLabel();
        label_codigo = new javax.swing.JLabel();
        label_chegada = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_consumo = new javax.swing.JTable();
        label_total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radio_dinheiro = new javax.swing.JRadioButton();
        radio_cartao = new javax.swing.JRadioButton();
        text_recebido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        box_nPessoas = new javax.swing.JComboBox<>();
        label_porPessoa = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        text_desconto = new javax.swing.JTextField();
        slider = new javax.swing.JSlider();
        label_porcentagem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Comanda");
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
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

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FECHAR COMANDA");

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel2.setText("Nome:");

        label_nome.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        label_codigo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        label_codigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        label_chegada.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel3.setText("Chegada:");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(label_total))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setToolTipText("");
        jPanel2.setName(""); // NOI18N

        radio_dinheiro.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        radio_dinheiro.setSelected(true);
        radio_dinheiro.setText("Dinheiro");
        radio_dinheiro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radio_dinheiroMouseClicked(evt);
            }
        });
        radio_dinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_dinheiroActionPerformed(evt);
            }
        });

        radio_cartao.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        radio_cartao.setText("Cartão");
        radio_cartao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radio_cartaoMouseClicked(evt);
            }
        });
        radio_cartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_cartaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radio_cartao)
                    .addComponent(radio_dinheiro))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radio_dinheiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radio_cartao)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        text_recebido.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        text_recebido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_recebidoKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel5.setText("R$");

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel6.setText("Recebido:");

        jButton1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton1.setText("+ Adicionar Comanda");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel2.png"))); // NOI18N
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/confirm2.png"))); // NOI18N
        jButton3.setText("Confirmar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel8.setText("Nº Pessoas:");

        box_nPessoas.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        box_nPessoas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", " " }));
        box_nPessoas.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                box_nPessoasPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        box_nPessoas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                box_nPessoasItemStateChanged(evt);
            }
        });

        label_porPessoa.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel9.setText("Desconto:");

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel10.setText("R$");

        text_desconto.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        text_desconto.setText("0.00");
        text_desconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_descontoActionPerformed(evt);
            }
        });
        text_desconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                text_descontoKeyPressed(evt);
            }
        });

        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setValue(0);
        slider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sliderMouseDragged(evt);
            }
        });

        label_porcentagem.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        label_porcentagem.setText("0%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_chegada, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(box_nPessoas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label_porPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(label_porcentagem)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(text_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(slider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(text_recebido, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(label_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(208, 208, 208))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(55, 55, 55)
                .addComponent(jButton3)
                .addGap(191, 191, 191))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(label_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(label_chegada, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label_porPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(box_nPessoas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(jLabel10))
                            .addComponent(text_desconto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_porcentagem))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5))
                            .addComponent(text_recebido, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void table_consumoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_table_consumoFocusGained
        
    }//GEN-LAST:event_table_consumoFocusGained

    private void radio_dinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_dinheiroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radio_dinheiroActionPerformed

    private void radio_cartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_cartaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radio_cartaoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            fechar_commanda();
        } catch (SQLException ex) {
            Logger.getLogger(Tela_fecha_comanda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if(!fechada){
            tela_comanda.setVisible(true);
            tela_comanda.refreshTable();
        }
        fecha_cliente.dispose();
        data_transfer.getTela_login().showOnScreen(1, data_transfer.getTela_login().getFundo());
        
    }//GEN-LAST:event_formWindowClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        tela_comanda.setVisible(true);
        this.dispose();
        tela_comanda.refreshTable();
        
    }//GEN-LAST:event_jButton2ActionPerformed

   
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        text_recebido.requestFocus();
        label_codigo.setText(String.valueOf(data_transfer.getComanda().getBar_code()));
        label_nome.setText(data_transfer.getComanda().getNome());
        label_chegada.setText(String.valueOf(data_transfer.getChegada()));
    }//GEN-LAST:event_formWindowOpened

    private void radio_cartaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radio_cartaoMouseClicked
        text_recebido.requestFocus();           
    }//GEN-LAST:event_radio_cartaoMouseClicked

    private void radio_dinheiroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radio_dinheiroMouseClicked
        text_recebido.requestFocus();
    }//GEN-LAST:event_radio_dinheiroMouseClicked

    private void box_nPessoasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_box_nPessoasItemStateChanged
        text_recebido.requestFocus();
        if(box_nPessoas.getSelectedItem()!="1"){
            label_porPessoa.setText("R$ "+String.format("%.2f",total/(Integer.valueOf((String) box_nPessoas.getSelectedItem())))
            +" /Pessoa");
            fecha_cliente.por_Pessoa(label_porPessoa.getText());
        }else{
            label_porPessoa.setText("");
            fecha_cliente.por_Pessoa("");
        }
    }//GEN-LAST:event_box_nPessoasItemStateChanged

    private void box_nPessoasPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_box_nPessoasPopupMenuWillBecomeInvisible
        text_recebido.requestFocus();
    }//GEN-LAST:event_box_nPessoasPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(comandas_aFechar.size()==data_transfer.getTela_login().lista_comandasAberta.size()){            
            JOptionPane.showMessageDialog(rootPane, "Não há comandas para adicionar!","ERRO",
                     JOptionPane.ERROR_MESSAGE);
        }else{
            data_transfer.getTela_login().adicionar_comanda(this);
            this.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        text_recebido.requestFocus();
    }//GEN-LAST:event_formWindowStateChanged

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        text_recebido.requestFocus();
    }//GEN-LAST:event_jButton1MouseReleased

    private void text_recebidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_recebidoKeyPressed
       if (evt.getKeyCode()==KeyEvent.VK_ENTER){
           try {
               fechar_commanda();
           } catch (SQLException ex) {
               Logger.getLogger(Tela_fecha_comanda.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }//GEN-LAST:event_text_recebidoKeyPressed

    private void text_descontoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_descontoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_descontoKeyPressed

    private void sliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderMouseDragged
       
        label_porcentagem.setText(String.valueOf(slider.getValue())+"%");
        text_desconto.setText(String.format("%.2f", (total*Float.valueOf(slider.getValue())/100)));
        float desconto = total*Float.valueOf(slider.getValue())/100;
        label_total.setText("R$ "+String.format("%.2f", total-desconto));
        text_recebido.requestFocus();
    }//GEN-LAST:event_sliderMouseDragged

    private void text_descontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_descontoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_descontoActionPerformed

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
            java.util.logging.Logger.getLogger(Tela_fecha_comanda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_comanda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_comanda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela_fecha_comanda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela_fecha_comanda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_nPessoas;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel label_chegada;
    private javax.swing.JLabel label_codigo;
    private javax.swing.JLabel label_nome;
    private javax.swing.JLabel label_porPessoa;
    private javax.swing.JLabel label_porcentagem;
    private javax.swing.JLabel label_total;
    private javax.swing.JRadioButton radio_cartao;
    private javax.swing.JRadioButton radio_dinheiro;
    private javax.swing.JSlider slider;
    private javax.swing.JTable table_consumo;
    private javax.swing.JTextField text_desconto;
    private javax.swing.JTextField text_recebido;
    // End of variables declaration//GEN-END:variables

    
}
