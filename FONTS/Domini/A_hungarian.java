package Domini;
import java.util.Arrays;
import java.util.AbstractMap.SimpleEntry;

public class A_hungarian {

    // Tamano de la matriz (filas, columnas)
    private int size;
    // Matriz inicial de los elementos
    private double [][] ini;      
    // Matriz resultante al aplicar los calculos          
    private static double [][] res;         
    // Numero de lineas minimo seleccionadas 
    private static int nlines;
    // Filas escogidas por el backtracking
    private static boolean [] idrows;      
    // Columnas seleccionadas por el backtracking     
    private static boolean [] idcols;              
    // Coste optimo de la asignacion
    private double opt;

    // Constructora para generar el algoritmo de Hungarian
    // Pre: La matriz a calcular ya viene en el formato adecuado
    public A_hungarian(double [][] Mfreq) 
    {
        ini = Mfreq;
        fasePreproceso();
        faseIterativa();
    }

    // Primera fase: Preproceso.
    // Pre: Las posiciones que no esten ocupadas habra asignado un valor -1, que más adelante lo cambiaremos por el valor max.
    // Post: Se ejecutaran todas las operaciones, por fila y columna, acumulandolas en la matriz resultante.
    private void fasePreproceso() 
    {        
        // Inicializacion de los valores
        initializeValues(ini);
        // Asignamos el valor maximo a los no asignados
        setMaxValue(getMaxValue());
        // Se substrae de cada fila y columna, el mínimo valor de esa fila
        substractValues();
    }

    // Segunda fase: Iterativa.
    // Pre: Los calculos por fila y columna ya han sido realizados correctamente y guardados en la matriz.
    // Post: Se asignara el coste optimo una vez seleccionado el minimo de lineas por fila y columna.
    private void faseIterativa() 
    {
        getLinesMin();
        while (nlines < size) {
            // Obtenemos el minimo de los elementos no cubiertos por las lineas
            double min = getMinUncovered();
            // Restamos ese valor minimo a los elementos no cubiertos
            substractUncoveredValues(min);
            // Sumamos ese valor, a los elementos que esten cubiertos
            addSolapatedValues(min);
            // Volvemos a calcular el numero de lineas minimo
            getLinesMin();
        }
        // Asignamos el coste optimo
        setCost();
    }

    
    // GETTERS

    // Calcula el maximo valor posible de una matriz
    // Pre: Suponemos que siempre habra una freq asignada => existiran numeros con valor > -1
    private double getMaxValue() 
    {
        double max = -1;
        for (int i = 0; i < size; ++i) 
            for (int j = 0; j < size; ++j) 
                if (max < res[i][j]) max = res[i][j];
        return max;
    }

    // Obtenemos el valor minimo de todos aquellos elementos con lineas sin solapar
    private double getMinUncovered() 
    {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (!idrows[i] && !idcols[j] && min > res[i][j]) min = res[i][j];
            }
        }
        return min;
    }

    // Calculo de las lineas minimas necesarias para cubrir todos los ceros
    private void getLinesMin() {
        // Primero asignamos el maximo valor para cada vez quedarnos con el min. de lineas
        nlines = Integer.MAX_VALUE;
        // Deseleccionamos las lineas para volver a probar con los cambios implementados
        initializeLines();

        // Buscamos la combinacion de lineas minimas por fila y columna
        int[] combination = new int[2*size];
        backtrackingLines(combination, 0, 2*size);
        // Tendremos las filas y columnas marcadas en idrows e idcols, respectivamente
    }

    // Obtenemos el numero de ceros que quedan por ser marcados con dicha combinacion
    private static int getNumZeros(int size, boolean[] idr, boolean[] idc) {
        int count = 0;
        for (int i = 0; i < size; ++i) 
            for (int j = 0; j < size; ++j) 
                if (!idr[i] && !idc[j] && res[i][j] == 0) ++count;
        return count;
    }


    // Devolvemos el coste optimo
    public double getCost() 
    {
        return opt;
    }


    // SETTERS

    // Restamos los valores por fila y columna de la parte preproceso
    private void substractValues() 
    {
        // FILA
        double min;
        for (int i = 0; i < size; ++i) {
            // Primer elemento de la respectiva fila
            min = res[i][0];                                                
            // Calculamos el minimo de la fila
            for (int j = 1; j < size; ++j) {                     
                if (res[i][j] < min) min = res[i][j];
            }
            // Restamos el minimo a toda la fila
            for (int j = 0; j < size; ++j) res[i][j] -= min;
        }

        // COLUMNA 
        for (int j = 0; j < size; ++j) {
            // Primer elemento de la respectiva columna
            min = res[0][j];                                                
            // Calculamos el minimo de la columna
            for (int i = 1; i < size; ++i) {                     
                if (res[i][j] < min) min = res[i][j];
            }
            // Restamos el minimo a toda la columna
            for (int i = 0; i < size; ++i) res[i][j] -= min;
        }
    }

    // Restamos el valor a los elementos sin lineas solapadas
    private void substractUncoveredValues(double min) 
    {
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                if (!idrows[i] && !idcols[j]) res[i][j] -= min;
    }

    // Sumamos el valor a los elementos con lineas solapadas
    private void addSolapatedValues(double min) 
    {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (idrows[i] && idcols[j]) res[i][j] += min;
            }
        }
    }

    // Asignamos el valor maximo a las posiciones que no haya asignadas un valor
    private void setMaxValue(double max) 
    {
        for (int i = 0; i < size; ++i) 
            for (int j = 0; j < size; ++j) 
                if (res[i][j] == -1) res[i][j] = max;
    }

    // Asignamos el coste optimo despues de hacer el backtracking con la asignacion
    private void setCost() 
    {
        boolean [] row = new boolean[size];
        boolean [] col = new boolean[size];
        for(int i = 0; i < size; ++i) {
            row[i] = false;
            col[i] = false;
        }
        @SuppressWarnings("unchecked")
        SimpleEntry<Integer, Integer> [] pos = new SimpleEntry[size];
        if (backtrackingCost(pos, 0, 0, size, row, col, 0)) {
            //Como mucho habrá k-zeros
            for (int k = 0; k < size; ++k) {
                SimpleEntry<Integer, Integer> p = pos[k];
                int i = p.getKey();
                int j = p.getValue();
                opt += ini[i][j];
            }
        }
    }

    // Se inicializan las variables de los atributos de la clase
    private void initializeValues(double [][] ini) 
    {
        // Inicialització de les variables
        size = ini[0].length;
        res = new double [size][size]; 

        // Inicialització de la matriu
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) 
                res[i][j] = ini[i][j];

        idrows = new boolean [size]; 
        idcols = new boolean [size]; 
        initializeLines();
    }
    
    // Se inicializan las lineas de cada fila y columna a false (no seleccionadas)
    private void initializeLines()
    {
        for (int i = 0; i < size; ++i) {
            idrows[i] = false; idcols[i] = false;
        }
    }


    // OTHER METHODS 

    // Algoritmo de backtracking del calculo de lineas minimo
    // Pre: True
    // Post: Se seleccionara el minimo numero de lineas para recubrir todos los ceros
    private static void backtrackingLines(int[] combination, int i, int max) {
        if (i == max) {
            // Partiremos el vector de 2N = | idr | idc |, ocupando cada parte N
            // Por ejemplo: Sease la combinacion: 01011000
            //   -> Significa que idr = 0101 | idc = 1000 => 
            //        - Rows selected: idr = {false, true, false, true}
            //        - Cols selected: idc = {true, false, false, false}
            int size = max/2;
            boolean[] idr = new boolean[size];
            boolean[] idc = new boolean[size];
            for (int k = 0; k < size; ++k) if (combination[k] == 1) idr[k] = true;
            for (int k = size; k < max; ++k) if (combination[k] == 1) idc[k-size] = true;
            
            int lines = 0;
            for (int k = 0; k < size; ++k) {
                if (idr[k]) ++lines;
                if (idc[k]) ++lines;
            }

            // Encontramos el numero de lineas para marcar, pero que no haya ningun cero por marcar todavia
            if (lines < nlines && getNumZeros(size, idr, idc) == 0) {
                nlines = lines;
                idrows = Arrays.copyOf(idr, size);
                idcols = Arrays.copyOf(idc, size);
            } 
        } 
        else {
            // Combinacion de 0 y 1 para despues relacionarlas con las filas y columnas a escoger
            combination[i] = 1;
            backtrackingLines(combination, i+1, max);
            combination[i] = 0;
            backtrackingLines(combination, i+1, max);
        }
    }


    // Backtracking de la asignacion para el coste. 
    // Pre: La matriz contiene el numero de lineas minimo con valor N
    // Post: Se asignara un cero para cada fila y columna, siendo estas diferentes entre si
    private static boolean backtrackingCost(SimpleEntry<Integer, Integer> [] pos, int i, int act, int size, boolean [] row, boolean [] col, int k) 
    {
        if (act == size) return true;
        else {
            for (int j = 0; j < size; ++j) {
                if (!row[i] && !col[j] && res[i][j] == 0) {
                    row[i] = true;
                    col[j] = true;
                    pos[k] = new SimpleEntry<>(i,j);
                    if (backtrackingCost(pos, i+1, act+1, size, row, col, k+1)) return true;
                    row[i] = false;
                    col[j] = false;
                }
            }
        }
        return false;
    }

    /* Algoritmo greedy de asignación de LINEAS MINIMAS (falta demostración)
     * Author: David García Arévalo ©

    // VARIABLES · · · · · · · · · · · · · · · · · · · · · · · · · · · · · 
    private int nzeros;
    private static int [][] D;              //matriu amb les linies marcades

    private static int [] zrows;                   //cantidad de 0's por fila
    private static int [] zcols;                   //cantidad de 0's por columna

    // METODOS · · · · · · · · · · · · · · · · · · · · · · · · · · · · · · 
    // Actualiza la matriz de lineas solapadas, calcula el numero de lineas min
    public void getLinesMin() 
    {
        // Inicializa las filas/cols prev. seleccionadas
        inicialitzaLinies();

        Pair<Integer, Pair<Boolean, Integer>>[] v = new Pair[2*size];
        for (int i = 0; i < size; ++i) {
            v[i] = new Pair<> (zrows[i], new Pair<>(true, i));     //true --> rows
            v[size+i] = new Pair<> (zcols[i], new Pair<>(false, i));    //false --> cols
        }
        Arrays.sort(v, Comparator.comparing(Pair::getKey, Comparator.reverseOrder()));
        //for (int o = 0; o < 2*size; ++o) System.out.print(v[o] + "   ");
        //System.out.print("\n\n");

        nlines = 0;
        int zcovered = 0;
        while (zcovered < nzeros) {
            Pair<Integer, Pair<Boolean, Integer>> p = v[0];   
            boolean fila = p.getValue().getKey();
            Integer i = p.getValue().getValue();
            zcovered += p.getKey();
            v[0] = new Pair<> (0, new Pair<>(fila, i));         // lo pondre al final del todo
            Arrays.sort(v, Comparator.comparing(Pair::getKey, Comparator.reverseOrder()));
            //for (int o = 0; o < 2*size; ++o) System.out.print(v[o] + "   ");
            //System.out.print("\n\n");

            // FILA
            if (fila) {
                idrows[i] = true;
                for (int k = 0; k < size; ++k) ++D[i][k];   //marcas la fila
                //Le quito el 0 a esas columnas
                for (int l = 0; l < 2*size; ++l) {
                    Pair<Integer, Pair<Boolean, Integer>> objetivo = v[l];
                    boolean b = objetivo.getValue().getKey(); // true --> row // false --> col
                    Integer x = objetivo.getValue().getValue();
                    if (!b && res[i][x] == 0) {
                        Integer key = objetivo.getKey();
                        v[l] = new Pair<> (--key, new Pair<>(false, x));     //true --> rows                            
                    }
                }
                Arrays.sort(v, Comparator.comparing(Pair::getKey, Comparator.reverseOrder()));
            }

            // COLUMNA
            else {
                idcols[i] = true;
                for (int k = 0; k < size; ++k) ++D[k][i];   //marcas la columna 
                //Le quito el 0 a esas filas
                for (int l = 0; l < 2*size; ++l) {
                    Pair<Integer, Pair<Boolean, Integer>> objetivo = v[l];
                    boolean b = objetivo.getValue().getKey(); // true --> row // false --> col
                    Integer x = objetivo.getValue().getValue();
                    if (b && res[x][i] == 0) {
                        Integer key = objetivo.getKey();
                        v[l] = new Pair<> (--key, new Pair<>(true, x));     //true --> rows                        
                    }
                }                                           
                Arrays.sort(v, Comparator.comparing(Pair::getKey, Comparator.reverseOrder()));
            }
        }

        // Determinar el numero de lineas minimas a pintar
        for (int i = 0; i < size; ++i) {
            if (idrows[i]) ++nlines;
            if (idcols[i]) ++nlines;
        }
    }
    */

    
    // PARA COMPROBAR EJECUCION (a parte del fichero de test)

    // Pinta la matriz resultante después de todos los cálculos
    public void printMatriu() 
    {
        System.out.print("Resultado: \n");
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                System.out.print(res[i][j] + "     ");
            }
            System.out.println("\n");
        }
    }

    // Printa los datos (lineas, vectores de booleanos de lineas para fila y columna, coste) 
    public void printDades() 
    {
        System.out.print("\n");
        System.out.println("Nlines: " + nlines + "\n");

        System.out.println("Lineas marcadas: \n");
        System.out.print("      ");
        for (int i = 0; i < size; ++i) System.out.print(idcols[i] + "   ");
        for (int i = 0; i < size; ++i) System.out.print("\n" + idrows[i] + "\n");
        System.out.print("      \n");

        System.out.print("\n");
        System.out.println("Cost òptim: " + opt + "\n\n\n");
    }
}

