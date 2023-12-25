package Presentacio;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Excepcions.*;


public class P_LeerInput {

    private File archivo;
    private FileReader fr;
    private BufferedReader br;

    private String texto;
    //private double[][] matFlujo;
    private char[] alfabeto;
    private String textoFrecPalabras;


    public P_LeerInput() {}

    // GETTERS

    public String getTexto() { return this.texto; }

    //public double[][] getMatFlujo() { return this.matFlujo; } // funcionalidad extra antigua

    public char[] getAlfabeto() { return this.alfabeto; }

    public String getTextoFrecPalabras() { return this.textoFrecPalabras; }


    // chequear si archivo esta vacio
    private boolean vacioArchivo() {
        return this.archivo.length() == 0;
    }

    // chequear si archivo de texto es correcto el formato
    private boolean formatoTextoOk() throws IOException {
        // expresión regular para texto de palabras y espacios y signos de puntacion (menos "")
        Pattern patron = Pattern.compile("^[\\p{L}.,!?;:'()\\s]+$");
        String linea;
        while ((linea = br.readLine()) != null) {
            // comparamos cada linea con la expresión patron
            if (linea.isEmpty()) continue;
            Matcher matcher = patron.matcher(linea);
            if (!matcher.matches()) {
                // si una línea no coincide con el patron el formato no es correcto
                return false;
            }
        }
        return true; // si todas estan ok true
    }

    // chequear si archivo de alfabeto es correcto el formato
    private boolean formatoAlfabetoOk() throws IOException {
        Pattern patron = Pattern.compile("^[\\p{L} ]+$");
        String linea;
        linea = br.readLine();
        // comprobar que no se repitan los chars
        Set<Character> charVistos = new HashSet<>();
        for (char ch : linea.toCharArray()) {
            if (!Character.isLowerCase(ch) && !Character.isWhitespace(ch)) return false;
            if (!charVistos.add(ch)) return false;
        }
        Matcher matcher = patron.matcher(linea);
        return matcher.matches();
    }

    // chequear si archivo de frec de palabras es correcto el formato
    private boolean formatoFrecPalabrasOk() throws IOException {
        // String palabra;
        // String frecuencia = "";

        String str;
        boolean esPalabra = true;
        int nlineas = 0;
        while ((str = br.readLine()) != null) {
            if (esPalabra && !str.matches("^[\\p{L} ]+$")) return false; // verificar que es una palabra
            if (!esPalabra && !str.matches("\\d+")) return false; // verificar que es un numero
            esPalabra = !esPalabra;
            ++nlineas;
        }
        if (nlineas%2!=0) {
            return false;
        }
        return true; // si no se cumple lo anterior todo ok
    }

    // Leer texto
    public void leerTexto(File fichero) throws ExcepFichero {
        String textoleido = "";

        // Apertura de fichero (excepcion si error)
        try {
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            if (vacioArchivo()) throw new FicheroVacio(fichero.getName());
            if (!formatoTextoOk()) throw new FormatoErroneoTexto(fichero.getName());

            // volvemos a leer desde el inicio del archivo
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            String linea;
            boolean primeraLinea = true;
            while (( linea = br.readLine()) != null) {
                if (!linea.isEmpty() && !primeraLinea) textoleido = textoleido + " " + linea;
                else if (primeraLinea) {
                    textoleido = linea;
                    primeraLinea = false;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("¡¡No existe el archivo!!");
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            // Cerramos fichero tanto si hay error como si no
            try {
                if (fr != null) fr.close();
            } catch (IOException e2) {
                System.out.println("¡¡Error al cerrar fichero!!");
                e2.printStackTrace();
            }
        }
        // Asignamos lo leido
        this.texto = textoleido;
    }

    /*
    // Anteriormente gestionabamos un input de matriz de flujo
    // Leer matriz flujo (tamanyo especificado en PARAMETRO)
    public void leerMatrizFlujo(String nombreArchivo, int tamanyo) throws ExcepFichero {
        double[][] matrizFlujo = new double[tamanyo][tamanyo];

        // Apertura fichero (excepcion si error)
        try {
            this.archivo = new File (nombreArchivo);
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            if (vacioArchivo(nombreArchivo)) throw new FicheroVacio(nombreArchivo);

            int fila = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] nums = linea.split(" ");

                // si fila >= tamanyo formato erroneo
                if (fila >= tamanyo) throw new FormatoErroneoMatriz(nombreArchivo);
                // Si el tamaño de nums es diferente del "tamanyo" formato erroneo
                if (tamanyo != nums.length) throw new FormatoErroneoMatriz(nombreArchivo);

                for (int i = 0; i < nums.length; ++i) {
                    // si el string nums[i] no es un double lanzar formato erroneo
                    if (!nums[i].matches("-?\\d+(\\.\\d+)?")) throw new FormatoErroneoMatriz(nombreArchivo);
                    matrizFlujo[fila][i] = Double.parseDouble(nums[i]);
                }
                ++fila;
            }
        } catch (FileNotFoundException e) {
            System.out.println("¡¡No existe el archivo!!");
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            // Cerramos fichero tanto si hay error como si no
            try {
                if (fr != null) fr.close();
            } catch (IOException e2) {
                System.out.println("¡¡Error al cerrar fichero!!");
                e2.printStackTrace();
            }
        }
        // Asignamos matriz
        this.matFlujo = matrizFlujo;
    }
     */

    // Leer alfabeto (linea de chars)
    public void leerAlfabeto(File fichero) throws ExcepFichero {
        char[] alfabetoleido = new char[0];

        // Apertura fichero (excepcion si error)
        try {
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            // si formato o que esta vacio excepcion
            if (vacioArchivo()) throw new FicheroVacio(fichero.getName());
            if (!formatoAlfabetoOk()) throw new FormatoErroneoAlfabeto(fichero.getName());

            // volvemos a leer desde el inicio
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            String linea;
            linea = br.readLine();
            int tamanyo = linea.length();
            alfabetoleido = new char[tamanyo];
            for (int i = 0; i < tamanyo; ++i)
                alfabetoleido[i] = linea.charAt(i);

        } catch (FileNotFoundException e) {
            System.out.println("¡¡No existe el archivo!!");
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            // Cerramos fichero tanto si hay error como si no
            try {
                if (fr != null) fr.close();
            } catch (IOException e2) {
                System.out.println("¡¡Error al cerrar archivo!!");
                e2.printStackTrace();
            }
        }
        // Devolvemos alfabeto
        this.alfabeto = alfabetoleido;
    }


    // procesar la lista de frecuencia de palabras para unirlo en un unico string
    private String procesarFrecPalabras() throws IOException {
        StringBuilder resultado = new StringBuilder();

        String palabra;
        while ((palabra = br.readLine()) != null) {
            String frecuenciaStr = br.readLine();
            int frecuencia = Integer.parseInt(frecuenciaStr);

            // Repetir la palabra según su frecuencia de aparicion
            for (int i = 0; i < frecuencia; i++) {
                    resultado.append(palabra).append(" ");
            }
        }
        return resultado.toString().trim(); // trim elimina el espacio al final
    }

    // Leer lista palabras con frecuencia de aparicion (cada palabra y las veces que aparece)
    public void leerFrecPalabras(File fichero) throws ExcepFichero {
        String resultado = "";

        try {
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            // chequear que no esta vacio
            if (vacioArchivo()) throw new FicheroVacio(fichero.getName());
            // chequear que formato sea el correcto
            if (!formatoFrecPalabrasOk()) throw new FormatoErroneoFrecPalabras(fichero.getName());

            // volver a leer desde el inicio
            this.archivo = fichero;
            this.fr = new FileReader (archivo);
            this.br = new BufferedReader (fr);

            resultado = procesarFrecPalabras();
        } catch (FileNotFoundException e) {
            System.out.println("¡¡No existe el archivo!!");
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            // Cerramos fichero tanto si hay error como si no
            try {
                if (fr != null) fr.close();
            } catch (IOException e2) {
                System.out.println("¡¡Error al cerrar archivo!!");
                e2.printStackTrace();
            }
        }
        // Asignamos a atributo
        this.textoFrecPalabras = resultado;
    }

}
