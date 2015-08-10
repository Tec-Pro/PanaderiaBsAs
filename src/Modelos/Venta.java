/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.util.LinkedList;
import org.javalite.activejdbc.Model;
import panaderia.Triple;

/**
 *
 * @author alan
 */
public class Venta extends Model{
     private LinkedList<Triple> articulos;
    
    public Venta(){
        articulos = null;
    }
    
    public Venta(LinkedList<Triple> l){
        articulos = l;
    }
    
    public LinkedList<Triple> getArticulos() {
        return articulos;
    }

    public void setArticulos(LinkedList<Triple> articulos) {
        this.articulos = articulos;
    }
}
