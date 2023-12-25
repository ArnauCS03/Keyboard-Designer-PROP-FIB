package Domini;
import java.io.*;
import java.util.*;

public class ProcesarTexto {

    // Matriz de flujos -> filas  a,b,c | cols a, b, c
    private double[][] matrizFlujos;
    // Frecuencias de letras | TreeMap
    private TreeMap<Character, Integer> frecuenciaLetras;
    // Guardar indice para matriz de flujos
    private HashMap<Character, Integer> indiceLetras;

    // Constructora vacia
    public ProcesarTexto() {}

    public ProcesarTexto(String texto) {
        setMatFlujIndiceFreq(texto);
    }


    // Constructora para el input que ya teines un alfabeto y te llega un texto o lista palabras y puede que falten letras (hay que ponerlas tambien en matriz flujos etc..)
    public ProcesarTexto(char[] alfabeto, String texto) {
        setMatFlujIndiceFreq(texto);
        modificarMatFlujoConAlfabeto(alfabeto, texto);
    }

    /*
    public ProcesarTexto(char modo, char tipoTexto) {
        A_ControladorCapaDominio ctrlDominio = new A_ControladorCapaDominio();
        String texto = "";

        if (tipoTexto == 't') texto = ctrlDominio.getTexto();
        else if (tipoTexto == 'l') texto = ctrlDominio.getTextoFrecPalabras();

        if (modo == '3') {
            setMatFlujIndiceFreq(texto);
            modificarMatFlujoConAlfabeto(ctrlDominio.getAlfabeto(), texto);
        }
        else {
            setMatFlujIndiceFreq(texto);
        }
    }
     */

    /*
    // Constructora para matriz de flujos ya viene dada
    public ProcesarTexto(double[][] matriz) {
        this.matrizFlujos = matriz;
    }
     */

    // Constructora para lista frecs a partir de lista de chars y sus frecs
    // (suponemos char[i] tiene el valor de int[i])
    // public ProcesarTexto(char[] listaChars, int[] frecChars) { // SI SON DIF TAMANYO THROW EXCEPTION
    //     this.frecuenciaLetras = creaFrecLetras(listaChars, frecChars);

    // }

    // // Crea el map de frecuencias dada una lista de chars y lista de sus frecs
    // // (deducimos que pos 0 de lista chars equivale a frec de pos 0 de frecChars)
    // public TreeMap<Character, Integer> creaFrecLetras(char[] lChars, int[] fChars) { // SI SON DIFERENTE TAMANYO THROW EXCEPTION
    //     TreeMap<Character, Integer> frec = new TreeMap<Character, Integer>();
    //     for (int i = 0; i < lChars.length; ++i) {
    //         frec.put(lChars[i], fChars[i]);
    //     }
    //     return frec;
    // }

    // GETTERS

    public double[][] getMatrizFlujos() {
        return this.matrizFlujos;
    }

    /*
    public double[][] getMatrizFlujos(String texto) {
        setMatFlujIndiceFreq(texto);
        return this.matrizFlujos;
    }
     */

    public TreeMap<Character, Integer> getFrecLetras() {
        return this.frecuenciaLetras;
    }

    // public TreeMap<Character, Integer> getFrecLetras(String texto) {
    //     setFrecLetras(texto);
    //     return this.frecuenciaLetras;
    // }
    public HashMap<Character, Integer> getIndiceLetras() {
        return this.indiceLetras;
    }

    // SETTERS

    // Pasar texto recibido a la matriz de flujos y por consiguiente crear la frecLetras
    // (necesario tener la frecuencia de letras para el tamaño de la matriz)
    // (si acaba en simbolo puntuacion trata ult char -> ch1 y con ' ' -> ch2. Si no, NO SE trata ult char con ' ')
    public void setMatFlujIndiceFreq(String texto) {
        // Determinamos la frecuencia de letras
        setFrecLetras(texto);
        // Tamanyo matriz sera el size de frec Letras que nos dice cuantas hay
        int tamanyoMatriz = this.frecuenciaLetras.size();
        this.matrizFlujos = new double[tamanyoMatriz][tamanyoMatriz];

        // Vector para contar apariciones de cada letra
        int[] aparicionesLetras = new int[tamanyoMatriz];

        // Declaramos el map del indice
        this.indiceLetras = new HashMap<Character, Integer>();

        // indice principal
        int indexPpl = 0;

        if (texto.length() == 1) {
            this.indiceLetras.put(texto.charAt(0), 0);
            matrizFlujos[0][0] = 0.0;
        }
        else {

            for (int i = 0; i < texto.length() - 1; ++i) {
                char ch1 = texto.charAt(i);
                char ch2 = texto.charAt(i + 1);

                // si ch1 no es letra implica que ya se ha tratado ej: Hola, que.
                // ch1 = ',' y ch2 = ' ' . Anteriormente se
                // habra tratado 'a' con ' ' (IGNORAMOS LA COMA)
                if (!Character.isLetter(ch2) && !Character.isWhitespace(ch2) && i != texto.length() - 2)
                    ch2 = texto.charAt(i + 2);
                    // si ult char es simbolo puntuacion saltar
                else if (!Character.isLetter(ch2) && !Character.isWhitespace(ch2) && i == texto.length() - 2) continue;
                // si simbolo de puntuacion ch1 saltar
                if (!Character.isLetter(ch1) && !Character.isWhitespace(ch1)) continue;
                // Si son mayus pasar a minus
                if (!Character.isLowerCase(ch1)) ch1 = Character.toLowerCase(ch1);
                if (!Character.isLowerCase(ch2)) ch2 = Character.toLowerCase(ch2);

                // Si no esta en el map lo añadimos y incrementamos el indice
                if (!this.indiceLetras.containsKey(ch1)) this.indiceLetras.put(ch1, indexPpl++);
                if (!this.indiceLetras.containsKey(ch2)) this.indiceLetras.put(ch2, indexPpl++);
                int index1 = indiceLetras.get(ch1);
                int index2 = indiceLetras.get(ch2);

                // incrementamos flujo entre letra y letra
                this.matrizFlujos[index1][index2]++;
                // incrementamos aparicion de letra
                aparicionesLetras[index1]++;
                // la ultima letra no la tratará por lo que anyadimos este if
                //    if (i == texto.length() - 2) aparicionesLetras[index2]++;
            }

            // Dividir por cada flujo entre par de letras / apariciones de esa letra
            for (int i = 0; i < matrizFlujos.length; ++i) {
                for (int j = 0; j < matrizFlujos[0].length; ++j) {
                    if (matrizFlujos[i][j] != 0) matrizFlujos[i][j] /= aparicionesLetras[i];
                }
            }
        }

    }

    // Pasar texto recibido al diccionario de frecuencias
    private void setFrecLetras(String texto) {
        this.frecuenciaLetras = new TreeMap<Character, Integer>();
        for (char i : texto.toCharArray()) {
            if (Character.isLetter(i) || Character.isWhitespace(i)) {
                if (!Character.isLowerCase(i)) i = Character.toLowerCase(i);
                if (this.frecuenciaLetras.containsKey(i)) {
                    int val = this.frecuenciaLetras.get(i);
                    val += 1;
                    this.frecuenciaLetras.replace(i, val);
                }
                else {
                    frecuenciaLetras.put(i, 1);
                }
            }
        }
    }

    // Pasar de matriz de flujo + alfabeto a inicializar todo
    // public void setIndiceAlfMat(char[] alfabeto, double[][] matFluj) {
    //     // Tamanyo de matriz == tamanyo del alfabeto
    //     int n = matFluj.length;
    //     // por cada posicion del array de alfabeto añadimos a indice letras con la pos i
    //     for (int i = 0; i < n; ++i) this.indiceLetras.put(alfabeto[i], i);
    // }

    // Caso 3r input alfabeto mas algun tipo de texto
    // (matFlujo ya creada, mirar chars de alfabeto que no se hayan tratado)
    // (anyadir filas+cols a la matriz segun los chars que falten)
    public void modificarMatFlujoConAlfabeto(char[] alf, String texto) {
        int charNoEncontrado = 0;
        Set<Character> letrasNoEnTexto = new HashSet<Character>();
        // Por cada char de alfabeto ver que no aparece en el texto
        for (char ch : alf) {
            if (texto.indexOf(ch) == -1) {
                ++charNoEncontrado;
                // anyadir a frecuenciaLetras
                this.frecuenciaLetras.put(ch,0);
                letrasNoEnTexto.add(ch);
            }
        }
        // Añadir filas y cols a matriz
        int size = this.matrizFlujos.length;
        int index = size;
        size = size + charNoEncontrado;

        // Antes añadir a indice los chars que no aparecen
        for (Character ch : letrasNoEnTexto) {
            this.indiceLetras.put(ch,index++);
        }

        // if (charNoEncontrado == 0) no hacerlo !!!
        double[][] nuevaMat = new double[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (i < (size - charNoEncontrado) && j < (size - charNoEncontrado)) nuevaMat[i][j] = this.matrizFlujos[i][j];
                else nuevaMat[i][j] = 0.0;
            }
        }
        this.matrizFlujos = nuevaMat;
    }
}
