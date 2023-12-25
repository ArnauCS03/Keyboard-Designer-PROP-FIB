package Persistencia;

import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class D_GuardarLlistaParaulesFrequencies {


    //Atributo: Listado de listasPalabras con su respectivo identificador
    HashMap<String, String> listasPalabras;
    int num_llistaParaulesFrecuencias;


    //Singleton
    private D_GuardarLlistaParaulesFrequencies() {
        listasPalabras = new HashMap<>();
        num_llistaParaulesFrecuencias = 0;
    }
    private static class singletonCreatorLlistaParaules {
        private static final D_GuardarLlistaParaulesFrequencies instancia = new D_GuardarLlistaParaulesFrequencies();
    }
    public static D_GuardarLlistaParaulesFrequencies getInstancia() {
        return singletonCreatorLlistaParaules.instancia;
    }

    // Metodos
    //guarda y modifica listasPalabras
    public void guarda_ListaPalabras(String id, String listaPalabras) {
        if(! listasPalabras.containsKey(id)) num_llistaParaulesFrecuencias++;
        listasPalabras.put(id, listaPalabras);
    }
    public void elimina_ListaPalabras (String id) {
        if( listasPalabras.containsKey(id)) {
            num_llistaParaulesFrecuencias--;
            listasPalabras.remove(id);
        }
    }
    public void elimina_todo () {
        num_llistaParaulesFrecuencias = 0;
        listasPalabras.clear();
    }
    public String getListaPalabras(String id) {

        return listasPalabras.get(id);
    }
    public int getNum_ListaPalabras() {
        return num_llistaParaulesFrecuencias;
    }

    public boolean existe_listaPalabras(String id) {
        return listasPalabras.containsKey(id);
    }

    // Método para cargar datos desde un Hashmap a un  archivo de listaPalabras
    public void guardarListaPalabrasComoArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../FONTS/Persistencia/ListasPalabras.txt"))) {
            for ( Map.Entry<String, String> entry : listasPalabras.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Método para cargar datos desde un archivo de listaPalabras a un HashMap
    public void cargarListaPalabrasDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("../FONTS/Persistencia/ListasPalabras.txt"))) {
            String linea;

            while ((linea = reader.readLine()) != null) {

                int indicePrimerIgual = linea.indexOf("="); // Encontrar el primer igual
                String clave = linea.substring(0, indicePrimerIgual);
                String valor = linea.substring(indicePrimerIgual + 1);
                listasPalabras.put(clave, valor);

            }

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }




}
