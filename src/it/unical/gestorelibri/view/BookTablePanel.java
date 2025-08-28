package it.unical.gestorelibri.view;

import it.unical.gestorelibri.controller.strategy.OrdinamentoStrategy;
import it.unical.gestorelibri.core.Observer;
import it.unical.gestorelibri.model.Libreria;
import it.unical.gestorelibri.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BookTablePanel extends JPanel implements Observer {

    private final Libreria libreria;
    private JTable tabellaLibri;
    private DefaultTableModel tableModel;
    private List<Libro> libriVisualizzati;

    public BookTablePanel() {
        this.libreria = Libreria.getInstance();
        this.libreria.registraObserver(this);
        this.libriVisualizzati = new ArrayList<>();

        setLayout(new BorderLayout());

        //tabella
        String[] columnNames = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato Lettura"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaLibri = new JTable(tableModel);

        add(new JScrollPane(tabellaLibri), BorderLayout.CENTER);

        update();
    }

    @Override
    public void update() {
        aggiornaVista(null, null, null);
    }

    public void aggiornaVista(OrdinamentoStrategy strategia, String genere, String stato) {
        List<Libro> libriDaMostrare = libreria.getLibri();

        if (genere != null && !genere.equals("Tutti")) {
            libriDaMostrare = libriDaMostrare.stream()
                    .filter(libro -> libro.getGenere().equalsIgnoreCase(genere))
                    .collect(Collectors.toList());
        }
        if (stato != null && !stato.equals("Tutti")) {
            libriDaMostrare = libriDaMostrare.stream()
                    .filter(libro -> libro.getStatoLettura().equalsIgnoreCase(stato))
                    .collect(Collectors.toList());
        }

        if (strategia != null) {
            strategia.ordina(libriDaMostrare);
        }

        this.libriVisualizzati = libriDaMostrare;

        tableModel.setRowCount(0);
        for (Libro libro : this.libriVisualizzati) {
            Object[] row = {
                    libro.getTitolo(), libro.getAutore(), libro.getIsbn(),
                    libro.getGenere(), libro.getValutazione(), libro.getStatoLettura()
            };
            tableModel.addRow(row);
        }
    }


    public Libro getLibroSelezionato() {
        int selectedRow = tabellaLibri.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < libriVisualizzati.size()) {
            return libriVisualizzati.get(selectedRow);
        }
        return null;
    }
}