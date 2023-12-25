package Excepcions;

public class ExcepFichero extends Exception {

    public String getTipus() {
        return "Error en fichero";
    }

    public ExcepFichero() {
        super();
    }

    public ExcepFichero(String name) {
        super("\n* * *\nError en fichero: " + name + "\n* * *");
    }

}
