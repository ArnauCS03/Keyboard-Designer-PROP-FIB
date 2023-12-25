package Persistencia;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class D_GuardarAlfabets {


    //Atributo: Listado de alfabetos con su respectivo identificador
    HashMap<String, char[]> alfabetos;
    int num_alfabets;


    //Singleton
    private D_GuardarAlfabets() {
        alfabetos = new HashMap<>();
        num_alfabets = 0;
    }
    private static class singletonCreatorAlfabets {
        private static final D_GuardarAlfabets instancia = new D_GuardarAlfabets();
    }
    public static D_GuardarAlfabets getInstancia() {
        return singletonCreatorAlfabets.instancia;
    }

    // Metodos
    //guarda y modifica alfabetos
    public void guarda_Alfabeto (String id, char[] alfabeto) {
        if(! alfabetos.containsKey(id)) num_alfabets++;
        alfabetos.put(id, alfabeto);
    }
    public void elimina_Alfabeto (String id) {
        if( alfabetos.containsKey(id)) {
            num_alfabets--;
            alfabetos.remove(id);
        }
    }
    public void elimina_todo () {
        num_alfabets = 0;
        alfabetos.clear();
    }

    public char[] getAlfabeto(String id) {
        return alfabetos.get(id);
    }
    public int getNum_alfabets() {
        return num_alfabets;
    }

    public boolean existe_alfabeto(String id) {
        return alfabetos.containsKey(id);
    }

    // Método para cargar datos desde un Hashmap a un  archivo de alfabeto
    public void guardarAlfabetoComoArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../FONTS/Persistencia/Alfabetos.txt"))) {
            for ( Map.Entry<String, char[]> entry : alfabetos.entrySet()) {
                writer.write(entry.getKey() + "=" );
                for(char c : entry.getValue()) writer.write(c);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Método para cargar datos desde un archivo de alfabeto a un HashMap
    public void cargarAlfabetoDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("../FONTS/Persistencia/Alfabetos.txt"))) {
            String linea;


            while ((linea = reader.readLine()) != null) {

                int indicePrimerIgual = linea.indexOf("="); // Encontrar el primer igual
                String clave = linea.substring(0, indicePrimerIgual);
                String valor = linea.substring(indicePrimerIgual + 1);
                char[] valores = valor.toCharArray();

                alfabetos.put(clave, valores);

            }

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }





}

