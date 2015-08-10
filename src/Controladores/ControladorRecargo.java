/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.PorcentajeRecargoGUI;
import Utils.ParserFloat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joako
 */
class ControladorRecargo implements ActionListener {

    private PorcentajeRecargoGUI gui;

    ControladorRecargo(PorcentajeRecargoGUI guiRecargo) {
        guiRecargo.setActionListener(this);
        gui = guiRecargo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "OK": {
                try {
                    ControladorPrincipal.setPorcentajeRecargo(ParserFloat.stringToFloat(gui.getTxtRecargo().getText()));
                } catch (IOException ex) {
                    Logger.getLogger(ControladorRecargo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            gui.dispose();
            break;
            case "CANCELAR":
                gui.dispose();
                break;
        }
    }

}
