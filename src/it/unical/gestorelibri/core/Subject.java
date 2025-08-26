package it.unical.gestorelibri.core;

public interface Subject {
    void registraObserver(Observer obs);
    void rimuoviObserver(Observer obs);
    void notificaObserver();
}