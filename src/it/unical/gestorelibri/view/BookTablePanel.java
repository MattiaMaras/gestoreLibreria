package it.unical.gestorelibri.view;

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
    private ActionPanel actionPanel;

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
    }

    public void setActionPanel(ActionPanel actionPanel) {
        this.actionPanel = actionPanel;
        aggiornaVista();
    }

    @Override
    public void update() {
        System.out.println("BookTablePanel: Notifica ricevuta, aggiorno la vista...");
        aggiornaVista();
    }

    public void aggiornaVista() {
        if (actionPanel == null) return;
        List<Libro> libriDaMostrare = libreria.getLibri();
        String testoRicerca = actionPanel.getSearchText().toLowerCase();
        if (!testoRicerca.isEmpty()) {
            libriDaMostrare = libriDaMostrare.stream()
                    .filter(libro -> libro.getTitolo().toLowerCase().contains(testoRicerca) ||
                            libro.getAutore().toLowerCase().contains(testoRicerca)) //qui si può aggiungere anche la ricerca per ISBN
                    .collect(Collectors.toList());
        }

        String genereSelezionato = actionPanel.getGenereSelezionato();
        if (!genereSelezionato.equals("Tutti")) {
            libriDaMostrare = libriDaMostrare.stream()
                    .filter(libro -> libro.getGenere().equalsIgnoreCase(genereSelezionato))
                    .collect(Collectors.toList());
        }

        String statoSelezionato = actionPanel.getStatoSelezionato();
        if (!statoSelezionato.equals("Tutti")) {
            libriDaMostrare = libriDaMostrare.stream()
                    .filter(libro -> libro.getStatoLettura().equalsIgnoreCase(statoSelezionato))
                    .collect(Collectors.toList());
        }

        actionPanel.getStrategiaCorrente().ordina(libriDaMostrare);

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