/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.GestionArticulos;
import Interfaces.AplicacionGUI;
import Interfaces.ArticulosGUI;
import Interfaces.ProductosEscasosGUI;
import Modelos.Articulo;
import Utils.ImageFilter;
import Utils.ParserFloat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author joako
 */
public class ControladorArticulos implements ActionListener {

    private final ArticulosGUI articulosGui;
    private float porcentajeRecargo;
    private final GestionArticulos gestionArticulos;
    private boolean mod;

    public ControladorArticulos(ArticulosGUI artGui) {
        articulosGui = artGui;
        articulosGui.setActionListener(this);
        gestionArticulos = new GestionArticulos();
        cargarListaArt();
        articulosGui.getTablaArticulos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (articulosGui.getTablaArticulos().getSelectedRowCount() == 1) {
                    articulosGui.seleccionTabla();
                    cargarDatosElementoSeleccionado();
                } else {
                    articulosGui.limpiarPantalla();
                }
                mod = false;
            }

        });
        
        articulosGui.getBoxTipo().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(articulosGui.getBoxTipo().getSelectedItem() == "PESABLE"){
                    articulosGui.getTxtCodigo().setText("Codigo autogenerado");
                    articulosGui.getTxtCodigo().setEnabled(false);
                }else{
                   articulosGui.getTxtCodigo().setText("");
                   articulosGui.getTxtCodigo().setEnabled(true); 
                }
            }
        });
        articulosGui.getTxtBuscador().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                cargarListaArt(gestionArticulos.buscarArticulo(articulosGui.getTxtBuscador().getText()));
            }

        });

        articulosGui.getTxtPrecioCompra().addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent evt) {
                String txtPrecioCompra = articulosGui.getTxtPrecioCompra().getText();
                float precio_compra = 0.0f;
                if (!txtPrecioCompra.trim().isEmpty()) {
                    precio_compra = ParserFloat.stringToFloat(articulosGui.getTxtPrecioCompra().getText().trim());
                }
                float precio = (precio_compra + (precio_compra * ControladorPrincipal.getPorcentajeRecargo() / 100));
                articulosGui.getTxtPrecio().setText(ParserFloat.floatToString(precio));
            }
        }
        );

    }

    private void cargarDatosElementoSeleccionado() {
        abrirBase();
        int row = articulosGui.getTablaArticulos().getSelectedRow();
        String codigo = (String) articulosGui.getTablaArticulos().getValueAt(row, 0);
        Articulo a = Articulo.first("codigo = ?", codigo);
        articulosGui.getBoxTipo().setSelectedItem(a.getString("tipo"));
        //cambia el box se ejecuta el evento y lo vuelvo
        articulosGui.getTxtCodigo().setEnabled(false);
        articulosGui.getTxtCodigo().setText(a.getString("codigo"));
        articulosGui.getTxtDescripcion().setText(a.getString("descripcion"));
        articulosGui.getTxtNombre().setText(a.getString("nombre"));
        articulosGui.getTxtPrecio().setText(a.getFloat("precio").toString());
        articulosGui.getTxtPrecioCompra().setText(a.getFloat("precio_compra").toString());
        articulosGui.getTxtStock().setText(a.getFloat("stock").toString());
        articulosGui.getTxtStockMinimo().setText(a.getFloat("min_stock").toString());
        articulosGui.limpiarPanelImagenProducto();
        articulosGui.setImagen(gestionArticulos.getProductImage(codigo, "jpg"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(articulosGui.getBtnNuevo())) {
            //Se oprimió el botón nuevo para crear o guardar un artículo.
            switch (articulosGui.getBtnNuevo().getActionCommand()) {
                //Limpio la pantalla y habilito el guardado.
                case "NUEVO":
                    articulosGui.limpiarPantalla();
                    articulosGui.habilitarGuardarCancelar();
                    articulosGui.getTxtCodigo().requestFocus();
                    break;
                //Doy de alta o modifico según sea el caso.
                case "GUARDAR":
                    if (datosOk()) {
                        if (mod) {
                            abrirBase();
                            int row = articulosGui.getTablaArticulos().getSelectedRow();
                            Articulo aux = Articulo.first("codigo = ?", articulosGui.getTablaArticulosDefault().getValueAt(row, 0));
                            aux.set("nombre", articulosGui.getTxtNombre().getText(), "descripcion", articulosGui.getTxtDescripcion().getText());
                            aux.setFloat("stock", articulosGui.getTxtStock().getText());
                            aux.setFloat("min_stock", articulosGui.getTxtStockMinimo().getText());
                            float precio_compra = ParserFloat.stringToFloat(articulosGui.getTxtPrecioCompra().getText());
                            aux.setFloat("precio_compra", precio_compra);
                            aux.setFloat("precio", (ControladorPrincipal.getPorcentajeRecargo() * precio_compra / 100.0f) + precio_compra);
                            gestionArticulos.modificar(aux);
                            gestionArticulos.saveProductImage(articulosGui.getImagen(), aux.getString("codigo"), "jpg");
                            mod = false;
                            JOptionPane.showMessageDialog(articulosGui, "Producto modificado exitosamente!", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                            articulosGui.habilitarNuevo();
                            cargarListaArt();
                        } else {
                            abrirBase();
                            if (articulosGui.getBoxTipo().getSelectedItem() != "PESABLE") {
                                Articulo b = Articulo.first("codigo = ?", articulosGui.getTxtCodigo().getText());
                                if (b == null) {
                                    Articulo a = new Articulo();
                                    a.setString("codigo", articulosGui.getTxtCodigo().getText());
                                    a.setString("nombre", articulosGui.getTxtNombre().getText());
                                    a.setString("tipo", articulosGui.getBoxTipo().getSelectedItem().toString());
                                    float precio_compra = ParserFloat.stringToFloat(articulosGui.getTxtPrecioCompra().getText());
                                    a.setFloat("precio_compra", precio_compra);
                                    a.setFloat("precio", (ControladorPrincipal.getPorcentajeRecargo() * precio_compra / 100.0f) + precio_compra);
                                    a.setFloat("stock", articulosGui.getTxtStock().getText());
                                    a.setFloat("min_stock", articulosGui.getTxtStockMinimo().getText());
                                    a.setString("descripcion", articulosGui.getTxtDescripcion().getText());
                                    gestionArticulos.alta(a, articulosGui.getImagen());
                                    JOptionPane.showMessageDialog(articulosGui, "Producto creado exitosamente!", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                                    articulosGui.habilitarNuevo();
                                    cargarListaArt();
                                } else {
                                    JOptionPane.showMessageDialog(articulosGui, "El codigo ingresado ya existe", "Atencion", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                Articulo a = new Articulo();
                                a.setString("codigo", "");
                                a.setString("nombre", articulosGui.getTxtNombre().getText());
                                a.setString("tipo", articulosGui.getBoxTipo().getSelectedItem().toString());
                                float precio_compra = ParserFloat.stringToFloat(articulosGui.getTxtPrecioCompra().getText());
                                a.setFloat("precio_compra", precio_compra);
                                a.setFloat("precio", (ControladorPrincipal.getPorcentajeRecargo() * precio_compra / 100.0f) + precio_compra);
                                a.setFloat("stock", articulosGui.getTxtStock().getText());
                                a.setFloat("min_stock", articulosGui.getTxtStockMinimo().getText());
                                a.setString("descripcion", articulosGui.getTxtDescripcion().getText());
                                gestionArticulos.alta(a, articulosGui.getImagen());
                                JOptionPane.showMessageDialog(articulosGui, "Producto creado exitosamente!", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                                articulosGui.habilitarNuevo();
                                cargarListaArt();

                            }

                        }

                    } else {
                        JOptionPane.showMessageDialog(articulosGui, "Error: Alguno de los campos obligatorios está vacío. Verifique que el codigo sea mayor o igual a 5 digitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } else if (e.getSource().equals(articulosGui.getBtnModificar())) {
            mod = true;
            articulosGui.habilitarGuardarCancelar();
            articulosGui.getTxtCodigo().setEnabled(false);
            articulosGui.getBoxTipo().setEnabled(false);
        } else if (e.getSource().equals(articulosGui.getBtnEliminar())) {
            switch (articulosGui.getBtnEliminar().getActionCommand()) {
                case "CANCELAR":
                    articulosGui.habilitarNuevo();
                    mod = false;
                    break;
                case "ELIMINAR":
                    Integer resp = JOptionPane.showConfirmDialog(articulosGui, "¿Desea borrar el artículo seleccionado?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        int row = articulosGui.getTablaArticulos().getSelectedRow();
                        Articulo a = Articulo.first("codigo = ?", articulosGui.getTablaArticulosDefault().getValueAt(row, 0));
                        if (gestionArticulos.borrar(a)) {
                            cargarListaArt();
                            gestionArticulos.deleteProductImage(a.getString("codigo"), "jpg");
                            JOptionPane.showMessageDialog(articulosGui, "Producto eliminado correctamente", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(articulosGui, "Ocurrio un error", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    articulosGui.habilitarNuevo();
                    break;
            }
        } else if (e.getActionCommand().equals("CARGAR IMAGEN")) {
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new ImageFilter());
            int showOpenDialog = fc.showOpenDialog(articulosGui);
            if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage image = ImageIO.read(fc.getSelectedFile());
                    articulosGui.setImagen(image);

                } catch (IOException ex) {
                    Logger.getLogger(ControladorArticulos.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getActionCommand().equals("ELIMINAR IMAGEN")) {
            int row = articulosGui.getTablaArticulos().getSelectedRow();
            String codigo = (String) articulosGui.getTablaArticulosDefault().getValueAt(row, 0);
            articulosGui.setImagen(null);
            gestionArticulos.deleteProductImage(codigo, "jpg");
        }
    }

    private void cargarListaArt(LazyList<Articulo> arts) {
        articulosGui.getTablaArticulosDefault().setRowCount(0);
        abrirBase();
        Object[] row = new Object[7];
        for (Articulo a : arts) {
            row[0] = a.get("codigo");
            row[1] = a.get("nombre");
            row[2] = a.get("precio");
            row[3] = a.get("precio_compra");
            row[4] = a.get("tipo");
            row[5] = a.get("stock");
            row[6] = a.get("min_stock");
            articulosGui.getTablaArticulosDefault().addRow(row);
        }
        Base.close();
    }

    private void cargarListaArt() {
        LazyList<Articulo> arts = gestionArticulos.listarArticulos();
        abrirBase();
        articulosGui.getTablaArticulosDefault().setRowCount(0);
        Object[] row = new Object[7];
        for (Articulo a : arts) {

            row[0] = a.get("codigo");
            row[1] = a.get("nombre");
            row[2] = a.get("precio");
            row[3] = a.get("precio_compra");
            row[4] = a.get("tipo");
            row[5] = a.get("stock");
            row[6] = a.get("min_stock");
            articulosGui.getTablaArticulosDefault().addRow(row);
        }
        Base.close();
    }

    private boolean datosOk() {
        if(articulosGui.getBoxTipo().getSelectedItem() == "PESABLE"){
            return (!articulosGui.getTxtCodigo().getText().equals("")
                && !articulosGui.getTxtPrecioCompra().getText().equals("")
                && !articulosGui.getTxtNombre().getText().equals(""));
        }else{
            return (!articulosGui.getTxtCodigo().getText().equals("")
                && !(articulosGui.getTxtCodigo().getText().length() < 5)
                && !articulosGui.getTxtPrecioCompra().getText().equals("")
                && !articulosGui.getTxtNombre().getText().equals("")
                && !articulosGui.getBoxTipo().getSelectedItem().equals("Seleccionar"));
        }
        
    }

    public void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/panaderiabsas", "root", "root");
        }
    }

    /**
     * @return the porcentajeRecargo
     */
    public float getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    /**
     * @param porcentajeRecargo the porcentajeRecargo to set
     */
    public void setPorcentajeRecargo(float porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public void listarProductosEscasos() {
        ProductosEscasosGUI prodEscasosGUI = new ProductosEscasosGUI();
        List<Articulo> lista = gestionArticulos.listarArticulosEscasos();
        prodEscasosGUI.getDtmProductos().setRowCount(0);
        Object[] row = new Object[5];
        for (Articulo a : lista) {
            row[0] = a.getInteger("codigo");
            row[1] = a.getString("nombre");
            row[2] = a.getFloat("precio");
            row[3] = a.getFloat("stock");
            row[4] = a.getFloat("min_stock");
            prodEscasosGUI.getDtmProductos().addRow(row);
        }
    }

}
