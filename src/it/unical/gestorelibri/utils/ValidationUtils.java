package it.unical.gestorelibri.utils;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isIsbnValido(String isbn) {
        if (isbn == null) return false;
        String isbnPulito = isbn.replace("-", "").trim();
        if (isbnPulito.length() == 10) { //formato valido, 9 numeri, X finale
            return isbnPulito.matches("^[0-9]{9}[0-9X]$");
        }
        if (isbnPulito.length() == 13) { //formato valido, 13 numeri
            return isbnPulito.matches("^[0-9]{13}$");
        }
        return false;
    }
}//creata una classe perchè lo stesso metodo ci serve in due classi diverse (Aggiungi e Modifica)