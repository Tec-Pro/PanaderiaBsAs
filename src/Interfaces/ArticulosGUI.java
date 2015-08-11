package Interfaces;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author joako
 */
public class ArticulosGUI extends javax.swing.JInternalFrame {

    private final DefaultTableModel tablaArticulosDefault;
    private BufferedImage imagen;

    /**
     * Creates new form ArticulosGUI
     */
    public ArticulosGUI() {
        initComponents();
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        inhabilitarCampos();
        tablaArticulosDefault = (DefaultTableModel) tablaArticulos.getModel();

    }

    public void limpiarPantalla() {

        txtNombre.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        getTxtPrecioCompra().setText("");
        getTxtPrecio().setText("");
        getTxtStock().setText("");
        getTxtStockMinimo().setText("");
        setImagenPorDefecto();
        boxTipo.setSelectedIndex(0);
    }

    public void habilitarGuardarCancelar() {
        habilitarCampos();
        btnPorcentaje.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnNuevo.setText("Guardar");
        btnNuevo.setActionCommand("GUARDAR");
        btnEliminar.setText("Cancelar");
        btnEliminar.setActionCommand("CANCELAR");
    }

    public void limpiarPanelImagenProducto() {
        pnlImagenProducto.setIcon(null);
        pnlImagenProducto.revalidate();
    }

    public void habilitarNuevo() {
        limpiarPantalla();
        btnNuevo.setText("Nuevo");
        btnNuevo.setActionCommand("NUEVO");
        btnPorcentaje.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setText("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR");
        btnEliminar.setEnabled(false);
        inhabilitarCampos();
    }

    public void habilitarCampos() {
        boxTipo.setEnabled(true);
        txtNombre.setEnabled(true);
        txtNombre.setEditable(true);
        txtCodigo.setEnabled(true);
        txtDescripcion.setEnabled(true);
        txtDescripcion.setEditable(true);
        getTxtStock().setEnabled(true);
        getTxtStock().setEditable(true);
        getTxtStockMinimo().setEnabled(true);
        getTxtStockMinimo().setEditable(true);
        getTxtPrecioCompra().setEnabled(true);
        getTxtPrecioCompra().setEditable(true);
        getTxtPrecio().setEnabled(true);
        getBtnCargarImagen().setEnabled(true);
        getBtnEliminarImagen().setEnabled(true);
    }

    private void inhabilitarCampos() {
        boxTipo.setEnabled(false);
        txtNombre.setEditable(false);
        txtNombre.setEnabled(false);
        txtCodigo.setEnabled(false);
        txtDescripcion.setEditable(false);
        txtDescripcion.setEnabled(false);
        getTxtStock().setEnabled(false);
        getTxtStockMinimo().setEnabled(false);
        getTxtPrecio().setEnabled(false);
        getTxtPrecioCompra().setEnabled(false);
        setImagenPorDefecto();
        getBtnCargarImagen().setEnabled(false);
        getBtnEliminarImagen().setEnabled(false);
    }

    public void setActionListener(ActionListener ac) {
        btnNuevo.addActionListener(ac);
        btnModificar.addActionListener(ac);
        btnEliminar.addActionListener(ac);
        btnPorcentaje.addActionListener(ac);
        getBtnCargarImagen().addActionListener(ac);
        getBtnEliminarImagen().addActionListener(ac);
    }

    public void setImagenPorDefecto() {
        pnlImagenProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sin_imagen.jpg")));
        imagen = null;
        pnlImagenProducto.repaint();

    }

    public void seleccionTabla() {
        btnModificar.setEnabled(true);
        btnNuevo.setText("Nuevo");
        btnNuevo.setActionCommand("NUEVO");
        btnEliminar.setEnabled(true);
        btnEliminar.setText("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR");
        boxTipo.setEnabled(false);
        txtCodigo.setEnabled(false);
        txtNombre.setEnabled(true);
        txtNombre.setEditable(false);
        getTxtPrecioCompra().setEnabled(true);
        getTxtPrecioCompra().setEnabled(false);
        txtDescripcion.setEnabled(true);
        txtDescripcion.setEditable(false);
    }

    public void setImagen(BufferedImage img) {
        if (img != null) {
            pnlImagenProducto.setIcon(new javax.swing.ImageIcon(img));
            imagen = img;
            pnlImagenProducto.repaint();
        } else {
            pnlImagenProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sin_imagen.jpg")));
            imagen = null;
            pnlImagenProducto.repaint();

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

        panelImage1 = new org.edisoncor.gui.panel.PanelImage();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaArticulos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBuscador = new javax.swing.JTextField();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        boxTipo = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtPrecioCompra = new javax.swing.JFormattedTextField();
        pnlImagenProducto = new org.edisoncor.gui.panel.PanelImage();
        jLabel7 = new javax.swing.JLabel();
        btnCargarImagen = new javax.swing.JButton();
        btnEliminarImagen = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtStock = new javax.swing.JFormattedTextField();
        txtStockMinimo = new javax.swing.JFormattedTextField();
        txtPrecioCompr = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        btnPorcentaje = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Gestion de Articulos");

        tablaArticulos.setAutoCreateRowSorter(true);
        tablaArticulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre", "Precio", "Precio Compra", "Tipo", "Stock", "Stock Min"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaArticulos.setFocusable(false);
        tablaArticulos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tablaArticulos);

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel1.setText("Buscar:");

        boxTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "PESABLE", "UNITARIO" }));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        jLabel2.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel3.setText("Codigo");

        jLabel4.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel4.setText("Precio Compra");

        jLabel5.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel5.setText("Tipo");

        jLabel6.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel6.setText("Descripcion");

        btnNuevo.setText("Nuevo");
        btnNuevo.setActionCommand("NUEVO");

        btnModificar.setText("Modificar");
        btnModificar.setActionCommand("MODIFICAR");

        btnEliminar.setText("Eliminar");
        btnEliminar.setActionCommand("ELIMINAR");

        txtPrecioCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        pnlImagenProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sin_imagen.jpg"))); // NOI18N
        pnlImagenProducto.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout pnlImagenProductoLayout = new javax.swing.GroupLayout(pnlImagenProducto);
        pnlImagenProducto.setLayout(pnlImagenProductoLayout);
        pnlImagenProductoLayout.setHorizontalGroup(
            pnlImagenProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlImagenProductoLayout.setVerticalGroup(
            pnlImagenProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 197, Short.MAX_VALUE)
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Imagen");

        btnCargarImagen.setText("Cargar");
        btnCargarImagen.setToolTipText("Cargar imagen de producto.");
        btnCargarImagen.setActionCommand("CARGAR IMAGEN");

        btnEliminarImagen.setText("Eliminar");
        btnEliminarImagen.setToolTipText("Eliminar imagen de producto.");
        btnEliminarImagen.setActionCommand("ELIMINAR IMAGEN");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Stock");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Stock mínimo");

        txtStock.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        txtStockMinimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtStockMinimo.setToolTipText("Stock mínimo del producto");

        txtPrecioCompr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Precio (kg - un)");

        btnPorcentaje.setText("Porcentaje");
        btnPorcentaje.setActionCommand("PORCENTAJE");
        btnPorcentaje.setEnabled(false);

        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelImage1);
        panelImage1.setLayout(panelImage1Layout);
        panelImage1Layout.setHorizontalGroup(
            panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelImage1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnCargarImagen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminarImagen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImage1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addGap(48, 48, 48)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                    .addGroup(panelImage1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addComponent(pnlImagenProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                            .addComponent(txtStockMinimo)
                            .addComponent(txtStock)
                            .addGroup(panelImage1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(btnPorcentaje)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecioCompr))
                            .addComponent(txtPrecioCompra)
                            .addComponent(txtNombre)
                            .addComponent(txtCodigo)
                            .addComponent(boxTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImage1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        panelImage1Layout.setVerticalGroup(
            panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImage1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImage1Layout.createSequentialGroup()
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(panelImage1Layout.createSequentialGroup()
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxTipo)
                            .addComponent(jLabel5))
                        .addGap(10, 10, 10)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPrecioCompra))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioCompr)
                            .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPorcentaje)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStock)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStockMinimo)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelImage1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCargarImagen)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarImagen))
                            .addComponent(pnlImagenProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNuevo)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelImage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelImage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox boxTipo;
    private javax.swing.JButton btnCargarImagen;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarImagen;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnPorcentaje;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.edisoncor.gui.panel.PanelImage panelImage1;
    private org.edisoncor.gui.panel.PanelImage pnlImagenProducto;
    private javax.swing.JTable tablaArticulos;
    private javax.swing.JTextField txtBuscador;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JFormattedTextField txtPrecioCompr;
    private javax.swing.JFormattedTextField txtPrecioCompra;
    private javax.swing.JFormattedTextField txtStock;
    private javax.swing.JFormattedTextField txtStockMinimo;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the boxTipo
     */
    public javax.swing.JComboBox getBoxTipo() {
        return boxTipo;
    }

    /**
     * @return the btnEliminar
     */
    public javax.swing.JButton getBtnEliminar() {
        return btnEliminar;
    }

    /**
     * @return the btnModificar
     */
    public javax.swing.JButton getBtnModificar() {
        return btnModificar;
    }

    /**
     * @return the btnNuevo
     */
    public javax.swing.JButton getBtnNuevo() {
        return btnNuevo;
    }

    /**
     * @return the jTable1
     */
    public javax.swing.JTable getTablaArticulos() {
        return tablaArticulos;
    }

    /**
     * @return the txtBuscador
     */
    public javax.swing.JTextField getTxtBuscador() {
        return txtBuscador;
    }

    /**
     * @return the txtCodigo
     */
    public javax.swing.JTextField getTxtCodigo() {
        return txtCodigo;
    }

    /**
     * @return the txtDescripcion
     */
    public javax.swing.JTextArea getTxtDescripcion() {
        return txtDescripcion;
    }

    /**
     * @return the txtNombre
     */
    public javax.swing.JTextField getTxtNombre() {
        return txtNombre;
    }

    /**
     * @return the txtPrecio
     */
    public javax.swing.JTextField getTxtPrecio() {
        return txtPrecioCompr;
    }

    /**
     * @return the pnlImagenProducto
     */
    public org.edisoncor.gui.panel.PanelImage getPnlImagenProducto() {
        return pnlImagenProducto;
    }

    /**
     * @return the tablaArticulosDefault
     */
    public DefaultTableModel getTablaArticulosDefault() {
        return tablaArticulosDefault;
    }

    /**
     * @return the imagen
     */
    public BufferedImage getImagen() {
        return imagen;
    }

    /**
     * @return the btnCargarImagen
     */
    public javax.swing.JButton getBtnCargarImagen() {
        return btnCargarImagen;
    }

    /**
     * @return the btnEliminarImagen
     */
    public javax.swing.JButton getBtnEliminarImagen() {
        return btnEliminarImagen;
    }

    /**
     * @return the txtStock
     */
    public javax.swing.JFormattedTextField getTxtStock() {
        return txtStock;
    }

    /**
     * @return the txtStockMinimo
     */
    public javax.swing.JFormattedTextField getTxtStockMinimo() {
        return txtStockMinimo;
    }

    /**
     * @return the txtPrecioCompra
     */
    public javax.swing.JFormattedTextField getTxtPrecioCompra() {
        return txtPrecioCompra;
    }

    /**
     * @return the porcentajeCheckbox
     */
    public javax.swing.JButton getBtnPorcentaje() {
        return btnPorcentaje;
    }
}
