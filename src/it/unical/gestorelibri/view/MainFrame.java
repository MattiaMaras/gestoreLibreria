package it.unical.gestorelibri.view;

import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.persistence.JsonPersistenceStrategy;
import it.unical.gestorelibri.persistence.CsvPersistenceStrategy;
import it.unical.gestorelibri.persistence.PersistenceFacade;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final PersistenceFacade persistence;

    public MainFrame() {
        this.persistence = new PersistenceFacade(new JsonPersistenceStrategy());
        //this.persistence = new PersistenceFacade(new CsvPersistenceStrategy());

        // 1. CARICA I DATI PRIMA DI CREARE LA UI
        persistence.carica();

        setTitle("Gestore Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. AGGIUNGI IL LISTENER PER IL SALVATAGGIO
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                persistence.salva(Libreria.getInstance());
                System.out.println("Libreria salvata. Applicazione in chiusura.");
            }
        });

        BookTablePanel tablePanel = new BookTablePanel();
        ActionPanel actionPanel = new ActionPanel(tablePanel);
        add(actionPanel.getTopPanel(), BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(actionPanel.getBottomPanel(), BorderLayout.SOUTH);
    }
}