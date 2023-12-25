package Domini;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Queue;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;


public class A_Branch {

    private int N;
    private int n;
    private HashMap<Character, Integer> indexCaractersMatFlux;
    private TreeMap<Character, Integer> freqAparicio;
    private List<Map.Entry<Character, Integer>> mayor_aparicion;
    private double[][] flows;
    private double currentCost;
    private double bestCost;
    private List<Character> alfabeto;
    private int[][] orden_visitas;
    private int[][] orden_visitas_completo;
    private HashMap<Character, SimpleEntry<Integer, Integer>> orden_visitas_hash;
    private List<SimpleEntry<Integer, Integer>> posiciones_libres;
    private char[] distribucio;
    List<Character> currentAssignment; // construccion parcial del teclado


    private int midaContenedorMatriz;


    public A_Branch(TreeMap<Character, Integer> freqAparicio, HashMap<Character, Integer> indexCaractersMatFlux, double[][] flows) {


        this.n = flows.length;
        if(n == 0) {
            distribucio = new char[0];
            orden_visitas_completo = new int[0][0];
            bestCost = 0;

        }
        else {
            //el algoritmo no necesita tener en cuenta aquellas teclas que no aparecen en el texto
            TreeMap<Character, Integer> freqAparacioOptima = new TreeMap<>();

            for (Map.Entry<Character, Integer> entry : freqAparicio.entrySet()) {
                if(entry.getValue() != 0)
                    freqAparacioOptima.put(entry.getKey(), entry.getValue());
            }
            this.N = freqAparacioOptima.size();
            this.freqAparicio = freqAparacioOptima;
            this.flows = flows;
            this.indexCaractersMatFlux = indexCaractersMatFlux;
            escogerDistribucion();


            calcula_ordre_visites();

            alfabeto = new ArrayList<>();


            calculaSolucioInical(this.freqAparicio);

            calculaDistribucion();

            //añadimos aquellas teclas que no aparecen en el texto al
        }
    }


    private void escogerDistribucion() {

        // calular el tamaño de la matriz que rodeara el teclado. La matriz sera un rectangulo exterior, para que se guarden los espacios, y junsto en el medio centrado el teclado ciruclar
        this.midaContenedorMatriz  = (int) Math.ceil(Math.sqrt(N));

        if (this.midaContenedorMatriz % 2 == 0 && this.midaContenedorMatriz != 0) {
            this.midaContenedorMatriz++;
        }



        this.distribucio = new char[N]; // tamaño de las teclas que pondremos en el teclado


    }


    // distancia euclideana al cuadrado
    static int euclideanDistanceSQ(int x1, int y1, int x2, int y2) {
        int x = x2 - x1;
        int y = y2 - y1;
        return x * x + y * y;
    }


    private void calcula_ordre_visites() {


        //posiciones de las teclas en 'distribucion'
        orden_visitas = new int[N][2];
        orden_visitas_completo = new int[n][2];
        this.orden_visitas_hash = new HashMap<>();

        boolean[][] matriz_visitados = new boolean[midaContenedorMatriz][midaContenedorMatriz];
        int filasMatriz = matriz_visitados.length;
        int columnasMatriz = matriz_visitados[0].length;

        int[] filas = {1, 0, -1, 0};
        int[] columnas = {0, 1, 0, -1};


        int inicioFila = midaContenedorMatriz/2;   // es simpre simetrica e impar, el medio esta justo entre 2
        int inicioColumna =  midaContenedorMatriz/2;



        // Cola para realizar el recorrido BFS
        Queue<int[]> cola = new LinkedList<>();
        cola.offer(new int[]{inicioFila, inicioColumna});

        matriz_visitados[inicioFila][inicioColumna] = true; // Marcamos la celda como visitada

        //creamos un heap ordenado crecientemente segun
        // la distancia de cada posicion a la posicion de inicio
        PriorityQueue<SimpleEntry<Integer, SimpleEntry<Integer, Integer>>> cercanos =
                new PriorityQueue<>((a, b) -> a.getKey() - b.getKey());


        cercanos.add(new SimpleEntry<>(0, new SimpleEntry<>(inicioFila, inicioColumna)));




        // todo se puede parar cuando se miran N posiciones correctas
        while (!cola.isEmpty()) {
            int[] coordenadas = cola.poll();

            int filaActual = coordenadas[0];
            int columnaActual = coordenadas[1];

            // Procesar los vecinos adyacentes
            for (int j = 0; j < 4; j++) {
                int filaVecino = filaActual + filas[j];
                int columnaVecino = columnaActual + columnas[j];

                // Verificar si las coordenadas del vecino son válidas y no han sido visitadas
                if (filaVecino >= 0 && filaVecino < filasMatriz && columnaVecino >= 0 &&
                        columnaVecino < columnasMatriz && !matriz_visitados[filaVecino][columnaVecino]) {

                    cola.offer(new int[]{filaVecino, columnaVecino});
                    matriz_visitados[filaVecino][columnaVecino] = true; // Marcar como visitado
                    int dist_centro = euclideanDistanceSQ(inicioFila, inicioColumna, filaVecino, columnaVecino);
                    cercanos.add(new SimpleEntry<>(dist_centro , new SimpleEntry<>(filaVecino, columnaVecino)));

                }
            }
        }



        for (int i = 0; i < n; i++) {
            SimpleEntry<Integer, SimpleEntry<Integer, Integer>> act = cercanos.poll();


            if (i < N) {
                orden_visitas[i][0] = act.getValue().getKey();
                orden_visitas[i][1] = act.getValue().getValue();
            }
        }



    }


    private void calculaSolucioInical(TreeMap<Character, Integer> freqAparicio) {

        // Obtener las entradas del TreeMap como una lista
        mayor_aparicion = new LinkedList<>(freqAparicio.entrySet());

        // Ordenar la lista de caracteres cercientemente segun su frecuencia de aparicion
        Collections.sort(mayor_aparicion, new Comparator<Map.Entry<Character, Integer>>() {
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int i = 0;
        for (Map.Entry<Character, Integer> entrada : mayor_aparicion) {
            alfabeto.add(entrada.getKey());
            distribucio[i] = entrada.getKey();
            orden_visitas_hash.put(entrada.getKey(), new SimpleEntry<>(orden_visitas[i][0], orden_visitas[i][1]));
            i++;
        }

        bestCost = A_cotaGilmoreLawler.cota(alfabeto, indexCaractersMatFlux, orden_visitas_hash, flows, freqAparicio.size(),
                new ArrayList<>(), new ArrayList<>());
    }


    public void calculaDistribucion() {

        currentAssignment = new ArrayList<>(); // Asignacion actual
        orden_visitas_hash = new HashMap<>();

        posiciones_libres = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            posiciones_libres.add(new SimpleEntry<>(orden_visitas[i][0], orden_visitas[i][1]));
        }

        List<Character> listaNoColocadas = new ArrayList<>(alfabeto);
        listaNoColocadas.removeAll(currentAssignment);


        currentCost = A_cotaGilmoreLawler.cota(currentAssignment, indexCaractersMatFlux, orden_visitas_hash, flows, N, listaNoColocadas , posiciones_libres);

        branching(0);
    }


    public void branching(int level) {
        if (level == N) {
            // Se ha completado una asignación
            if (currentCost < bestCost) {
                bestCost = currentCost;
                for (int i = 0; i < N; i++) {
                    distribucio[i] = currentAssignment.get(i);
                }
            }

        }
        else {
            // añadir las posiciones libres
            for (int i = level; i < N; i++) {
                posiciones_libres.add(new SimpleEntry<>(orden_visitas[i][0], orden_visitas[i][1]));
            }

            List<Character> no_assigned = new ArrayList<>(alfabeto);
            no_assigned.removeAll(currentAssignment);
            for (char c : no_assigned) {

                currentAssignment.add(c);
                orden_visitas_hash.put(c, new SimpleEntry<>(orden_visitas[level][0], orden_visitas[level][1]));

                // Actualizar el costo actual considerando la asignación actual
                List<Character> no_assig_actual = new ArrayList<>(no_assigned);
                no_assig_actual.remove((Character) c);
                currentCost = A_cotaGilmoreLawler.cota(currentAssignment, indexCaractersMatFlux, orden_visitas_hash, flows, N,
                        no_assig_actual, posiciones_libres);

                if (currentCost < bestCost) {
                    branching(level + 1);
                }

                // Revertir los cambios para la siguiente iteración
                currentAssignment.remove(currentAssignment.size() - 1);
                orden_visitas_hash.remove(c);
            }
        }
    }

    public double getCost() {
        return bestCost;
    }

    public char[] getDistribution() {
        return distribucio;
    }

    public int[][] getPositions() {
        return orden_visitas;
    }

}