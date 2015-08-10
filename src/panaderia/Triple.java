/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package panaderia;

import Modelos.Articulo;
import java.math.BigDecimal;

/**
 *
 * @author alan
 */
public class Triple {
    
    Object idArticulo;
    BigDecimal cantidad;
    BigDecimal precioFinal;

    public Triple(Object a, BigDecimal c, BigDecimal p){
        idArticulo = a;
        cantidad = c;
        precioFinal = p;
    }

    public Object getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Object articulo) {
        this.idArticulo = articulo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(BigDecimal precioFinal) {
        this.precioFinal = precioFinal;
    }
    
    
}
