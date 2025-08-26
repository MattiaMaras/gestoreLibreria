package it.unical.gestorelibri;

import it.unical.gestorelibri.view.VistaTabellaLibri;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VistaTabellaLibri vista = new VistaTabellaLibri();
                vista.setVisible(true);
            }
        });
    }
}