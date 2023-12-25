package Domini;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Queue;

import Domini.A_cotaGilmoreLawler;

public class A_SimulatedAnnealing {

    // Generacion de numeros aleatorios
    Random rand = new Random();
    // Frecuencia de aparicion de las letras
    private TreeMap<Character, Integer> frecuenciaLetras;
    // Indice de las letras para la matriz de flujos
    private HashMap<Character, Integer> indiceLetras;
    // Matriz de flujos generada 
    private double [][] matrizFlujos;
    // Hashmap de la posicion de cada tecla en la distribucion
    private HashMap<Character, SimpleEntry<Integer,Integer>> posTeclas;
    // Plantilla del Expansion Spiral Search
    private int [][] plantilla;

    // Vector de chars de la distribucion resultante
    private char [] distribution;
    // Double para el coste de la generacion del teclado
    private double cost;
    // Vector de chars para almacenar el alfabeto
    private char [] alfabeto;
    // Valor entero para guardar el tamano del alfabeto
    private int size;

    // Temperatura del algoritmo
    private double temp = 1000;
    // Factor de enfriamiento del algoritmo
    private double fcooling = 0.9;
    // Iteraciones a realizar para bajar la temperatura
    private int iters = 1000;

    // Constructora del algoritmo de Simulated Annealing
    public A_SimulatedAnnealing(TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double [][] matrizFlujos) 
    {
        //ahora las variables seran heredadas y ya las tendra el Sim. Annealing
        this.frecuenciaLetras = frecuenciaLetras;
        this.indiceLetras = indiceLetras;
        this.matrizFlujos = matrizFlujos;
    }

    // Inicio de la ejecucion del Simulated Annealing
    public void execute() {
        // Obtenemos el alfabeto inicialmente
        int a = 0;
        size = matrizFlujos[0].length;
        alfabeto = new char[size];
        for (Map.Entry<Character, Integer> freq : frecuenciaLetras.entrySet()) {
            alfabeto[a] = freq.getKey(); ++a;
        }
        
        // Se modifica ssi hay una distribucion mejor
        distribution = Arrays.copyOf(alfabeto, alfabeto.length);

        // Inicializar distribucion, sera nuestra solucion inicial
        char [] teclado = Arrays.copyOf(alfabeto, alfabeto.length);

        // Asignamos para cada tecla, su respectica distribucion con la plantilla
        posTeclas = new HashMap<>();
        plantilla = spyral(size, size);
        for (int i = 0; i < size; ++i) {
            char c = teclado[i];
            SimpleEntry<Integer, Integer> plant = new SimpleEntry<>(plantilla[i][0],plantilla[i][1]);
            posTeclas.put(c, plant);
        }
        cost = getCostDistribution();

        // Ejecutamos el algoritmo hasta que se enfrie.
        while (temp > 1) {
            // Recorremos un numero de iteraciones antes de bajar la temperatura
            for (int k = 0; k < iters; ++k) {

                // Creamos una nueva distribucion
                char [] aux = Arrays.copyOf(teclado, teclado.length);

                // Swapeamos para poder mejorarla
                int i = rand.nextInt(size);
                int j = rand.nextInt(size);
                swap(aux, i, j); 
                
                // Asignamos para cada tecla, su respectica distribucion con la plantilla
                posTeclas = new HashMap<>();
                for (int x = 0; x < size; ++x) {
                    char c = aux[x];
                    SimpleEntry<Integer, Integer> plant = new SimpleEntry<>(plantilla[x][0],plantilla[x][1]);
                    posTeclas.put(c, plant);
                }

                // Calculo de los nuevos costes
                double new_cost = getCostDistribution();
                double res = new_cost - cost;

                // Mejorar la capacidad del algoritmo para explorar y encontrar soluciones más óptimas en el espacio de soluciones.
                boolean explora = Math.random() < Math.exp(-res/temp);

                // Modificamos distrubucion en caso de ser mejor (minimizar el coste) o 
                // la posibilidad de explorar una nueva solución, aun sabiendo que puede ser peor
                if (res < 0 || explora) {
                    teclado = Arrays.copyOf(aux, aux.length);

                    // Mejoramos la distribucion resultante, ya que el coste es menor
                    if (res < 0) {
                        distribution = Arrays.copyOf(teclado, teclado.length);
                        cost = new_cost;
                    }
                }
            }
            // Bajamos la temperatura
            temp *= fcooling;
        }
    }

    // Intercambiar dos teclas en la distribucion
    private static void swap(char [] distribution, int i, int j) {
        char aux = distribution[i];
        distribution[i] = distribution[j];
        distribution[j] = aux;
    }

    // GETTERS

    // Devuelve las posiciones de las teclas colocadas
    public int[][] getPositions() 
    {
        return plantilla;
    }

    // Devuelve las distribucion resultante
    public char [] getDistribution() 
    {
        return distribution;
    }

    // Calculo de la metaheuristica => minimizar la cota0 de Gilmore Lawler
    private double getCostDistribution() 
    {
        return A_cotaGilmoreLawler.metaheuristica(posTeclas, matrizFlujos, indiceLetras);
    }

    // Devuelve el coste de la generacion del teclado
    public double getCost()
    {
        return cost;
    }

    private int[][] spyral(int N, int size) { // N : numero teclas a colocar, size : numero teclas totales

        // Tiene el orden desde el centro del teclado que se construira, el indicie de la posicion en la matriz contenedor. Osea la primera posicion sera la del centro por ejemplo la (1,1)
        int[][] plantilla = new int[N][2];  // array 1D donde cada posicion contine un pair, que lo representamos como una matriz que seria un pair seria:  simpleEntry<Integer, Integer>[]

        int midaContenedor  = (int) Math.ceil(Math.sqrt(size));  // contenedor exterior cuadrado, que elvuelve el teclado circular

        if (midaContenedor % 2 == 0 && midaContenedor != 0) {
            midaContenedor++;
        }

        boolean[][] contenedorVisitado = new boolean[midaContenedor][midaContenedor];  // para marcar las casillas visitadas

        // orden a seguir
        int[] filas = {1, 0, -1, 0, 1, 1, -1, -1} ;   // abajo derecha arriba izquierda
        int[] columnas = {0, 1, 0, -1, 1, -1, 1, -1};
        // diagonales: abajo derecha, abajo izquierda, arriba derecha, arriba izquierda

        int inicioFila = midaContenedor/2; // el medio, tenemos tamaño impar i matriz cuadrada simpre
        int inicioColumna = midaContenedor/2;

        // Cola para realizar el recorrido BFS modificado
        Queue<Integer[]> cola = new LinkedList<>();
        int i = 0;  // orden de las posiciones, si i == 0 es el centro, que sera la primera posicion de la plantilla

        plantilla[0][0] = inicioFila;
        plantilla[0][1] = inicioColumna;
        contenedorVisitado[inicioFila][inicioColumna] = true;
        i++;

        for (int dirs = 0; dirs < 8; dirs++) {  // mirando diagonales, las adyacentes y diagonales iniciales, si hay mas luego no miramos las diagonales
            if (i < N) {
                int filaVecino = inicioFila + filas[dirs];
                int columnaVecino = inicioColumna + columnas[dirs];

                // Verificar si las coordenadas del vecino son válidas y no han sido visitadas (caracter nulo)
                if (filaVecino >= 0 && filaVecino < size && columnaVecino >= 0 && columnaVecino < size && !contenedorVisitado[filaVecino][columnaVecino]) {

                    cola.offer(new Integer[]{filaVecino, columnaVecino}); // los vecinos a la cola

                    plantilla[i][0] = filaVecino;
                    plantilla[i][1] = columnaVecino;
                    contenedorVisitado[filaVecino][columnaVecino] = true;
                    ++i;
                }
            }
        }
        // ahora continuar desde lo que hemos puesto a la cola por orden
        while (!cola.isEmpty() && i < N) {
            Integer[] coordenadas = cola.poll();

            int filaActual = coordenadas[0];
            int columnaActual = coordenadas[1];

            if (!contenedorVisitado[filaActual][columnaActual]) {  // si no esta ocupada
                plantilla[i][0] = filaActual;
                plantilla[i][1] = columnaActual;
                contenedorVisitado[filaActual][columnaActual] = true;
                ++i;
            }
            // Procesar los vecinos adyacentes
            for (int dirs = 0; dirs < 4; dirs++) {  // solo adyacentes
                int filaVecino = filaActual + filas[dirs];
                int columnaVecino = columnaActual + columnas[dirs];

                // Verificar si las coordenadas del vecino son válidas y no han sido visitadas (caracter nulo)
                if (filaVecino >= 0 && filaVecino < midaContenedor && columnaVecino >= 0 && columnaVecino < midaContenedor) {
                    if (!contenedorVisitado[filaVecino][columnaVecino]) { // si no la hemos visitado
                        cola.offer(new Integer[]{filaVecino, columnaVecino});
                    }
                }
            }
        }
        return plantilla;
    }
}
