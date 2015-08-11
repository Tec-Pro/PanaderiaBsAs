/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.GestionArticulos;
import Interfaces.AplicacionGUI;
import Interfaces.ArticulosGUI;
import Interfaces.CargarVentasGUI;
import Interfaces.PorcentajeRecargoGUI;
import Interfaces.ProductosEscasosGUI;
import Interfaces.VentasGUI;
import Modelos.Articulo;
import Modelos.Demo;
import Utils.GeneralConfig;
import Utils.ParserFloat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.javalite.activejdbc.Base;

/**
 *
 * @author alan
 */
public class ControladorPrincipal implements ActionListener {

    static AplicacionGUI aplicacionGUI;
    CargarVentasGUI cargarVentaGUI;
    VentasGUI ventasGUI;
    GestionBackupBD gestionBackup;
    ControladorArticulos controladorArticulos;

    private final ArticulosGUI articulosGUI;
    private static float porcentajeRecargo;
    private static GeneralConfig config;
    private final ProductosEscasosGUI productosEscasosGui;

    public static int balanza = 0;

    ControladorCargarVentas controladorCargarVentas;

    public ControladorPrincipal() throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, e);
        }

        aplicacionGUI = new AplicacionGUI();
        aplicacionGUI.setActionListener(this);
        aplicacionGUI.setExtendedState(JFrame.MAXIMIZED_BOTH);

        config = new GeneralConfig();
        try {
            config.loadProperties();
            porcentajeRecargo = ParserFloat.stringToFloat(config.getPercentage());
        } catch (IOException ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        ventasGUI = new VentasGUI();
        ControladorVentas controladorVentas = new ControladorVentas(ventasGUI);

        cargarVentaGUI = new CargarVentasGUI();
        controladorCargarVentas = new ControladorCargarVentas(cargarVentaGUI, controladorVentas);

        articulosGUI = new ArticulosGUI();
        controladorArticulos = new ControladorArticulos(articulosGUI);

        productosEscasosGui = new ProductosEscasosGUI();

        aplicacionGUI.getEscritorio().add(cargarVentaGUI);
        aplicacionGUI.getEscritorio().add(ventasGUI);
        aplicacionGUI.getEscritorio().add(articulosGUI);

        aplicacionGUI.setVisible(true);

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

    public static void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/panaderiabsas", "root", "root");
        }
    }

    public static void main(String[] args) throws Exception {
        abrirBase();
        Demo d = Demo.first("id = ?", 2);
        if (Calendar.getInstance().getTime().before(d.getDate("fecha"))) {

            ControladorPrincipal aplicacion = new ControladorPrincipal();
            Integer resp = JOptionPane.showConfirmDialog(aplicacionGUI, "¿La balanza esta conectada?", "Atencion", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                balanza = 1;
                System.out.println(balanza);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Se acabo el tiempo para la version de prueba.", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(aplicacionGUI.getBtnCargarVenta())) {
            cargarVentaGUI.setVisible(true);
            controladorCargarVentas.cargarProductosPesables();
            cargarVentaGUI.getTxtCodigo().requestFocus();
        }
        if (e.getSource().equals(aplicacionGUI.getBtnVentas())) {
            ventasGUI.setVisible(true);
        }
        if (e.getSource().equals(aplicacionGUI.getBtnArticulos())) {
            controladorArticulos.recargarListaArticulos();
            articulosGUI.setVisible(true);
        }
        if (e.getSource() == aplicacionGUI.getCrearBackup()) {
            gestionBackup = new GestionBackupBD();
            gestionBackup.conectar();
            gestionBackup.GuardarRutaBackup();
            gestionBackup.CrearBackup();
            String dir = (new File(System.getProperty("user.dir")).getAbsolutePath());
        } else if (e.getActionCommand().equals("PORCENTAJE RECARGO")) {
            PorcentajeRecargoGUI guiRecargo = new PorcentajeRecargoGUI(aplicacionGUI, true);
            ControladorRecargo contRecargo = new ControladorRecargo(guiRecargo);
            guiRecargo.getTxtRecargo().setText(String.valueOf(getPorcentajeRecargo()));
            guiRecargo.setLocationRelativeTo(null);
            guiRecargo.setVisible(true);
        } else if (e.getActionCommand().equals("LISTAR ESCASOS")) {
            cargarListaEscasos();
            productosEscasosGui.setLocationRelativeTo(null);
            productosEscasosGui.setVisible(true);
            productosEscasosGui.toFront();
        }
    }

    public static void setPorcentajeRecargo(float aPercentage) throws IOException {
        porcentajeRecargo = aPercentage;
        config.setPercentage(ParserFloat.floatToString(aPercentage));
        config.saveProperty("percentage", ParserFloat.floatToString(aPercentage));
    }

    public static float getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    private void cargarListaEscasos() {
        GestionArticulos articulos = new GestionArticulos();
        List<Articulo> escasos = articulos.listarArticulosEscasos();
        System.out.println(escasos);
        Object[] row = new Object[5];
        productosEscasosGui.getDtmProductos().setRowCount(0);
        for (Articulo a : escasos) {
            row[0] = a.getInteger("codigo");
            row[1] = a.getString("nombre");
            row[2] = a.getFloat("precio");
            row[3] = a.getFloat("stock");
            row[4] = a.getFloat("min_stock");
            productosEscasosGui.getDtmProductos().addRow(row);
        }
    }
}
