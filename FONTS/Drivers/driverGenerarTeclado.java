package Drivers;

import Domini.A_ControladorCapaDominio;
import Excepcions.ExcepFichero;

import java.util.Scanner;


/*
    DRIVER interactivo para poder Generar un Teclado, leyendo la informacion de un fichero

     En ejecutar el driverGenerarTeclado.class, introducir si se quiere leer los datos de teminal o fichero y ya se genera automaticamente el teclado

     Cuando se ejecute el driverGenerarTeclado.class:

    - Objeto de la prueba: buen funcionamento del algoritmo
    - Ficheros de datos necesarios: deben estar en la carpeta EXE/Driver, se indica el nombre del fichero a leer interactivamente por la terminal, tener cada tipo de input en un fichero separado
    - Operativa: la descripcion del funcionamiento es: en ejecutarlo, introducir el modo de input de los ficheros (ya se indican instrucciones al ejecutar)
                 y ya se genera automaticamente el teclado y se imprime por la terminal
 */

public class driverGenerarTeclado {

    public static void main(String [] args) throws ExcepFichero {

        A_ControladorCapaDominio _ctrlCapaDominio = new A_ControladorCapaDominio();

        Scanner scanner = new Scanner(System.in);

        System.out.println("* * * * * * * * * *");
        System.out.println("Para evitar errores en el formato del input y poder gestionar excepciones, se lee el input de ficheros, seguir las instrucciones:");
        System.out.println("* * * * * * * * * *");

        // leer input
        System.out.println("Introducir un numero que representa el modo de lectura de los ficheros:\n1 texto\n2 lista palabras con frecuencia aparicion\n3 alfabeto + otra Opcion (se indicara posteriormente instrucciones)");
        char opcion = scanner.next().charAt(0);


        if (opcion == '1') { // texto

            System.out.println("* * * * * * * * * *");
            System.out.println("Introcuce un numero que indica uno de los juegos de prueba:\n1 texto.txt  2 texto2vacio.txt  3 texto3formatoerror.txt  4 texto4.txt");
            System.out.println("Si quieres usar tu propio fichero, introduce  0");

            char numFicheroTexto = scanner.next().charAt(0);
            scanner.nextLine();

            String nombreFichero = "dummy.txt";

            if (numFicheroTexto == '0') {
                System.out.println("Asegurate que el fichero de prueba que quieres usar, este en la carpeta EXE/Driver");
                System.out.println("\nIntroduce el nombre completo del archivo texto con su extension y pulsa enter: Ej: customTexto.txt");

                nombreFichero = scanner.nextLine().trim(); // nombre fichero texto del usuario
            }
            else {
                if (numFicheroTexto == '1')      nombreFichero = "texto.txt";
                else if (numFicheroTexto == '2') nombreFichero = "texto2vacio.txt";
                else if (numFicheroTexto == '3') nombreFichero = "texto3formatoerror.txt";
                else if (numFicheroTexto == '4') nombreFichero = "texto4.txt";
                else {
                    System.out.println("\nOpcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                    return;
                }
            }
            // lectura fichero
            _ctrlCapaDominio.leerDeFicheroTexto(nombreFichero);  // Se lee el texto y tambien se procesa para que si desde el algoritmo necesita por ejemplo matriz flujos se la pueda dar
        }

        else if (opcion == '2') {  // lista palabras con freq aparicion

            System.out.println("* * * * * * * * * *");
            System.out.println("Introcuce un numero que indica uno de los juegos de prueba:\n1 frecpalabras.txt  2 frecpalabras2vacio.txt  3 frecpalabras3formatoerror.txt");
            System.out.println("\nSi quieres usar tu propio fichero, introduce  0");

            char numFicheroFrecPalab = scanner.next().charAt(0);
            scanner.nextLine();

            String nombreFichero = "dummy.txt";

            if (numFicheroFrecPalab == '0') {
                System.out.println("Asegurate que el fichero de prueba que quieres usar, este en la carpeta EXE/Driver");
                System.out.println("\nIntroduce el nombre completo del archivo de palabras con frecuencia de aparicion con su extension y pulsa enter: Ej: customFrecpalabras.txt");

                nombreFichero = scanner.nextLine(); // nombre fichero lista palab del usuario
            }
            else {
                if (numFicheroFrecPalab == '1')      nombreFichero = "frecpalabras.txt";
                else if (numFicheroFrecPalab == '2') nombreFichero = "frecpalabras2vacio.txt";
                else if (numFicheroFrecPalab == '3') nombreFichero = "frecpalabras3formatoerror.txt";
                else {
                    System.out.println("\nOpcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                    return;
                }
            }
            // lectura fichero
            _ctrlCapaDominio.leerDeFicheroFrecPalabras(nombreFichero);
        }

        else if (opcion == '3') {

            System.out.println("* * * * * * * * * *");
            System.out.println("Introcuce un numero que indica uno de los juegos de prueba:\n1 alfabeto.txt  2 alfabeto2vacio.txt  3 alfabeto3formatoerror.txt");
            System.out.println("\nSi quieres usar tu propio fichero, introduce  0");

            char numFicheroAlfabeto = scanner.next().charAt(0);
            scanner.nextLine();

            String nombreFichero = "dummy.txt";

            // Usuari intodcuce su fichero alfabeto
            if (numFicheroAlfabeto == '0') {
                System.out.println("Asegurate que el fichero de prueba que quieres usar, este en la carpeta EXE/Driver");
                System.out.println("\nIntroduce el nombre completo del archivo de alfabeto con su extension y pulsa enter: Ej: customalfabeto.txt");

                nombreFichero = scanner.nextLine(); // nombre fichero alfabeto del usuario
            }
            else { // usar alfabeto juego pruebas
                if (numFicheroAlfabeto == '1')      nombreFichero = "alfabeto.txt";
                else if (numFicheroAlfabeto == '2') nombreFichero = "alfabeto2vacio.txt";
                else if (numFicheroAlfabeto == '3') nombreFichero = "alfabeto3formatoerror.txt";
                else {
                    System.out.println("Opcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                    return;
                }
            }

            // lectura fichero alfabeto
            _ctrlCapaDominio.leerDeFicheroAlfabeto(nombreFichero);


            // ===================================================================================================================================================================================================


            System.out.println("\nYa se ha leido el alfabeto pero el solo no se puede generar el teclado\nIntroducir: 't'  para leer texto o  'l'  para lista de palabras con frecuencia de aparicion");

            // Leer despues del alfabeto:   texto  o   lista palabras con freq aparicion

            char tipoTexto = scanner.next().charAt(0);

            if (tipoTexto == 't') { // Texto

                System.out.println("Introcuce un numero que indica uno de los juegos de prueba:\n1 texto.txt  2 texto2vacio.txt  3 texto3formatoerror.txt  4 texto4.txt");
                System.out.println("\nSi quieres usar tu propio fichero, introduce  0");

                char numFicheroTexto = scanner.next().charAt(0);
                scanner.nextLine();

                String nombreFicheroSegundo = "dummy2.txt";

                if (numFicheroTexto == '0') {  // user introduce su nombre de fichero
                    System.out.println("Asegurate que el fichero de prueba que quieres usar, este en la carpeta EXE/Driver");
                    System.out.println("\nIntroduce el nombre completo del archivo texto con su extension y pulsa enter: Ej: customTexto.txt");

                    nombreFicheroSegundo = scanner.nextLine(); // nombre fichero texto del usuario
                }
                else {

                    System.out.println("\nEn algun caso puede tardar mucho, no es que se quede colgado.  Cargando...");

                    if (numFicheroTexto == '1')      nombreFicheroSegundo = "texto.txt";
                    else if (numFicheroTexto == '2') nombreFicheroSegundo = "texto2vacio.txt";
                    else if (numFicheroTexto == '3') nombreFicheroSegundo = "texto3formatoerror.txt";
                    else if (numFicheroTexto == '4') nombreFicheroSegundo = "texto4.txt";
                    else {
                        System.out.println("Opcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                        return;
                    }
                }
                // lectura fichero
                _ctrlCapaDominio.leerDeFicheroTextoDespuesAlfabeto(nombreFicheroSegundo);  // Se lee el texto y tambien se procesa para que si desde el algoritmo necesita por ejemplo matriz flujos se la pueda dar
            }

            // lista palabras con freq aparicion
            else if (tipoTexto == 'l') {

                System.out.println("Introcuce un numero que indica uno de los juegos de prueba:\n1 frecpalabras.txt  2 frecpalabras2vacio.txt  3 frecpalabras3formatoerror.txt");
                System.out.println("\nSi quieres usar tu propio fichero, introduce  0");

                char numFicheroFrecPalab = scanner.next().charAt(0);
                scanner.nextLine();

                String nombreFicheroSegundo = "dummy.txt";

                if (numFicheroFrecPalab == '0') {
                    System.out.println("Asegurate que el fichero de prueba que quieres usar, este en la carpeta EXE/Driver");
                    System.out.println("\nIntroduce el nombre completo del archivo de palabras con frecuencia de aparicion con su extension y pulsa enter: Ej: customFrecpalabras.txt");

                    nombreFicheroSegundo = scanner.nextLine(); // nombre fichero lista palab del usuario
                }
                else {

                    System.out.println("\nEn algun caso puede tardar mucho, no es que se quede colgado.  Cargando...");

                    if (numFicheroFrecPalab == '1')      nombreFicheroSegundo = "frecpalabras.txt";
                    else if (numFicheroFrecPalab == '2') nombreFicheroSegundo = "frecpalabras2vacio.txt";
                    else if (numFicheroFrecPalab == '3') nombreFicheroSegundo = "frecpalabras3formatoerror.txt";
                    else {
                        System.out.println("\nOpcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                        return;
                    }
                }

                // lectura fichero
                _ctrlCapaDominio.leerDeFicheroPalabrasDespuesAlfabeto(nombreFicheroSegundo);  // Se lee el texto y tambien se procesa para que si desde el algoritmo necesita por ejemplo matriz flujos se la pueda dar
            }
            else {
                System.out.println("Opcion no correcta, se aborta la ejecucion, por favor vuelve a ejecutarlo con una de las opciones");
                return;
            }
        }


        // Fin lectura y tratamiento de los inputs para que el algoritmo pueda usarlos


        // ======================================================================================================================================================================


        // generar teclado
        _ctrlCapaDominio.generarTeclado();

        // imprimir teclado
        _ctrlCapaDominio.imprimeTeclado();
    }

}
