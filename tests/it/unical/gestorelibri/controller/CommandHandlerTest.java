package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    private CommandHandler commandHandler;

    private TestCommand testCommand1;
    private TestCommand testCommand2;

    private static class TestCommand implements Command { //comando finto solo per i test
        private int executeCount = 0;
        private int undoCount = 0;

        @Override
        public void execute() { this.executeCount++; }
        @Override
        public void undo() { this.undoCount++; }

        public int getExecuteCount() { return executeCount; }
        public int getUndoCount() { return undoCount; }
    }

    @BeforeEach
    void setUp() {
        commandHandler = new CommandHandler();
        testCommand1 = new TestCommand();
        testCommand2 = new TestCommand();
    }

    @Test
    @DisplayName("Eseguire un comando chiama il suo metodo execute")
    void testEseguiComando() {
        commandHandler.eseguiComando(testCommand1);
        assertEquals(1, testCommand1.getExecuteCount());
    }

    @Test
    @DisplayName("Annullare un comando chiama il suo metodo undo")
    void testAnnullaComando() {
        commandHandler.eseguiComando(testCommand1);
        commandHandler.annullaUltimoComando();
        assertEquals(1, testCommand1.getUndoCount());
    }

    @Test
    @DisplayName("Ripristinare un comando chiama di nuovo il suo metodo execute")
    void testRipristinaComando() {
        commandHandler.eseguiComando(testCommand1);
        commandHandler.annullaUltimoComando();

        // Il conteggio di execute è 1
        assertEquals(1, testCommand1.getExecuteCount());

        commandHandler.ripristinaUltimoComando();

        // Il conteggio di execute dovrebbe essere 2
        assertEquals(2, testCommand1.getExecuteCount(), "execute doveva essere chiamato di nuovo per il redo");
    }

    @Test
    @DisplayName("Eseguire un nuovo comando svuota lo stack di redo")
    void testNuovoComandoSvuotaRedoStack() {
        commandHandler.eseguiComando(testCommand1);
        commandHandler.annullaUltimoComando(); // Ora redoStack contiene testCommand1

        //comando nuovo -> redoStack si deve svuotare
        commandHandler.eseguiComando(testCommand2);

        //A schermo deve essere stampato nessuna operazione da ripristinare
        commandHandler.ripristinaUltimoComando();

        //Verifica che il comando non sia stato rieseguito
        assertEquals(1, testCommand1.getExecuteCount(), "Execute del primo comando non doveva essere richiamato");
        assertEquals(1, testCommand1.getExecuteCount(), "Execute del secondo comando non doveva essere richiamato");
        assertEquals(1, testCommand1.getUndoCount(), "Undo del primo comando doveva essere chiamato solo una volta");
    }

    @Test
    @DisplayName("Undo e Redo su storico vuoto non lanciano eccezioni")
    void testUndoRedoSuStoricoVuoto() { //vuoto garantito da BeforeEach
        assertDoesNotThrow(() -> commandHandler.annullaUltimoComando());
        assertDoesNotThrow(() -> commandHandler.ripristinaUltimoComando());
    }

    @Test
    @DisplayName("Una sequenza di undo/redo")
    void testSequenzaUndoRedo() {
        commandHandler.eseguiComando(testCommand1); // Eseguito cmd1
        commandHandler.eseguiComando(testCommand2); // Eseguito cmd2

        commandHandler.annullaUltimoComando(); // Annullato cmd2

        // Stato: cmd1 eseguito 1 volta, cmd2 eseguito 1 volta e annullato 1 volta
        assertEquals(1, testCommand1.getExecuteCount());
        assertEquals(0, testCommand1.getUndoCount());
        assertEquals(1, testCommand2.getExecuteCount());
        assertEquals(1, testCommand2.getUndoCount());

        commandHandler.ripristinaUltimoComando(); // Rieseguito cmd2

        // Stato: cmd1 eseguito 1 volta, cmd2 eseguito 2 volte e annullato 1 volta
        assertEquals(2, testCommand2.getExecuteCount());
        assertEquals(1, testCommand2.getUndoCount());
    }
}