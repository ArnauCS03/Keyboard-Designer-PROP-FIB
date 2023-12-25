package Domini;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import Domini.A_QAP;

public class A_AlgoritmeGeneral {

    // Booleano que determinara si queremos ejecutar el algoritmo con QAP, o bien, 
    // un Simulated Annealing con nuestras metaheuristicas.
    // (True -> QAP || False -> Simulated Annealing) 
    private boolean algoritme;

    // Frecuencia de aparicion de las letras
    private TreeMap<Character, Integer> frecuenciaLetras;
    // Indice de las letras para la matriz de flujos
    private HashMap<Character, Integer> indiceLetras;
    // Matriz de flujos generada 
    private double [][] matrizFlujos;
    // Hashmap de la posicion de cada tecla en la distribucion
    private HashMap<Character, SimpleEntry<Integer, Integer> > posTeclas;

    // Vector para la distribucion del teclado resultante
    private char [] distribution;
    // Double del coste de la generacion del teclado
    private double cost;
    // Matriz Nx2 con las posiciones de las teclas colocadas
    private int [][] pos;

    // Constructora por defecto
    public A_AlgoritmeGeneral() {}

    // Constructora del Algoritmo General. Se decidirá para ejecutar entre uno de los dos algoritmos implementados
    public A_AlgoritmeGeneral(TreeMap<Character, Integer> frecLetras, HashMap<Character, Integer> indiceLet, double[][] flows)
    {
        // Obtiene los atributos de la controladora de dominio
        this.frecuenciaLetras = frecLetras;
        this.indiceLetras = indiceLet;
        this.matrizFlujos = flows;
    }

    // Decision sobre que algoritmo ejecutar
    public void generarTeclado() {

        // Si el tamaño del afabeto <= 10, entonces ejecuta QAP
        // En caso contrario, ejecuta Simulated Annealing
        algoritme = (matrizFlujos[0].length <= 10);

        if (algoritme) {
            // Generamos la distribucion del teclado con QAP
            A_QAP QAP = new A_QAP(frecuenciaLetras, indiceLetras, matrizFlujos);
            QAP.execute();
            distribution = QAP.getDistribution();
            cost = QAP.getCost();
            pos = QAP.getPositions();
        }

        else {
            // Generamos la distribucion del teclado con Simulated Annealing
            A_SimulatedAnnealing SA = new A_SimulatedAnnealing(frecuenciaLetras, indiceLetras, matrizFlujos);
            SA.execute();
            distribution = SA.getDistribution();
            cost = SA.getCost();
            pos = SA.getPositions();
        }
    }

    // GETTERS

    // Devuelve las posiciones de las teclas colocadas
    public int[][] getPositions() 
    {
        return pos;
    }

    // Devuelve las distribucion resultante
    public char [] getDistribution() 
    {
        return distribution;
    }

    // Devuelve el coste de la generacion del teclado
    public double getCost()
    {
        return cost;
    }

    // Devuelve el coste para un teclado ya hecho
    public double getCostTeclado(char[] teclado, int[][] plantilla, HashMap<Character, Integer> indiceLet, double[][] flows) 
    {
        // Actualizamos parametros de entrada de la función
        this.indiceLetras = indiceLet;
        this.matrizFlujos = flows;

        // Creacion del indice de letras a partir del teclado y la plantilla
        posTeclas = new HashMap<>();
        int tam = teclado.length;
        for (int i = 0; i < tam; ++i) {
            char c = teclado[i];
            SimpleEntry<Integer, Integer> plant = new SimpleEntry<>(plantilla[i][0],plantilla[i][1]);
            posTeclas.put(c, plant);
        }

        // Devuelve el coste dado un teclado generado/modificado
        return A_cotaGilmoreLawler.metaheuristica(posTeclas, matrizFlujos, indiceLetras);
    }
}
