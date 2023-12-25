package Excepcions;

public class FormatoErroneoFrecPalabras extends ExcepFichero {

    public String getTipus() {
        return "¡¡Formato erroneo!!";
    }

    public FormatoErroneoFrecPalabras() {
        super("¡¡Formato erroneo!!");
    }

    public FormatoErroneoFrecPalabras(String name) {
        super("¡¡Fichero " + name + " formato erroneo!!\nFormato correcto:\nHola\n3\nque\n1\ntal\n2...");
    }

}
