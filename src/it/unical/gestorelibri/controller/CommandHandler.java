package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import java.util.Stack;

public class CommandHandler {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void eseguiComando(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void annullaUltimoComando() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("Nessuna operazione da annullare.");
        }
    }

    public void ripristinaUltimoComando() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            // Rimetto il comando ripristinato nella pila dell'undo
            undoStack.push(command);
        } else {
            System.out.println("Nessuna operazione da ripristinare.");
        }
    }
}