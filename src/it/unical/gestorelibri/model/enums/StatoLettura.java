package it.unical.gestorelibri.model.enums;

public enum StatoLettura {
    DA_LEGGERE("Da leggere"),
    IN_LETTURA("In lettura"),
    LETTO("Letto");

    private final String displayName;

    StatoLettura(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}