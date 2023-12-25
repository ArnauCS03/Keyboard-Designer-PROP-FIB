package Excepcions;

public class FormatoErroneoTexto extends ExcepFichero {

    public String getTipus() {
        return "¡¡Formato erroneo!!";
    }

    public FormatoErroneoTexto() {
        super("¡¡Formato erroneo!!");
    }

    public FormatoErroneoTexto(String name) {
        super("¡¡Fichero " + name + " formato erroneo!!\nFormato correcto:\nHola buenas...\nseguidamente seria...\nY entonces...");
    }

}
