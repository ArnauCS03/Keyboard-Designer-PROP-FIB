package Excepcions;

public class FormatoErroneoAlfabeto extends ExcepFichero {

    public String getTipus() {
        return "¡¡Formato erroneo!!";
    }

    public FormatoErroneoAlfabeto() {
        super("¡¡Formato erroneo!!");
    }

    public FormatoErroneoAlfabeto(String name) {
        super("¡¡Fichero " + name + " formato erroneo!!\nFormato correcto: abcdef...\nSin repetidos y todos seguidos en una linea.");
    }

}
