package it.unical.gestorelibri.model.enums;

public enum Genere {
    ROMANZO("Romanzo"),
    FANTASY("Fantasy"),
    SAGGIO("Saggio"),
    GIALLO("Giallo"),
    BIOGRAFIA("Biografia"),
    ALTRO("Altro");

    private final String displayName;

    Genere(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}