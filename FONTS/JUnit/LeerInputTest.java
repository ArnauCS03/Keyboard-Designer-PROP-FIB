package JUnit;

import Presentacio.P_LeerInput;
import Excepcions.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class LeerInputTest {

// 3 primeras funciones TEST sobre leerTexto -> fichero vacio, fichero erroneo el formato, ok
    @Test
    public void leerTextoTest() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/inputJU.txt";
        P_LeerInput li = new P_LeerInput();
        String expected = "abcd abcd aabbcc";
        li.leerTexto(archivo);
        String res = li.getTexto();
        assertEquals(expected, res);
    }

    @Test(expected = FicheroVacio.class)
    public void leerTextoTestVacio() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/emptyFile.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerTexto(archivo);
        String a = li.getTexto();
    }

    @Test(expected = FormatoErroneoTexto.class)
    public void leerTextoTestFormatoErroneo() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/formatoErroneoTexto.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerTexto(archivo);
        String a = li.getTexto();
    }

// 3 funciones TEST sobre leerMatriz -> fichero vacio, erroneo formato, ok
    // @Test
    // public void leerMatrizFlujoTest() throws ExcepFichero {
    //     String archivo = "inputJUMatriz.txt";
    //     P_LeerInput li = new P_LeerInput();
    //     double[][] expected = {{0.5,0.45},{0.25,0.75}};
    //     // indicar tamanyo matriz en parametro
    //     double[][] result = li.leerMatrizFlujo(archivo, 2);
    //     assertEquals(expected, result);
    // }

    // @Test(expected = FicheroVacio.class)
    // public void leerMatrizFlujoTestVacio() throws ExcepFichero {
    //     String archivo = "emptyFile.txt";
    //     P_LeerInput li = new P_LeerInput();
    //     double[][] a = li.leerMatrizFlujo(archivo, 2);
    // }

    // @Test(expected = FormatoErroneoMatriz.class)
    // public void leerMatrizFlujoTestFormatoErroneo() throws ExcepFichero {
    //     String archivo = "formatoErroneoMatriz.txt";
    //     P_LeerInput li = new P_LeerInput();
    //     double[][] a = li.leerMatrizFlujo(archivo, 2);
    // }

// 3 funciones TEST sobre leerAlfabeto -> fichero vacio, erroneo formato, ok
    @Test
    public void leerAlfabetoTest() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/inputJUAlfabeto.txt";
        P_LeerInput li = new P_LeerInput();
        char[] expected = {'a','b','c','d'};
        li.leerAlfabeto(archivo);
        char[] result = li.getAlfabeto();
        assertArrayEquals(expected, result);
    }

    @Test(expected = FicheroVacio.class)
    public void leerAlfabetoTestVacio() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/emptyFile.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerAlfabeto(archivo);
        char[] a = li.getAlfabeto();
    }

    @Test(expected = FormatoErroneoAlfabeto.class)
    public void leerAlfabetoTestFormatoErroneo() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/formatoErroneoAlfabeto.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerAlfabeto(archivo);
        char[] a = li.getAlfabeto();
    }

// // 3 funciones TEST sobre leerFrecPalabras -> fichero vacio, erroneo formato, ok
    @Test
    public void leerFrecPalabrasTest() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/inputJUFrecPalabras.txt";
        P_LeerInput li = new P_LeerInput();
        String expected = "atun atun atun hola hola";
        li.leerFrecPalabras(archivo);
        String result = li.getTextoFrecPalabras();
        assertEquals(expected, result);
    }

    @Test(expected = FicheroVacio.class)
    public void leerFrecPalabrasTestVacio() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/emptyFile.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerFrecPalabras(archivo);
        String a = li.getTextoFrecPalabras();
    }

    @Test(expected = FormatoErroneoFrecPalabras.class)
    public void leerFrecPalabrasTestFormatoErroneo() throws ExcepFichero {
        String archivo = "../../EXE/JUnit/formatoErroneoFrecPalabras.txt";
        P_LeerInput li = new P_LeerInput();
        li.leerFrecPalabras(archivo);
        String a = li.getTextoFrecPalabras();
    }
}
