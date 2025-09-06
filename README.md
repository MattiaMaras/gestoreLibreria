# Gestore di Libreria Personale

[![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)](https://www.oracle.com/java/)
[![JUnit](https://img.shields.io/badge/Testing-JUnit5-green?logo=java)](https://junit.org/junit5/)
[![Gson](https://img.shields.io/badge/JSON-Gson-orange?logo=json)](https://github.com/google/gson)

Applicazione desktop per organizzare e tenere traccia della propria collezione personale di libri. Il progetto è stato sviluppato come parte del corso di Ingegneria del Software, con un focus sulla progettazione robusta, l'applicazione rigorosa dei design pattern e il testing unitario.

##  Caratteristiche Principali

- **Gestione Catalogo**: Aggiunta, modifica e rimozione di libri tramite un'interfaccia personalizzata e con validazione dei dati in tempo reale.
- **Consultazione Avanzata**: Vista tabellare con funzionalità di ricerca testuale, filtro componibile per genere e stato di lettura, e ordinamento dinamico.
- **Cronologia Operazioni**: Supporto completo per operazioni di **Undo/Redo** per tutte le azioni di modifica, garantendo la reversibilità delle azioni.
- **Persistenza Flessibile**: Salvataggio automatico della libreria alla chiusura e caricamento all'avvio. L'architettura supporta diversi formati di salvataggio (es. `JSON`, `CSV`).
- **Coerenza dei Dati**: Utilizzo di `enum` per guidare l'input di dati consistenti (Genere, Stato Lettura) e validazione robusta per i campi critici come l'ISBN.

## Architettura e Design

- **Linguaggio**: Java
- **UI**: Java Swing
- **Testing**: JUnit 5
- **Architettura**: Model-View-Controller (MVC) con una chiara separazione delle responsabilità tra i package.
- **Design Patterns Utilizzati**:
    - **Singleton**: Per la gestione dell'unica istanza della libreria.
    - **Observer**: Per il disaccoppiamento tra dati (Model) e interfaccia utente (View).
    - **Command** e **Memento**: Per una gestione robusta delle azioni e della loro reversibilità (Undo/Redo).
    - **Strategy**: Per rendere flessibili e intercambiabili gli algoritmi di ordinamento e la logica di persistenza.
    - **Facade**: Per semplificare l'interazione con il sottosistema di persistenza.

## ▶️ Download e Avvio

Il modo più semplice per provare l'applicazione è scaricare il file `.jar` eseguibile.

1.  Vai alla sezione **[Releases](https://github.com/MattiaMaras/gestoreLibreria/releases)** di questo repository.
2.  Scarica il file `gestoreLibreria.jar`.
3.  Esegui l'applicazione con un doppio click sullo script di avvio (`avvia.command` o `avvia.bat`).

In alternativa, puoi lanciarla da terminale con il comando:
```bash
java -jar gestoreLibreria.jar
```


## Per gli sviluppatori - Struttura del Progetto

```bash
gestoreLibreria/
├── docs/
│   └── UML/            # Diagrammi di design (Casi d’uso, Classi, Sequenza)
├── lib/                # Dipendenze esterne (Gson, JUnit)
├── out/                # File compilati e artifacts (ignorato da Git)
├── src/
│   └── it/unical/gestorelibri/ # Codice sorgente dell'applicazione
│       ├── controller/
│       ├── core/
│       ├── model/
│       ├── persistence/
│       ├── utils/
│       └── view/
└── tests/
    └── it/unical/gestorelibri/ # Test unitari con JUnit 5
```
## Script di Configurazione (Procedura per IntelliJ IDEA)

### Clonare il Repository
Esegui questo comando in un terminale per scaricare il codice sorgente:

```bash
git clone https://github.com/<tuo-utente>/gestoreLibreria.git
```