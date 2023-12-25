package Presentacio;

import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P_VerificarFormatoPantalla {

    public P_VerificarFormatoPantalla() {}

    public boolean formatoOkTexto(String texto) {
        // Si aparece algun num error
        if (texto.equals("")) return false;
        if (texto.matches(".*\\d.*")) return false;
        else return true;
    }

    public boolean formatoOkAlfabeto(String alfabeto) {
        // que sean letras solo
        if (alfabeto.equals("")) return false;
        if (!alfabeto.matches("^[\\p{L} ]+$")) return false;

        Set<Character> charVistos = new HashSet<>();
        for (char ch : alfabeto.toCharArray()) {
            // Si es una letra en mayus error (queremos todas en minus)
            if (!Character.isLowerCase(ch) && !Character.isWhitespace(ch)) return false;
            // si es un char repetido error
            if (!charVistos.add(ch)) return false;
        }

        return true;
    }

    public String transfFrecPalabras(String frecpalabras) {
        // procesar a texto
        StringBuilder resultado = new StringBuilder();
        // pasamos a array de strings todo
        String[] frecArray = frecpalabras.split("\\s+");
        // por cada par de palabra y su frecuencia lo vamos juntando en String resultado
        for (int i = 0; i < frecArray.length; i += 2) {
            String palabra = frecArray[i];
            int frec = Integer.parseInt(frecArray[i+1]);
            // añadimos por tantas veces como su frecuencia
            for (int j = 0; j < frec; ++j) {
                resultado.append(palabra).append(" ");
            }
        }
        // cuando acaba quitamos ultimo " " y pasamos a String
        return resultado.toString().trim();
    }

    public boolean formatoOkFrecPalabras(String frecpalabras) {
        // Patron para el formato que queremos "palabra numero palabra numero..."
        // no permitimos que palabra = " " (el espacio se tendra en cuenta entre las palabras totales)
        String patron = "(\\w+\\s+\\d+\\s+)*\\w+\\s+\\d+";

        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(frecpalabras);

        if (frecpalabras.equals("")) return false;
        if (!matcher.matches()) return false;
        else return true;
    }

}
