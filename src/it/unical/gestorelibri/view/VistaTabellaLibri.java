package it.unical.gestorelibri.view;

import it.unical.gestorelibri.controller.AggiungiLibroCommand;
import it.unical.gestorelibri.controller.CommandHandler;
import it.unical.gestorelibri.core.Observer;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaTabellaLibri extends JFrame implements Observer {

    private final Libreria libreria;
    private final CommandHandler commandHandler;

    private JTable tabellaLibri;
    private DefaultTableModel tableModel;

    public VistaTabellaLibri() {
        this.libreria = Libreria.getInstance();
        this.commandHandler = new CommandHandler();

        this.libreria.registraObserver(this);

        initUI();
    }

    private void initUI() {
        setTitle("Gestore Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //tabella
        String[] columnNames = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato Lettura"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabellaLibri = new JTable(tableModel);

        //pulsanti
        JButton aggiungiButton = new JButton("Aggiungi Libro");
        JButton annullaButton = new JButton("Annulla");

        JPanel panel = new JPanel();
        panel.add(aggiungiButton);
        panel.add(annullaButton);

        add(new JScrollPane(tabellaLibri), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // --- Collegamento Azioni -> Controller (Pattern Command) ---
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //dati del libro per prima versione (poi passo al form)
                Libro nuovoLibro = new Libro("Nuovo Titolo", "Nuovo Autore", "1234567890", "Romanzo");

                AggiungiLibroCommand cmd = new AggiungiLibroCommand(nuovoLibro, libreria);
                commandHandler.eseguiComando(cmd);
            }
        });

        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commandHandler.annullaUltimoComando();
            }
        });

        aggiornaTabella();
    }

    @Override
    public void update() {
        System.out.println("VISTA: Notifica ricevuta! Aggiorno la tabella...");
        aggiornaTabella();
    }

    private void aggiornaTabella() {
        tableModel.setRowCount(0);
        List<Libro> libri = libreria.getLibri();
        for (Libro libro : libri) {
            Object[] row = {
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getIsbn(),
                    libro.getGenere(),
                    libro.getValutazione(),
                    libro.getStatoLettura()
            };
            tableModel.addRow(row);
        }
    }
}