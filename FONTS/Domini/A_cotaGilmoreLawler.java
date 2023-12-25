package Domini;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class A_cotaGilmoreLawler {   // GLB

    // Distancia euclideana,dadas las componentes x y de dos posiciones
    private static double distanciaEucl(int x1, int y1, int x2, int y2) {
        return (x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1);  // mas eficiente devolver la dist al cuadrado, y trabajar siempre con distancias al cuadrado (asi no calculamos la raiz)
    }


    // Cota 0 adaptado para el algoritmo simulated, es el coste completo de Gilmore-Lawler porque todas las teclas ya estan colocadas
    //   este metodo usa menos parametros que el metodo normal de cota0, la lista de las teclas colocadas es redundante, se puede obtener con los indices de las posicones
    public static double metaheuristica(HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocadesIndexPos, double[][] flows, HashMap<Character, Integer> indexTeclesMatriuFlux) {

        double costTotal = 0.0;

        // iterar sobre todas las teclas colocadas, serian las i-esimas teclas
        for (HashMap.Entry<Character, SimpleEntry<Integer, Integer>> entry : teclesColocadesIndexPos.entrySet()) {

            char teclaColocada = entry.getKey();  // simbolo de la tecla que tratamos

            int indexCaracter1 = indexTeclesMatriuFlux.get(teclaColocada);  // la fila que ocupa el caracter en la matriz de flujo

            SimpleEntry<Integer, Integer> posColocada1 = entry.getValue(); // pos de la i-esima tecla colocada, una pareja (x,y) que corresponde una casilla de la distribucion del teclado

            // iterar sobre todas las teclas colocadas, serian las j-esimas teclas (miraremos todas las parejas i j de teclas colocadas y calcular el coste acumulado)
            for (HashMap.Entry<Character, SimpleEntry<Integer, Integer>> entry2 : teclesColocadesIndexPos.entrySet()) {

                char teclaColocada2 = entry2.getKey();

                if (teclaColocada != teclaColocada2) {  // no mirar una tecla con sigo misma

                    SimpleEntry<Integer, Integer> posColocada2 = entry2.getValue();  // pos de la j-esima tecla colocada

                    // Calcular distancias, cuando es necesaria, no hace falta precalcularla en una matriz, porque hay toda una submatriz traingular debajo la diagonal, repetida
                    double distanciaEntreColocades = distanciaEucl(posColocada1.getKey(), posColocada1.getValue(), posColocada2.getKey(),posColocada2.getValue());

                    int indexCaracter2 = indexTeclesMatriuFlux.get(teclaColocada2);  // la columna que ocupa el caracter en la matriz de flujo

                    costTotal += distanciaEntreColocades * flows[indexCaracter1][indexCaracter2];
                }
            }
        }
        return costTotal;
    }


    // Cota 0: Coste entre las teclas colocadas
    private static double cota0(List<Character> teclesColocades, HashMap<Character, Integer> indexTeclesMatriuFlux,
                                HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocadesIndexPos, double[][] flows) {

        double costTotal = 0.0;

        int numColocades = teclesColocades.size();

        // Para iterar sobre la lista de teclasColocadas
        Iterator<Character> iterator = teclesColocades.iterator();

        // por cada par de teclas colocadas i j
        for (int i = 0; i < numColocades; i++) {

            char teclaColocada = iterator.next();  // simbolo de la tecla que tratamos

            int indexCaracter1 = indexTeclesMatriuFlux.get(teclaColocada);  // la fila que ocupa el caracter en la matriz de flujo

            SimpleEntry<Integer, Integer> posColocada1 = teclesColocadesIndexPos.get(teclaColocada); // pos de la i-esima tecla colocada, una pareja (x,y) que corresponde una casilla de la distribucion del teclado

            // Para iterar por segunda sobre la lista de teclasColocadas
            Iterator<Character> iterator2 = teclesColocades.iterator();

            for (int j = 0; j < numColocades; j++) {  // mirando la i-esima tecla colocada, ahora calcular el coste con todas las otras teclas colocadas

                char teclaColocada2 = iterator2.next();

                if (i != j) {  // no mirar una tecla con sigo misma

                    SimpleEntry<Integer, Integer> posColocada2 = teclesColocadesIndexPos.get(teclaColocada2);  // pos de la j-esima tecla colocada

                    // Calcular distancias, cuando es necesaria, no hace falta precalcularla en una matriz, porque hay toda una submatriz traingular debajo la diagonal, repetida
                    double distanciaEntreColocades = distanciaEucl(posColocada1.getKey(), posColocada1.getValue(), posColocada2.getKey(),posColocada2.getValue());

                    int indexCaracter2 = indexTeclesMatriuFlux.get(teclaColocada2);  // la columna que ocupa el caracter en la matriz de flujo

                    costTotal += distanciaEntreColocades * flows[indexCaracter1][indexCaracter2];
                }
            }
        }
        return costTotal;
    }

    // Martiz C1: a cada posicion de la matriz: la i-esima fila es la tecla no asignada i la k-esima columna la posicion sin tecla colocada de la distribucion (seria de una manera el coste entre las teclas colocada y las que faltan por colocar)
    private static double[][] matC1(int N, List<Character> teclesColocades,  HashMap<Character,
                                    SimpleEntry<Integer, Integer>> teclesColocadesIndexPos, List<Character> teclesPerColocar,
                                    List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades,
                                    double[][] flows, HashMap<Character, Integer> indexTeclesMatriuFlux) {

        int m = teclesColocades.size();

        int midaNoColocades = N - m;

        // por si acaso no hay no colocadas:
        if (midaNoColocades == 0 || indexPosicionsSenseTeclesColocades.isEmpty()) {  // no hay matriz por construir
            return new double[0][0];
        }


        double[][] C1 = new double[midaNoColocades][midaNoColocades];   // Matriz con mida: las filas, teclas por colocar y las columnas, posiciones sin tecla

        // Precalcular distancias, ya que se mantienen constantes para cada k (cada columna matriz C1)
        double[][] distanciesPreCalculades = new double[midaNoColocades][m];  // las midas, son, en cada columna todas las posiciones que puede ir la tecla, y la columna la distancia de esa posicion a una tecla ya colocada

        for (int k = 0; k < midaNoColocades; k++) {

            SimpleEntry<Integer, Integer> posNoColocada = indexPosicionsSenseTeclesColocades.get(k);

            Iterator<Character> iterator = teclesColocades.iterator();

            for (int j = 0; j < m; j++) {

                char teclaColocada = iterator.next();

                if (distanciesPreCalculades[k][j] == 0.0) {
                    SimpleEntry<Integer, Integer> posColocada = teclesColocadesIndexPos.get(teclaColocada);   // posicion en la distribucion de la tecla ya colocada en indice (ej: (x, y) )
                    double dist = distanciaEucl(posNoColocada.getKey(), posNoColocada.getValue(), posColocada.getKey(), posColocada.getValue());  // distancia euclideana entre teclas, getKey es el ".first" i el getValue el ".second"

                    distanciesPreCalculades[k][j] = dist;
                }
            }
        }

        double costCasella = 0.0;

        Iterator<Character> iterator = teclesPerColocar.iterator();

        // En una iteracion: miramos una tecla no colocada i, miramos a una posicion de la matriz distribucion (k1, k2) y una tecla ya colocada j.
        //  Para calcular un valor de C1, es un coste acumulado de la tecla i en la pos (k1, k2), mirando que este libre y calculado con todas las teclas colocadas la distancia * flujo

        for (int i = 0; i < midaNoColocades; i++) {  // recorrer las teclas no colocadas

            char teclaPerColocar = iterator.next();

            int indexTeclaAColocarFlux = indexTeclesMatriuFlux.get(teclaPerColocar);  // fila que ocupa la tecla no colocada en la matriz de flujo

            for (int k = 0; k < midaNoColocades; k++) {  // por todas las posiciones libres, cada posicion de el vector indexPosicionsSenseTeclesColocades es el pair (k1, k2) de la posicion de una casilla de la distribucion que falta completar

                Iterator<Character> iterator2 = teclesColocades.iterator();  // para recorrer las teclas colocadas

                for (int j = 0; j < m; j++) {            // por todas las teclas ya colocadas

                    char teclaColocada = iterator2.next();

                    double distTeclaColcarAColocada = distanciesPreCalculades[k][j];

                    int indexTeclaColocadaFlux = indexTeclesMatriuFlux.get(teclaColocada);                 // columna que ocupa la tecla colocada en la matriz de flujo

                    double fluxTeclaColocarAColocada = flows[indexTeclaAColocarFlux][indexTeclaColocadaFlux];   // flujo de la tecla no colocada a la ya colocada  (seria mas eficiente, calcularlo una vez por tecla no colocada i, ya que los valores se repiten en toda k de la i-esima tecla)

                    costCasella += distTeclaColcarAColocada * fluxTeclaColocarAColocada;
                }

                C1[i][k] = costCasella;  // guardamos el coste de la tecla no colocada 'i' en la posicion libre (k1, k2), en una columna disponible de C1

                costCasella = 0.0;
            }
        }
        return C1;
    }


    private static double dotProduct(double[] T, double[] D) {
        int size = T.length;
        double total = 0.0;
        for (int i = 0; i < size; i++) {
            total += T[i] * D[i];
        }
        return total;
    }

    // no importa el orden en que lo calculemos las filas de la matriz ya que luego se tendra que ordenar, pero si que cada fila, sea el flujo de esa tecla no colocada al resto
    private static double[][] precalcularTrafico(int numNoColocades, HashMap<Character, Integer> indexTeclesMatriuFlux,
                                                 List<Character> teclesPerColocar,  double[][] flows) {

        double[][] traficPrecalculat = new double[numNoColocades][numNoColocades];

        Iterator<Character> iterator1 = teclesPerColocar.iterator();

        for (int i = 0; i < numNoColocades; i++) {

            char teclaPerColocar = iterator1.next();

            int indexTeclaPerColocInicial = indexTeclesMatriuFlux.get(teclaPerColocar);  // miramos siempre desde esta tecla i, hacia las otras sin colocar para calcular el flujo, el orden importa

            Iterator<Character> iterator2 = teclesPerColocar.iterator();

            for (int t = 0; t < numNoColocades; t++) {

                char teclaPerColocar2 = iterator2.next();

                int indexAltreNoColocada = indexTeclesMatriuFlux.get(teclaPerColocar2);
                traficPrecalculat[i][t] = flows[indexTeclaPerColocInicial][indexAltreNoColocada];
            }
        }
        return traficPrecalculat;
    }


    private static double[][] precalcularDistancias(int numNoColocades,
                                                    List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades) {

        double[][] distanciesPreCalculades = new double[numNoColocades][numNoColocades];

        for (int k = 0; k < numNoColocades; k++) {
            SimpleEntry<Integer, Integer> posNoOcupada1 = indexPosicionsSenseTeclesColocades.get(k);
            for (int p = 0; p < numNoColocades; p++) {

                if (distanciesPreCalculades[k][p] == 0.0) {
                    SimpleEntry<Integer, Integer> posNoOcupada2 = indexPosicionsSenseTeclesColocades.get(p);
                    double dist = distanciaEucl(posNoOcupada1.getKey(), posNoOcupada1.getValue(), posNoOcupada2.getKey(), posNoOcupada2.getValue());

                    distanciesPreCalculades[k][p] = dist;
                    distanciesPreCalculades[p][k] = dist;
                }
            }
        }
        return distanciesPreCalculades;
    }


    // Matriz C2: El elemento (i, k) representa el costo de colocar la i-ésima tecla no colocada en la k-ésima ubicación, con respecto a las teclas aun no emplazadas. (trafico entre las teclas no colocadas)
    private static double[][] matC2(int N, int m, List<Character> teclesPerColocar,
                                    List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades, double[][] flows,
                                    HashMap<Character, Integer> indexTeclesMatriuFlux) {

        int numNoColocades = N - m;

        // por si acaso no hay teclas por colocar
        if (numNoColocades == 0 || indexPosicionsSenseTeclesColocades.isEmpty()) {  // no hay matriz por construir
            return new double[0][0];
        }

        double[][] C2 = new double[numNoColocades][numNoColocades];

        double[][] traficPrecalculat = precalcularTrafico(numNoColocades, indexTeclesMatriuFlux, teclesPerColocar, flows); // vector de trafico de la i-esima tecla no colocada a las otras por colocar


        // Precalcular las distancias, ya que se mantienen constantes para cada k (cada columna matriz C2)
        double[][] distanciesPreCalculades = precalcularDistancias(numNoColocades, indexPosicionsSenseTeclesColocades);

        for (int i = 0; i < numNoColocades; i++) {  // per totes les tecles no colocades, calcular el vector de transit T

            double[] T = traficPrecalculat[i];

            for (int k = 0; k < numNoColocades; k++) {  // por todas las casillas no ocupadas, en la distribucion

                double[] D = distanciesPreCalculades[k];

                Arrays.sort(T); // ordenar vec transito crecientemente

                Arrays.sort(D);

                // Ordenar D decrecientemente
                int sizeD = D.length;
                int halfSizeD = sizeD / 2;
                for (int l = 0; l < halfSizeD; l++) { // recorreoms hasta la mitad del vec ordenado y hacemos swaps
                    double temp = D[l];
                    D[l] = D[sizeD - 1 - l];
                    D[sizeD - 1 - l] = temp;
                }

                // Hacer el producto escalar del vector de transito y el de distancias, y asi no calculamos todas las permutaciones, es una aproximacion del coste
                C2[i][k] = dotProduct(T, D);
            }
        }
        return C2;
    }


    // Suma de matrices del mismo tamano
    private static double[][] sumaMatrius(double[][] M1, double[][] M2) {
        int rows = M1.length;
        int cols = rows;  // siempre cuadrada

        double[][] matResultant = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                matResultant[i][j] = M1[i][j] + M2[i][j];
            }
        }
        return matResultant;
    }


    // Calcular una cota inferior, con la adicion de c0 y el coste de asignacion optima calculado con el Hungarian Algorithm partiendo de la matriz de la suma de dos matrizes de costes C1 y C2
    public static double cota(List<Character> teclesColocades, HashMap<Character, Integer> indexTeclesMatriuFlux,
                              HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocadesIndexPos,
                              double[][] flows, int numTeclesTotal, List<Character> teclesPerColocar,
                              List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades) {

        double c0 = cota0(teclesColocades, indexTeclesMatriuFlux, teclesColocadesIndexPos, flows);

        double[][] matC1 = matC1(numTeclesTotal, teclesColocades, teclesColocadesIndexPos, teclesPerColocar, indexPosicionsSenseTeclesColocades, flows, indexTeclesMatriuFlux);
        double[][] matC2 = matC2(numTeclesTotal, teclesColocades.size(), teclesPerColocar, indexPosicionsSenseTeclesColocades, flows, indexTeclesMatriuFlux);

        if (matC1.length == 0) {  // matriz vacia  (C2 tambien lo es, no hace falta llamar al Hungarian)
            return c0; // solo el coste de las ya colocadas
        }
        else {
            double[][] matResultant = sumaMatrius(matC1, matC2);

            A_hungarian hung = new A_hungarian(matResultant);

            double costAssignacioOptima = hung.getCost();  // buscar la asignacion optima para las teclas no colocadas de la matriz resultante con el Hungarian Algorithm

            return c0 + costAssignacioOptima;
        }
    }

}
