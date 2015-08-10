/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.GestionVentas;
import Interfaces.CargarVentasGUI;
import Modelos.Articulo;
import Modelos.Venta;
import Utils.ParserFloat;
import giovynet.nativelink.SerialPort;
import giovynet.serial.Baud;
import giovynet.serial.Com;
import giovynet.serial.Parameters;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import panaderia.Triple;

/**
 *
 * @author alan
 */
public class ControladorCargarVentas implements ActionListener, CellEditorListener {

    CargarVentasGUI cargarVentaGUI;
    GestionVentas gestionVentas;
    private final ControladorVentas controladorVentas;

    Com com1 = null;

    public ControladorCargarVentas(CargarVentasGUI cv, ControladorVentas controlador) throws Exception {
        cargarVentaGUI = cv;
        cargarVentaGUI.setActionListener(this);
        controladorVentas = controlador;
        gestionVentas = new GestionVentas();

        SerialPort serialPort = new SerialPort();
        List<String> portsFree = serialPort.getFreeSerialPort();
        if (portsFree.size() >= 1) {
            Parameters param = new Parameters();
            param.setPort("COM1");
            param.setBaudRate(Baud._9600);
            param.setMinDelayWrite(50);
            com1 = new Com(param);
        }

        cargarVentaGUI.getTxtCodigo().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaKeyReleased(evt);
            }
        });

        cargarVentaGUI.getTxtBuscarPesables().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                abrirBase();
                cargarVentaGUI.getTablaProductosPesablesDefault().setRowCount(0);
                LazyList<Articulo> arts = Articulo.where("nombre like ? and tipo like ?", "%" + cargarVentaGUI.getTxtBuscarPesables().getText() + "%", "PESABLE");
                for (Articulo a : arts) {
                    Object[] row = new Object[3];
                    row[0] = a.getString("id");
                    row[1] = a.getString("nombre");
                    row[2] = a.getString("precio");
                    cargarVentaGUI.getTablaProductosPesablesDefault().addRow(row);
                }
            }
        });

        cargarVentaGUI.getTxtPeso().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (FormatoOK()) {
                    Float peso = Float.parseFloat(cargarVentaGUI.getTxtPeso().getText());
                    int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
                    Float precio = Float.parseFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
                    Float total = peso * precio;
                    System.out.println(precio);
                    cargarVentaGUI.getTxtTotalPesable().setText(total.toString());
                }
            }
        });
        /*DocumentListener documentListener = new DocumentListener() {

         @Override
         public void insertUpdate(DocumentEvent de) {
         System.out.println("caca");
         String data = cargarVentaGUI.getTxtPeso().getText();
         Float peso = ParserFloat.stringToFloat(data);
         int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
         Float precio = ParserFloat.stringToFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
         Float total = peso * precio;
         cargarVentaGUI.getTxtTotalPesable().setText(ParserFloat.floatToString(total));
         }

         @Override
         public void removeUpdate(DocumentEvent de) {
         System.out.println("caca");
         String data = cargarVentaGUI.getTxtPeso().getText();
         Float peso = ParserFloat.stringToFloat(data);
         int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
         Float precio = ParserFloat.stringToFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
         Float total = peso * precio;
         cargarVentaGUI.getTxtTotalPesable().setText(ParserFloat.floatToString(total));
         }

         @Override
         public void changedUpdate(DocumentEvent de) {
         System.out.println("caca");
         String data = cargarVentaGUI.getTxtPeso().getText();
         Float peso = ParserFloat.stringToFloat(data);
         int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
         Float precio = ParserFloat.stringToFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
         Float total = peso * precio;
         cargarVentaGUI.getTxtTotalPesable().setText(ParserFloat.floatToString(total));
         }

         };

         Document txtPeso = cargarVentaGUI.getTxtPeso().getDocument();
         txtPeso.addDocumentListener(documentListener);*/

        cargarVentaGUI.getTablaVenta().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cargarVentaGUI.getTxtCodigo().requestFocus();//pongo el cursor sobre el campo del codigo
            }
        });

        cargarVentaGUI.getTablaProductosPesables().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cargarVentaGUI.getTxtCodigo().requestFocus();
                if (cargarVentaGUI.getTablaProductosPesables().getSelectedRow() != -1) {
                    cargarVentaGUI.getBtnAgregarPesable().setEnabled(true);
                    if (com1 != null) {
                        String data = "";
                        try {
                            com1.sendSingleData("$");
                            for (int x = 0; x < 9; x++) {
                                data = data + com1.receiveSingleString();
                                System.out.println(data);
                            }
                            cargarVentaGUI.getTxtPeso().setText(data.substring(0, 6));
                            Float peso = Float.parseFloat(cargarVentaGUI.getTxtPeso().getText());
                            int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
                            Float precio = Float.parseFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
                            Float total = peso * precio;
                            cargarVentaGUI.getTxtTotalPesable().setText(total.toString());
                            cargarVentaGUI.getBtnPesar().setEnabled(true);
                        } catch (Exception ex) {
                            Logger.getLogger(ControladorCargarVentas.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(cargarVentaGUI, "Ocurrio un error al recibir los datos de la balanza");
                        }
                    } else {
                        Float peso = Float.parseFloat(cargarVentaGUI.getTxtPeso().getText());
                        int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
                        Float precio = Float.parseFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
                        Float total = peso * precio;
                        cargarVentaGUI.getTxtTotalPesable().setText(total.toString());
                    }
                } else {
                    cargarVentaGUI.getTxtPeso().setText("0.00");
                    cargarVentaGUI.getTxtTotalPesable().setText("0.00");
                    cargarVentaGUI.getBtnAgregarPesable().setEnabled(false);
                    cargarVentaGUI.getBtnPesar().setEnabled(false);
                }

            }
        }
        );

    }

    private boolean FormatoOK() {
        try {
            Double peso = Double.valueOf(cargarVentaGUI.getTxtPeso().getText());
            Double totalPesable = Double.valueOf(cargarVentaGUI.getTxtTotalPesable().getText());
            Double total = Double.valueOf(cargarVentaGUI.getTxtTotal().getText());
        } catch (NumberFormatException | ClassCastException e) {
            JOptionPane.showMessageDialog(cargarVentaGUI, "Solo se admiten numeros. Los decimales se escriben despues de un . (punto)", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private int articuloYaCargado(String codigo) {
        for (int i = 0; i < cargarVentaGUI.getTablaVenta().getRowCount(); i++) {
            if (String.valueOf(cargarVentaGUI.getTablaVentaDefault().getValueAt(i, 0)).equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    private void busquedaKeyReleased(KeyEvent evt) {
        String codigo = cargarVentaGUI.getTxtCodigo().getText();
        if (codigo.length() >= 5) {
            //Integer id = Integer.valueOf(codigo.substring(2, 7));//Selecciono solo el id del producto
            int lineaArticulo = articuloYaCargado(codigo);
            abrirBase();
            Articulo articulo = Articulo.first("codigo = ?", codigo);
            if (articulo != null) {
                if (lineaArticulo == -1) {
                    Object row[] = new Object[6];
                    row[0] = articulo.getString("codigo");
                    row[1] = articulo.getString("nombre");
                    /*if (articulo.getString("tipo").equals("PESABLE")) {
                     BigDecimal a = new BigDecimal(codigo.substring(7, 9) + "." + codigo.substring(9, 12));
                     row[2] = a;
                     } else {*/
                    row[2] = BigDecimal.valueOf(1.00);
                    //}
                    row[3] = articulo.getBigDecimal("precio").setScale(2, RoundingMode.CEILING).toString();
                    row[4] = articulo.getBigDecimal("precio").setScale(2, RoundingMode.CEILING).toString();
                    cargarVentaGUI.getTablaVentaDefault().addRow(row);
                } else {
                    // Lo que se hace dentro de este else es sumar en uno o en el peso que sea a la cantidad del articulo si ya estaba en el carrito.
                   /* if (articulo.getString("tipo").equals("PESABLE")) {
                     Double viejaCantidad = new Double(String.valueOf(cargarVentaGUI.getTablaVentaDefault().getValueAt(lineaArticulo, 2)));
                     BigDecimal viejaCantidadBD = BigDecimal.valueOf(viejaCantidad);
                     BigDecimal uno = new BigDecimal(codigo.substring(7, 9) + "." + codigo.substring(9, 12));
                     BigDecimal nuevaCantidad = viejaCantidadBD.add(uno);
                     cargarVentaGUI.getTablaVentaDefault().setValueAt(nuevaCantidad, lineaArticulo, 2);
                     } else {*/
                    Double viejaCantidad = new Double(String.valueOf(cargarVentaGUI.getTablaVentaDefault().getValueAt(lineaArticulo, 2)));
                    BigDecimal viejaCantidadBD = BigDecimal.valueOf(viejaCantidad);
                    BigDecimal uno = new BigDecimal(1);
                    BigDecimal nuevaCantidad = viejaCantidadBD.add(uno);
                    cargarVentaGUI.getTablaVentaDefault().setValueAt(nuevaCantidad, lineaArticulo, 2);
                    //}
                }
            } //else {
            //  JOptionPane.showMessageDialog(cargarVentaGUI, "Producto no encontrado!", "ERROR", JOptionPane.ERROR_MESSAGE);
            //}
            Base.close();

            cargarVentaGUI.getTxtCodigo().setText("");//limpio el campo de id
            setCellEditor();
            actualizarMonto();
        }
    }

    public void cargarProductosPesables() {
        abrirBase();
        cargarVentaGUI.getTablaProductosPesablesDefault().setRowCount(0);
        LazyList<Articulo> lista = Articulo.where("tipo = ?", "PESABLE");
        for (Articulo a : lista) {
            Object[] row = new Object[3];
            row[0] = a.getString("codigo");
            row[1] = a.getString("nombre");
            row[2] = a.getString("precio");
            cargarVentaGUI.getTablaProductosPesablesDefault().addRow(row);
        }
    }

    public Venta ObtenerDatosVenta() {
        abrirBase();
        LinkedList<Triple> listaArticulos = new LinkedList();
        for (int i = 0; i < cargarVentaGUI.getTablaVentaDefault().getRowCount(); i++) {
            Object codigo = cargarVentaGUI.getTablaVenta().getValueAt(i, 0);
            Object idArticulo = Articulo.first("codigo = ?", codigo).get("id");
            Double doubleCantidad = Double.valueOf(String.valueOf(cargarVentaGUI.getTablaVentaDefault().getValueAt(i, 2)));
            BigDecimal cantidad = BigDecimal.valueOf(doubleCantidad);
            Double doublePrecioFinal = Double.valueOf(String.valueOf(cargarVentaGUI.getTablaVentaDefault().getValueAt(i, 3)));
            BigDecimal precioFinal = BigDecimal.valueOf(doublePrecioFinal);
            listaArticulos.add(new Triple(idArticulo, cantidad, precioFinal));
        }
        Venta v = new Venta(listaArticulos);
        v.set("fecha", dateToMySQLDate(Calendar.getInstance().getTime(), false));
        v.setBigDecimal("monto", cargarVentaGUI.getTxtTotal().getText());
        return v;
    }

    public boolean DatosOK() {
        if (cargarVentaGUI.getTablaVenta().getRowCount() == 0) {
            return false;
        }
        return true;
    }

    public void actualizarMonto() {
        BigDecimal importe;
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < cargarVentaGUI.getTablaVenta().getRowCount(); i++) {
            BigDecimal precio_unit = new BigDecimal(String.valueOf(cargarVentaGUI.getTablaVenta().getValueAt(i, 3)));
            Float cantidadF = Float.parseFloat(String.valueOf(cargarVentaGUI.getTablaVenta().getValueAt(i, 2)));
            BigDecimal cantidad = new BigDecimal(cantidadF);
            importe = cantidad.multiply(precio_unit).setScale(2, RoundingMode.CEILING);
            cargarVentaGUI.getTablaVentaDefault().setValueAt(importe, i, 4);
            total = total.add((BigDecimal) cargarVentaGUI.getTablaVentaDefault().getValueAt(i, 4)).setScale(2, RoundingMode.CEILING);
        }
        cargarVentaGUI.getTxtTotal().setText("");
        cargarVentaGUI.getTxtTotal().setText(total.setScale(2, RoundingMode.CEILING).toString());

    }

    public void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/panaderiabsas", "root", "root");
        }
    }

    public void setCellEditor() {
        for (int i = 0; i < cargarVentaGUI.getTablaVenta().getRowCount(); i++) {
            cargarVentaGUI.getTablaVenta().getCellEditor(i, 2).addCellEditorListener(this);
            cargarVentaGUI.getTablaVenta().getCellEditor(i, 3).addCellEditorListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cargarVentaGUI.getBtnQuitar())) {
            if (cargarVentaGUI.getTablaVenta().getSelectedRow() != -1) {//-1 retorna getSelectedRow si no hay fila seleccionada
                cargarVentaGUI.getTablaVentaDefault().removeRow(cargarVentaGUI.getTablaVenta().getSelectedRow());
                actualizarMonto();
            }
        }
        if (e.getSource().equals(cargarVentaGUI.getBtnRegVenta())) {
            if (DatosOK()) {
                if (gestionVentas.Alta(ObtenerDatosVenta())) {
                    JOptionPane.showMessageDialog(cargarVentaGUI, "Venta registrada exitosamente!");
                    cargarVentaGUI.getTablaVentaDefault().setRowCount(0);
                    cargarVentaGUI.getTxtCodigo().setText("");
                    cargarVentaGUI.getTxtTotal().setText("");
                    controladorVentas.cargarListaVentas();
                } else {
                    JOptionPane.showMessageDialog(cargarVentaGUI, "Ocurrio un error, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(cargarVentaGUI, "La lista de productos esta vacia.", "Atencion!", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getSource().equals(cargarVentaGUI.getBtnCancelar())) {
            cargarVentaGUI.getTablaVentaDefault().setRowCount(0);
            cargarVentaGUI.getTxtCodigo().setText("");
            cargarVentaGUI.getTxtTotal().setText("");
        }
        if (e.getSource().equals(cargarVentaGUI.getBtnPesar())) {
            if (cargarVentaGUI.getTablaProductosPesables().getSelectedRow() != -1) {
                String data = "";
                try {
                    com1.sendSingleData("$");
                    for (int x = 0; x < 9; x++) {
                        data = data + com1.receiveSingleString();
                        System.out.println(data);
                    }
                    cargarVentaGUI.getTxtPeso().setText(data.substring(0, 6));
                    Float peso = Float.parseFloat(cargarVentaGUI.getTxtPeso().getText());
                    int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
                    Float precio = Float.parseFloat((String) cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2));
                    Float total = peso * precio;
                    cargarVentaGUI.getTxtTotalPesable().setText(total.toString());
                } catch (Exception ex) {
                    Logger.getLogger(ControladorCargarVentas.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                cargarVentaGUI.getTxtPeso().setText("0.00");
                JOptionPane.showMessageDialog(cargarVentaGUI, "Seleccione un producto.");
            }
        }
        if (e.getSource().equals(cargarVentaGUI.getBtnAgregarPesable())) {
            int r = cargarVentaGUI.getTablaProductosPesables().getSelectedRow();
            Object[] row = new Object[5];
            row[0] = cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 0);
            row[1] = cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 1);
            row[2] = cargarVentaGUI.getTxtPeso().getText();
            row[3] = cargarVentaGUI.getTablaProductosPesables().getValueAt(r, 2);
            //row[4] = cargarVentaGUI.getTxtTotalPesable().getText();
            cargarVentaGUI.getTablaVentaDefault().addRow(row);
            setCellEditor();
            actualizarMonto();
        }

        cargarVentaGUI.getTxtCodigo().requestFocus();//pongo el cursor sobre el campo del codigo
    }

    /*paraMostrar == true: retorna la fecha en formato dd/mm/yyyy (formato pantalla)
     * paraMostrar == false: retorna la fecha en formato yyyy/mm/dd (formato SQL)
     */
    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if (paraMostrar) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(fecha);
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        actualizarMonto();
        cargarVentaGUI.getTxtCodigo().requestFocus();//pongo el cursor sobre el campo del codigo
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
    }
}
