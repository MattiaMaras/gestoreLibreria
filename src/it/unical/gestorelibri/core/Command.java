package it.unical.gestorelibri.core;

public interface Command {
    void execute();
    void undo();
}