package it.unical.gestorelibri.view;

import it.unical.gestorelibri.controller.AggiungiLibroCommand;
import it.unical.gestorelibri.controller.CommandHandler;
import it.unical.gestorelibri.controller.ModificaLibroCommand;
import it.unical.gestorelibri.controller.RimuoviLibroCommand;
import it.unical.gestorelibri.controller.strategy.OrdinaPerAutore;
import it.unical.gestorelibri.controller.strategy.OrdinaPerTitolo;
import it.unical.gestorelibri.controller.strategy.OrdinaPerValutazione;
import it.unical.gestorelibri.controller.strategy.OrdinamentoStrategy;
import it.unical.gestorelibri.core.Observer;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class VistaTabellaLibri extends JFrame implements Observer {

    private final Libreria libreria;
    private final CommandHandler commandHandler;

    private JTable tabellaLibri;
    private DefaultTableModel tableModel;

    private OrdinamentoStrategy strategiaCorrente;

    public VistaTabellaLibri() {
        this.libreria = Libreria.getInstance();
        this.commandHandler = new CommandHandler();
        this.libreria.registraObserver(this);

        //strategia di default all'avvio
        this.strategiaCorrente = new OrdinaPerTitolo();

        initUI();
    }

    private void initUI() {
        setTitle("Gestore Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sortLabel = new JLabel("Ordina per:");
        String[] sortOptions = {"Titolo", "Autore", "Valutazione"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        topPanel.add(sortLabel);
        topPanel.add(sortComboBox);

        //pulsanti
        JPanel bottomPanel = new JPanel();
        JButton aggiungiButton = new JButton("Aggiungi Libro");
        JButton modificaButton = new JButton("Modifica Selezionato");
        JButton rimuoviButton = new JButton("Rimuovi Selezionato");
        JButton annullaButton = new JButton("Annulla");
        bottomPanel.add(aggiungiButton);
        bottomPanel.add(modificaButton);
        bottomPanel.add(rimuoviButton);
        bottomPanel.add(annullaButton);

        //tabella
        String[] columnNames = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato Lettura"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tabellaLibri = new JTable(tableModel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tabellaLibri), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Listener ordinamento ---
        sortComboBox.addActionListener(e -> {
            String selectedOption = (String) sortComboBox.getSelectedItem();
            switch (selectedOption) {
                case "Autore":
                    this.strategiaCorrente = new OrdinaPerAutore();
                    break;
                case "Valutazione":
                    this.strategiaCorrente = new OrdinaPerValutazione();
                    break;
                case "Titolo":
                default:
                    this.strategiaCorrente = new OrdinaPerTitolo();
                    break;
            }
            aggiornaTabella();
        });

        // --- Listener AGGIUNGI ---
        aggiungiButton.addActionListener(e -> {
            Libro nuovoLibro = new Libro("Nuovo Titolo", "Nuovo Autore", "1234567890", "Romanzo");
            AggiungiLibroCommand cmd = new AggiungiLibroCommand(nuovoLibro, libreria);
            commandHandler.eseguiComando(cmd);
        });

        // --- Listener MODIFICA ---
        modificaButton.addActionListener(e -> {
            int selectedRow = tabellaLibri.getSelectedRow();
            if (selectedRow >= 0) {
                Libro libroDaModificare = libreria.getLibri().get(selectedRow);
                String nuovoTitolo = "Titolo Modificato";
                String nuovoAutore = "Autore Modificato";

                ModificaLibroCommand cmd = new ModificaLibroCommand(
                        libreria, libroDaModificare,
                        nuovoTitolo, nuovoAutore,
                        libroDaModificare.getIsbn(),
                        libroDaModificare.getGenere(),
                        5, "Letto"
                );
                commandHandler.eseguiComando(cmd);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- Listener RIMUOVI ---
        rimuoviButton.addActionListener(e -> {
            int selectedRow = tabellaLibri.getSelectedRow();
            if (selectedRow >= 0) {
                Libro libroDaRimuovere = libreria.getLibri().get(selectedRow);
                RimuoviLibroCommand cmd = new RimuoviLibroCommand(libroDaRimuovere, libreria);
                commandHandler.eseguiComando(cmd);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- Listener ANNULLA ---
        annullaButton.addActionListener(e -> commandHandler.annullaUltimoComando());

        aggiornaTabella();
    }

    @Override
    public void update() {
        aggiornaTabella();
    }

    private void aggiornaTabella() {
        tableModel.setRowCount(0);
        List<Libro> libri = libreria.getLibri();

        if (strategiaCorrente != null) {
            strategiaCorrente.ordina(libri);
        }

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