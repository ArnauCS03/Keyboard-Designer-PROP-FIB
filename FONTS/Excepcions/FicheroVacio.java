package Excepcions;

public class FicheroVacio extends ExcepFichero {

    public String getTipus() {
        return "¡¡Fichero vacio!!";
    }

    public FicheroVacio() {
        super("¡¡Fichero vacio!!");
    }

    public FicheroVacio(String name) {
        super("¡¡Fichero " + name + " vacio!!");
    }

}
