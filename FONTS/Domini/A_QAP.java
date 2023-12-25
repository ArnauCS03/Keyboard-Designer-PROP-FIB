package Domini;

import java.util.TreeMap;
import java.util.HashMap;

public class A_QAP {

    // Frecuencia de aparicion de las letras
    private TreeMap<Character, Integer> frecuenciaLetras;
    // Indice de las letras para la matriz de flujos
    private HashMap<Character, Integer> indiceLetras;
    // Matriz de flujos generada 
    private double [][] matrizFlujos;

    // Vector de chars de la distribucion resultante
    private char [] distribution;
    // Double para el coste de la generacion del teclado
    private double cost;
    // Matriz Nx2 con las posiciones de las teclas colocadas
    private int [][] pos;

    // Constructora del algoritmo de Branch & Bound
    public A_QAP(TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double [][] matrizFlujos) 
    {
        this.frecuenciaLetras = frecuenciaLetras;
        this.indiceLetras = indiceLetras;
        this.matrizFlujos = matrizFlujos;
    }

    // Inicio de la ejecucion del algoritmo de Branch & Bound
    public void execute() 
    {
        A_Branch BranchAndBound = new A_Branch(frecuenciaLetras, indiceLetras, matrizFlujos);
        distribution = BranchAndBound.getDistribution();
        cost = BranchAndBound.getCost();
        pos = BranchAndBound.getPositions();
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
}