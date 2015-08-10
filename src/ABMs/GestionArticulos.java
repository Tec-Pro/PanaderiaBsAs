/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ABMs;

import Modelos.Articulo;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author Joako
 *
 * Clase que se encarga de crear, modificar y eliminar artículos.
 */
public class GestionArticulos {

    public Map<String, Object> alta(Articulo a, BufferedImage photo) {
        abrirBase();
        Base.openTransaction();
        Articulo nuevo = Articulo.create("codigo", a.get("codigo"), "nombre", a.get("nombre"), "tipo", a.get("tipo"), "precio", a.get("precio"), "precio_compra", a.get("precio_compra"), "descripcion", a.get("descripcion"), "stock", a.get("stock"), "min_stock", a.get("min_stock")
        );
        nuevo.saveIt();
        if(a.getString("tipo").equals("PESABLE")){
            nuevo.set("codigo",nuevo.get("id"));
            nuevo.saveIt();
        }
        saveProductImage(photo, nuevo.getString("codigo"), "jpg");
        Base.commitTransaction();
        return nuevo.toMap();
    }

    public Map<String, Object> modificar(Articulo a) {
        abrirBase();
        Base.openTransaction();
        a.saveIt();
        Base.commitTransaction();
        Base.close();
        return a.toMap();
    }

    public boolean borrar(Articulo a) {
        abrirBase();
        Base.openTransaction();
        Articulo aux = Articulo.first("codigo = ?", a.getString("codigo"));
        boolean res = aux.delete();
        Base.commitTransaction();
        Base.close();
        return res;
    }

    public LazyList<Articulo> listarArticulos() {
        abrirBase();
        LazyList<Articulo> res = Articulo.findAll();
        Base.close();
        return res;
    }

    public LazyList<Articulo> buscarArticulo(String texto) {
        abrirBase();
        LazyList<Articulo> res;
        res = Articulo.where("nombre like ? or tipo like ? or codigo like ?", "%" + texto + "%", "%" + texto + "%", "%" + texto + "%");
        Base.close();
        return res;
    }

    public LazyList<Articulo> listarPorStock(int stock) {
        abrirBase();
        LazyList<Articulo> res;
        res = Articulo.where("stock >= ?", stock);
        Base.close();
        return res;
    }

    public LazyList<Articulo> listarPorStockMinimo(int stock_minimo) {
        abrirBase();
        LazyList<Articulo> res;
        res = Articulo.where("min_stock =< ?", stock_minimo);
        Base.close();
        return res;
    }

    private void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/panaderiabsas", "root", "root");
        }
    }

    public Articulo getArticulo(String id) {
        abrirBase();
        Articulo res = Articulo.first("id = ?", id);
        return res;
    }

    public void deleteProductImage(String codigo, String extension) {
        saveProductImage(null, codigo, extension);
    }

    public BufferedImage getProductImage(String codigo, String extension) {
        BufferedImage img = null;
        File f = new File(System.getProperty("user.dir") + "/product_images/producto_" + codigo + "."+extension);
        if (f.exists() && !f.isDirectory()) { /* do something */
            try {
                img = ImageIO.read(f);
            } catch (IOException e) {
                Logger.getLogger(GestionArticulos.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return img;
    }

    public void saveProductImage(BufferedImage img, String codigo, String extension) {
        if (img != null) {
            try {
                //se escribe en disco
                File output = new File(System.getProperty("user.dir") + "/product_images/producto_" + codigo + "." + extension);
                ImageIO.write(img, extension, output);
            } catch (IOException ex) {
                Logger.getLogger(GestionArticulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            File f = new File(System.getProperty("user.dir") + "/product_images/producto_" + codigo + "." + extension);
            if (f.exists() && !f.isDirectory()) {
                f.delete();
            }
        }
    }
    
    public List<Articulo> listarArticulosEscasos(){
        abrirBase();
        return Articulo.where("stock < min_stock");
    }
}
