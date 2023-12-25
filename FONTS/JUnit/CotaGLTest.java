package JUnit;

import Domini.A_cotaGilmoreLawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Arrays;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/*
import org.mockito.*;
import org.mockito.Mockito;
*/

// ===================================================================================================
// Test de JUnit (unit test: solo los metodos) para la clase A_cotaGilmoreLawler
//
//      La fucnionalidad principal es el metodo cota() que devuelve un double que representa
//      el coste de poner una tecla no colocada en el teclado, con respecto las teclas ya
//      colocadas entre ellas, lo que colocamos con las ya colocadas y las por colocar entre ellas.
//
//  Los tests de JUnit prueban la clase A_cotaGilmoreLawler.java independientemente de las otras por
//  eso  hace falta tambien hacer un mock del A_hungaria, pero no me ha funcionado (usando mockito).
//
// ===================================================================================================

@RunWith(Parameterized.class)
public class CotaGLTest {

    private List<Character>  teclesColocades;
    private HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocadesIndexPos;
    private double[][] flows;
    private HashMap<Character, Integer> indexTeclesMatriuFlux;
    private int numTeclesTotal;
    private List<Character>  teclesPerColocar;
    private List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades;


    /*
    // Mocks
    private A_hungarian hungMock;
    private double costOptim;
    */

    // Constructora para tener todos los parametros (algunos eran necesarios para hacer tests de otros metodos privados que ya no hago, se encuentran comentados al final del fichero)
    public CotaGLTest(List<Character>  teclesColocades, HashMap<Character, Integer> indexTeclesMatriuFlux, HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocadesIndexPos,
                      double[][] flows, int numTeclesTotal, List<Character>  teclesPerColocar, List<SimpleEntry<Integer, Integer>> indexPosicionsSenseTeclesColocades ) {
        this.teclesColocades = teclesColocades;
        this.indexTeclesMatriuFlux = indexTeclesMatriuFlux;
        this.teclesColocadesIndexPos = teclesColocadesIndexPos;
        this.flows = flows;
        this.numTeclesTotal = numTeclesTotal;
        this.teclesPerColocar = teclesPerColocar;
        this.indexPosicionsSenseTeclesColocades = indexPosicionsSenseTeclesColocades;
    }


    @BeforeClass
    public static void antesDeTodasLasClases() throws Exception {
        System.out.println("\nInicio de los tests");
    }

    @AfterClass
    public static void despuesDeTodasLasClases() throws Exception {
        System.out.println("\nFin de los tests, EXITO!");
    }


    // para poner los parametros de cada test, y esta parte se encarga de llamar a la constructora y inicializar los parametros
    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        HashMap<Character, Integer> indexMatFlux = new HashMap<>();
        indexMatFlux.put('a', 0); indexMatFlux.put('b', 1);
        indexMatFlux.put('c', 2); indexMatFlux.put('d', 3);

        HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocPos = new HashMap<>();
        teclesColocPos.put('a', new SimpleEntry<>(0, 0)); teclesColocPos.put('b', new SimpleEntry<>(0, 1));

        double [][] fl = new double[][]{
                {0.5, 0.5, 0.0, 0.0},
                {0.5, 0.0, 0.5, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0}};

        List<SimpleEntry<Integer, Integer>> indexPosNoColoc = new ArrayList<>();
        indexPosNoColoc.add(new SimpleEntry<>(1, 0)); indexPosNoColoc.add(new SimpleEntry<>(1, 1));

        // cada fila contiene los parametros de cada test individual de mas abajo (algunos contienen de mas, pero son para la constructora general del test
        return Arrays.asList(new Object[][] {
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc } // testCota
        });
    }


    // test mas general, el metodo cota(), es el que devuelve el double de la cota de Gilmore-Lawler
    @Test
    public void testCota() {
        double resulat = A_cotaGilmoreLawler.cota(teclesColocades, indexTeclesMatriuFlux, teclesColocadesIndexPos, flows, numTeclesTotal, teclesPerColocar, indexPosicionsSenseTeclesColocades);

        assertEquals("Test calcul Cota Gilmore-Lawler 1", 2.0, resulat, 0.001);   // esta hecho a mano la cota de Gilmore-Lawler para comprobar que da 2.0 el coste

        System.out.println("Primer test superado");

        // Segundo test:

        // El teclado colocado podria ser este: {'d', 'e', 'libre'}  Y con un texto:  dfeefded   podemos sacar la matriz de flujos normalizada
        HashMap<Character, Integer> indexMatFlux = new HashMap<>();
        indexMatFlux.put('d', 0); indexMatFlux.put('e', 1);
        indexMatFlux.put('f', 2);

        HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocPos = new HashMap<>();
        teclesColocPos.put('d', new SimpleEntry<>(0, 0)); teclesColocPos.put('e', new SimpleEntry<>(0, 1));

        double [][] fl = new double[][]{
                {0.0,     0.5,     0.5},
                {1.0/3.0, 1.0/3.0, 1.0/3.0},
                {0.5,     0.5,     0.0}};

        List<SimpleEntry<Integer, Integer>> indexPosNoColoc = new ArrayList<>();
        indexPosNoColoc.add(new SimpleEntry<>(0, 2));

        resulat = A_cotaGilmoreLawler.cota(List.of('d','e'), indexMatFlux, teclesColocPos, fl, 3, List.of('f'), indexPosNoColoc);

        assertEquals("Test calcul Cota Gilmore-Lawler 2", 3.333333333333333, resulat, 0.001);

        System.out.println("Segundo test superado");
    }


    /*
    // Test de la cota amb el cost optim --> MOCK
    @Test
    public void testCostHungarian() {
        System.out.println("Iniciant mock amb l'Hungarian ...");

        // Mock per l'Hungarian
        hungMock = Mockito.mock(A_hungarian.class);
        Mockito.when(hungMock.getCost()).thenReturn(2.0);

        double resultado = A_cotaGilmoreLawler.cota(teclesColocades, indexTeclesMatriuFlux, teclesColocadesIndexPos, flows, numTeclesTotal, teclesPerColocar, indexPosicionsSenseTeclesColocades);
        assertEquals("El coste no es el mismo", costOptim, resultado, 0.001);
        System.out.println("Mock realitzat!");
    }
    */


    // Antes pensaba que habia que hacer test de todos los metodos, pero como habia algunos privados los puse publicos, y hice tests de mas, que no serian necesarios
    // Aqui estan todos los otros tests:
    /*
    @Test
    public void testDistanciaEucl() {

        double resulat = A_cotaGilmoreLawler.distanciaEucl(1, 2, 3, 4);
        assertEquals("Test calcul distancia Euclideana", 8.0, resulat, 0.001); // tercer parametro, es una tolerancia, va bien si hay errores redondeo de doubles

        resulat = A_cotaGilmoreLawler.distanciaEucl(-1, 8, 2, 9);
        assertEquals("Test calcul distancia Euclideana", 10.0, resulat, 0.001);
    }


    // para poner los parametros de cada test, y esta parte se encarga de llamar a la constructora y inicializar los parametros
    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        HashMap<Character, Integer> indexMatFlux = new HashMap<>();
        indexMatFlux.put('a', 0); indexMatFlux.put('b', 1);
        indexMatFlux.put('c', 2); indexMatFlux.put('d', 3);

        HashMap<Character, SimpleEntry<Integer, Integer>> teclesColocPos = new HashMap<>();
        teclesColocPos.put('a', new SimpleEntry<>(0, 0)); teclesColocPos.put('b', new SimpleEntry<>(0, 1));

        double [][] fl = new double[][]{
                {0.5, 0.5, 0.0, 0.0},
                {0.5, 0.0, 0.5, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0}};

        List<SimpleEntry<Integer, Integer>> indexPosNoColoc = new ArrayList<>();
        indexPosNoColoc.add(new SimpleEntry<>(1, 0)); indexPosNoColoc.add(new SimpleEntry<>(1, 1));

        // cada fila contiene los parametros de cada test individual de mas abajo (algunos contienen de mas, pero son para la constructora general del test
        return Arrays.asList(new Object[][] {
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc }, // testCota0
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc }, // testmatC1
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc }, // testmatC2
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc }, // testPrecalcularTrafico
                { List.of('a','b'), indexMatFlux, teclesColocPos, fl, 4, List.of('c','d'), indexPosNoColoc }  // testPrecalcularDistancias
        });
    }


     @Test
    public void testCota0() {

        double resulat = A_cotaGilmoreLawler.cota0(teclesColocades, indexTeclesMatriuFlux, teclesColocadesIndexPos, flows);

        assertEquals("Test calcul cota0", 1.0, resulat, 0.001);
    }


    @Test
    public void testmatC1() {

        double [][] resultat = A_cotaGilmoreLawler.matC1(numTeclesTotal, teclesColocades, teclesColocadesIndexPos, teclesPerColocar, indexPosicionsSenseTeclesColocades, flows, indexTeclesMatriuFlux);

        double [][] matEsperada = new double[][] {
                {2, 1},
                {0, 0}};

        assertArrayEquals("Test calcul matriu C1", matEsperada, resultat);
    }


    @Test
    public void testmatC2() {

        double [][] resultat = A_cotaGilmoreLawler.matC2(numTeclesTotal, 2, teclesPerColocar, indexPosicionsSenseTeclesColocades, flows, indexTeclesMatriuFlux);

        double [][] matEsperada = new double[][] { // da 0, porque al ser solo dos teclas por colocar y la distancia a si misma es 0, el dot product da 0
                {0, 0},
                {0, 0}};

        assertArrayEquals("Test calcul matriu C1", matEsperada, resultat);
    }

    @Test
    public void testPrecalcularTrafico() {
        double [][] resultat = A_cotaGilmoreLawler.precalcularTrafico(2, indexTeclesMatriuFlux, teclesPerColocar, flows);

        double [][] matEsperada = new double[][] {
                {0.0, 0.0},     // flujo de 'c' al resto de las no colocadas
                {1.0, 0.0}};    // flujo de 'd' al resto de las no colocadas

        assertArrayEquals("Test matriz de trafico precalculada", matEsperada, resultat);
    }


    @Test
    public void testPrecalcularDistancias() {
        double [][] resultat = A_cotaGilmoreLawler.precalcularDistancias(2, indexPosicionsSenseTeclesColocades);

        double [][] matEsperada = new double[][] {
                {0.0, 1.0},     // distancia de la posicion no colocada inicial a otras no colocadas
                {1.0, 0.0}};

        assertArrayEquals("Test matriz de trafico precalculada", matEsperada, resultat);
    }


    @Test
    public void testSumaMatrius() {
        double [][] mat1 = new double[][] {
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0} };
        double [][] mat2 = new double[][] {
                {2.0, 2.0, 2.0, 2.0},
                {2.0, 2.0, 2.0, 2.0},
                {2.0, 2.0, 2.0, 2.0},
                {2.0, 2.0, 2.0, 2.0} };


        double [][] resultat = A_cotaGilmoreLawler.sumaMatrius(mat1, mat2);

        double [][] matEsperada = new double[][] {
                {3.0, 3.0, 3.0, 3.0},
                {3.0, 3.0, 3.0, 3.0},
                {3.0, 3.0, 3.0, 3.0},
                {3.0, 3.0, 3.0, 3.0} };

        assertArrayEquals("Test suma de dues matrius", matEsperada, resultat);
    }

    @Test
    public void testDotProduct() {
        double [] v1 = new double[] {1.0, 2.0, 3.0};
        double [] v2 = new double[] {4.0, 5.0, 6.0};

        double resultat = A_cotaGilmoreLawler.dotProduct(v1, v2);

        double resuladoEsperado =  32.0;

        assertEquals("Test porducto escalar entre dos vectores", resuladoEsperado, resultat, 0.0001);
    }

    */
}
