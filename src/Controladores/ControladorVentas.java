/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.GestionArticulos;
import ABMs.GestionVentas;
import Interfaces.VentasGUI;
import Modelos.Articulo;
import Modelos.ArticulosVentas;

import Modelos.Venta;
import Utils.ParserFloat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author alan
 */
public class ControladorVentas implements ActionListener {

    private final GestionArticulos gestionArticulos;
    private final GestionVentas gestionVentas;
    private final VentasGUI ventaGui;
    private double total;
    private String desde;
    private String hasta;

    public ControladorVentas(VentasGUI v) {
        gestionArticulos = new GestionArticulos();
        gestionVentas = new GestionVentas();
        total = 0.00;
        ventaGui = v;
        ventaGui.setActionListener(this);
        cargarListaVentas();
        ventaGui.getLblTotal().setText(String.valueOf(total));
        ventaGui.getTablaVentas().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (ventaGui.getTablaVentas().getSelectedRowCount() == 1) {
                    ventaGui.seleccionTabla();
                    cargarArticulosVenta();
                    ventaGui.getBtnEliminarVenta().setEnabled(true);
                } else {
                    ventaGui.getTablaProductosDefault().setRowCount(0);
                    ventaGui.getBtnEliminarVenta().setEnabled(false);
                }
            }

        });

        desde = "";
        hasta = "";
        ventaGui.getTxtDesde().getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cargarVentas();
            }
        });

        ventaGui.getTxtHasta().getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cargarVentas();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Eliminar venta")) {
            abrirBase();
            Integer resp = JOptionPane.showConfirmDialog(ventaGui, "¿Desea borrar la venta seleccionada?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                int row = ventaGui.getTablaVentas().getSelectedRow();
                Venta v = Venta.first("id = ?", ventaGui.getTablaVentas().getValueAt(row, 0));
                if (gestionVentas.Eliminar(v)) {
                    cargarVentas();
                    JOptionPane.showMessageDialog(ventaGui, "Venta eliminada correctamente", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ventaGui, "Ocurrio un error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (Base.hasConnection()) {
                Base.close();
            }
        }
    }

    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if (paraMostrar) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(fecha);
        }
    }

    private void cargarVentas() {
        desde = dateToMySQLDate(ventaGui.getTxtDesde().getCalendar().getTime(), false);
        hasta = dateToMySQLDate(ventaGui.getTxtHasta().getCalendar().getTime(), false);
        ventaGui.getTablaVentasDefault().setRowCount(0);
        List<Venta> lista = gestionVentas.buscarVentas(desde, hasta);
        abrirBase();
        total = 0.00;
        BigDecimal r = new BigDecimal(0);
        for (Venta v : lista) {
            Object row[] = new Object[3];
            row[0] = v.get("id");
            row[1] = dateToMySQLDate(v.getDate("fecha"), true);
            BigDecimal monto = v.getBigDecimal("monto");
            row[2] = monto;
            ventaGui.getTablaVentasDefault().addRow(row);
            r = r.add(monto);
        }
        ventaGui.getLblTotal().setText("");
        ventaGui.getLblTotal().setText(r.setScale(2, RoundingMode.CEILING).toString());
        // Base.close();
    }

    public void cargarListaVentas() {
        LazyList<Venta> ventas = gestionVentas.listarVentas();
        abrirBase();
        ventaGui.getTablaVentasDefault().setRowCount(0);
        total = 0.00;
        BigDecimal r = new BigDecimal(0);
        for (Venta v : ventas) {
            Object row[] = new Object[3];
            row[0] = v.get("id");
            row[1] = dateToMySQLDate(v.getDate("fecha"), true);
            BigDecimal monto = v.getBigDecimal("monto");
            row[2] = monto;
            ventaGui.getTablaVentasDefault().addRow(row);
            r = r.add(monto);
        }
        ventaGui.getLblTotal().setText("");
        ventaGui.getLblTotal().setText(r.setScale(2, RoundingMode.CEILING).toString());
        Base.close();
    }

    private void cargarArticulosVenta() {
        int row = ventaGui.getTablaVentas().getSelectedRow();
        LazyList<ArticulosVentas> articulosVentas = gestionVentas.listarArticulosVenta(String.valueOf(ventaGui.getTablaVentas().getValueAt(row, 0)));
        abrirBase();
        ventaGui.getTablaProductosDefault().setRowCount(0);
        for (ArticulosVentas av : articulosVentas) {
            Articulo a = gestionArticulos.getArticulo(av.getString("articulo_id"));
            if (a != null) {
                Object auxRow[] = new Object[5];
                auxRow[0] = a.get("codigo");
                auxRow[1] = a.get("nombre");
                float cant_art = av.getFloat("cantidad_articulo");
                float precio_art = av.getFloat("monto_articulo");
                auxRow[2] = cant_art;
                auxRow[3] = precio_art;
                auxRow[4] = ParserFloat.floatToString(cant_art * precio_art);
                ventaGui.getTablaProductosDefault().addRow(auxRow);
            }
        }
        Base.close();
    }

    private void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/panaderiabsas", "root", "root");
        }
    }

}
