package JUnit;

import java.util.*;

import Domini.ProcesarTexto;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ProcesarTextoTest {

    private TreeMap<Character, Integer> expectedTM, tm1, tm2;
    private double[][] expectedMat, matF;

    public ProcesarTextoTest(double[][] e, double[][] m) {
        this.expectedMat = e; this.matF = m;
    }

    @Parameters
    public static Collection<Object[]> getTestParameters () {
        ProcesarTexto da = new ProcesarTexto();
        String texto = "abcd abcd aabbcc";
        da.setMatFlujIndiceFreq(texto);

        double[][] mf = { {0.25, 0.75, 0.0, 0.0, 0.0},
                          {0.0, 0.25, 0.75, 0.0, 0.0},
                          {0.0, 0.0, 0.3333333333333333, 0.6666666666666666, 0.0},
                          {0.0, 0.0, 0.0, 0.0, 1.0},
                          {1.0, 0.0, 0.0, 0.0, 0.0} };

        // unico char un espacio
        ProcesarTexto da1 = new ProcesarTexto(" ");
        double[][] mf2 = {{0.0}};

        // Todo misma letra y un espacio
        String aaa = "AAaaaaaAAaa AAaaaAAaa";
        double [][] mf3 = { {0.9473684210526315, 0.05263157894736842},
                            {1.0, 0.0}};
        ProcesarTexto da2 = new ProcesarTexto(aaa);

        // Considerablemente largo el texto para matriz 11x11
        String texto2 = "aaaa bbbb cccc dddd eeee ffff gggg hhhh iiii abacadbfjghh";
        double[][] mf4 = { {0.42857142857142855, 0.14285714285714285, 0.14285714285714285, 0.14285714285714285, 0.14285714285714285, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                           {0.1111111111111111, 0.0, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.1111111111111111, 0.0},
                           {0.16666666666666666, 0.16666666666666666, 0.5, 0.0, 0.0, 0.0, 0.16666666666666666, 0.0, 0.0, 0.0, 0.0},
                           {0.2, 0.2, 0.0, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                           {0.0, 0.2, 0.2, 0.0, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                           {0.0, 0.25, 0.0, 0.0, 0.0, 0.75, 0.0, 0.0, 0.0, 0.0, 0.0},
                           {0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.6, 0.0, 0.0, 0.0, 0.2},
                           {0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.6, 0.2, 0.0, 0.0},
                           {0.0, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.0, 0.0},
                           {0.0, 0.25, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.75, 0.0},
                           {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0}};
        ProcesarTexto da3 = new ProcesarTexto(texto2);

        return Arrays.asList(new Object[][] {
            { mf, da.getMatrizFlujos()},
            { mf2, da1.getMatrizFlujos()},
            { mf3, da2.getMatrizFlujos()},
            { mf4, da3.getMatrizFlujos()}});
    }

    @Test
    public void TestSetMatrizFlujos() {
        assertEquals(expectedMat, matF);
    }


    @Test
    public void TestModifcarMatFlujoConAlfabeto() {
        String text = "abcd";
        char[] alfab = {'a','b','c','d','e'};
        ProcesarTexto t = new ProcesarTexto();
        // Generamos una primera matriz
        t.setMatFlujIndiceFreq(text);
        // La modificamos añadiendo las filas y columnas de las letras que no aparezcan en el texto
        t.modificarMatFlujoConAlfabeto(alfab, text);

        double[][] res = t.getMatrizFlujos();

        double[][] expected = {{0.0, 1.0, 0.0, 0.0, 0.0},
                               {0.0, 0.0, 1.0, 0.0, 0.0},
                               {0.0, 0.0, 0.0, 1.0, 0.0},
                               {0.0, 0.0, 0.0, 0.0, 0.0},
                               {0.0, 0.0, 0.0, 0.0, 0.0}};

        String text2 = " ";
        ProcesarTexto t2 = new ProcesarTexto();
        t2.setMatFlujIndiceFreq(text2);
        char[] alfab2 = {'a'};
        t2.modificarMatFlujoConAlfabeto(alfab2, text2);
        double[][] expected2 = {{0.0, 0.0}, {0.0, 0.0}};
        double[][] res2 = t2.getMatrizFlujos();

        assertEquals(expected, res);
        assertEquals(expected2,res2);
    }
}
