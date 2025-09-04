package it.unical.gestorelibri.view;

import it.unical.gestorelibri.model.Libro;
import it.unical.gestorelibri.model.enums.Genere;
import it.unical.gestorelibri.model.enums.StatoLettura;
import it.unical.gestorelibri.utils.ValidationUtils;
import javax.swing.*;
import java.awt.*;


public class BookDialog extends JDialog {

    private boolean confirmed = false;
    private Libro libro;

    private JTextField titoloField = new JTextField(20);
    private JTextField autoreField = new JTextField(20);
    private JTextField isbnField = new JTextField(20);
    private JComboBox<Genere> genereComboBox = new JComboBox<>(Genere.values());
    private JSpinner valutazioneSpinner;
    private JComboBox<StatoLettura> statoLetturaComboBox = new JComboBox<>(StatoLettura.values());


    public BookDialog(Frame owner, Libro libroDaModificare) { //libroDaModifare == null -> aggiunta
        super(owner, "Dettagli Libro", true);
        this.libro = libroDaModificare;

        int valutazioneIniziale;
        if (libro != null) { //significa che stiamo modificando, prendiamo la vecchia valutazione per precompilare il campo
                valutazioneIniziale = libro.getValutazione();
        }   else {
            valutazioneIniziale = 1;
        }
        valutazioneSpinner = new JSpinner(new SpinnerNumberModel(valutazioneIniziale, 1, 5, 1)); //le valutazioni vanno da 1 a 5

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Titolo:")); formPanel.add(titoloField);
        formPanel.add(new JLabel("Autore:")); formPanel.add(autoreField);
        formPanel.add(new JLabel("ISBN:")); formPanel.add(isbnField);
        formPanel.add(new JLabel("Genere:")); formPanel.add(genereComboBox);
        formPanel.add(new JLabel("Valutazione (1-5):")); formPanel.add(valutazioneSpinner);
        formPanel.add(new JLabel("Stato Lettura:")); formPanel.add(statoLetturaComboBox);

        // pre compilo i campi se sono in modifica
        if (libro != null) {
            titoloField.setText(libro.getTitolo());
            autoreField.setText(libro.getAutore());
            isbnField.setText(libro.getIsbn());

            try {
                genereComboBox.setSelectedItem(Genere.valueOf(libro.getGenere().toUpperCase().replace("...", "")));
            } catch (IllegalArgumentException e) {
                genereComboBox.setSelectedItem(Genere.ALTRO);
            }

            try {
                statoLetturaComboBox.setSelectedItem(StatoLettura.valueOf(libro.getStatoLettura().toUpperCase().replace(" ", "_")));
            } catch (IllegalArgumentException e) {
                // Se non corrisponde lasciamo il default
            }
        }

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Annulla");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> onConfirm());
        cancelButton.addActionListener(e -> onCancel());

        pack();
        setLocationRelativeTo(owner);
    }

    private void onConfirm() {
        String titolo = titoloField.getText().trim();
        String autore = autoreField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (titolo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Titolo' non può essere vuoto.", "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (autore.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il campo 'Autore' non può essere vuoto.", "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidationUtils.isIsbnValido(isbn)) {
            JOptionPane.showMessageDialog(this, "Il formato dell'ISBN non è valido (richiesto 10 o 13 cifre).", "Errore di Validazione", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se tutti i controlli passano, creiamo l'oggetto Libro
        this.libro = new Libro(
                titolo,
                autore,
                isbn,
                genereComboBox.getSelectedItem().toString()
        );
        this.libro.setValutazione((Integer) valutazioneSpinner.getValue());
        this.libro.setStatoLettura(statoLetturaComboBox.getSelectedItem().toString());

        this.confirmed = true;
        dispose();
    }

    private void onCancel() {
        this.confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Libro getLibro() {
        return libro;
    }
}