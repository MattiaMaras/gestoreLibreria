# Analisi dei Requisiti - Gestore Libreria Personale

Questo documento descrive le funzionalità e le caratteristiche di qualità richieste per l'applicazione.

## 1. Funzionalità Principali (Requisiti Funzionali)

Elenco delle funzionalità che il sistema deve offrire, con una stima di importanza e complessità.

| #   | Funzionalità            | Descrizione                                                                                                                                                            | Importanza | Complessità |
| :-- | :---------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :--------- | :---------- |
| 1   | **Aggiunta Libro** | L'utente può aggiungere un nuovo libro alla collezione inserendo titolo, autore, ISBN e genere. Valutazione e stato di lettura hanno valori di default.                 | Alta       | Media       |
| 2   | **Modifica Libro** | L'utente può selezionare un libro esistente e modificare i suoi dettagli.                                                                                              | Alta       | Bassa       |
| 3   | **Rimozione Libro** | L'utente può selezionare un libro e rimuoverlo dalla collezione.                                                                                                       | Alta       | Bassa       |
| 4   | **Visualizzazione** | L'applicazione mostra tutti i libri in una tabella o lista chiara e leggibile.                                                                                         | Alta       | Media       |
| 5   | **Ricerca** | L'utente può cercare un libro per titolo o autore. La ricerca non fa distinzione tra maiuscole e minuscole.                                                            | Alta       | Media       |
| 6   | **Filtro** | L'utente può filtrare la lista dei libri in base al genere e/o allo stato di lettura.                                                                                  | Media      | Media       |
| 7   | **Ordinamento** | L'utente può ordinare la lista dei libri per titolo, autore o valutazione.                                                                                             | Media      | Bassa       |
| 8   | **Validazione ISBN** | Il sistema verifica che il codice ISBN inserito sia formalmente valido (es. controllando lunghezza e caratteri).                                                         | Media      | Bassa       |
| 9   | **Annulla/Ripristina** | L'utente può annullare (`undo`) e ripristinare (`redo`) le operazioni di aggiunta, modifica e rimozione di un libro.                                                    | Media      | Alta        |
| 10  | **Salvataggio Dati** | L'applicazione salva l'intera libreria su un file (formato JSON) per non perdere i dati alla chiusura.                                                                 | Alta       | Media       |
| 11  | **Caricamento Dati** | All'avvio, l'applicazione carica automaticamente la libreria salvata in precedenza, se presente.                                                                       | Alta       | Media       |

## 2. Caratteristiche di Qualità (Requisiti Non Funzionali)

* **Usabilità**: L'applicazione deve essere intuitiva e facile da usare, con messaggi di feedback chiari per l'utente (es. "Libro salvato con successo").
* **Affidabilità**: Il salvataggio e il caricamento dei dati devono essere sicuri, senza rischio di perdite o corruzioni, anche in caso di chiusura imprevista.
* **Prestazioni**: La ricerca, il filtro e l'ordinamento devono essere reattivi (risposta entro 1 secondo) anche con centinaia di libri.
* **Manutenibilità**: Il codice deve essere strutturato in modo modulare (usando i design pattern) per facilitare future modifiche o l'aggiunta di nuove funzionalità.
* **Portabilità**: L'applicazione deve poter essere eseguita su diversi sistemi operativi (es. Windows, macOS, Linux) senza modifiche, grazie all'uso di Java.
  
## 3. Attori Coinvolti
- **Utente**: persona che gestisce la propria libreria personale.