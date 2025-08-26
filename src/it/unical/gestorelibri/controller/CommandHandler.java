package it.unical.gestorelibri.controller;

import it.unical.gestorelibri.core.Command;
import java.util.Stack;

public class CommandHandler {
    private final Stack<Command> undoStack = new Stack<>();


    public void eseguiComando(Command command) {
        command.execute();
        undoStack.push(command);
    }

    public void annullaUltimoComando() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
        } else {
            System.out.println("Nessuna operazione da annullare.");
        }
    }
}