package Persistencia;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class D_GuardarTexts {


    //Atributo: Listado de textos con su respectivo identificador
    HashMap<String, String> textos;
    int num_texts;


    //Singleton
    private D_GuardarTexts() {
        textos = new HashMap<>();
        num_texts = 0;
    }
    private static class singletonCreatorTexts {
        private static final D_GuardarTexts instancia = new D_GuardarTexts();
    }
    public static D_GuardarTexts getInstancia() {
        return singletonCreatorTexts.instancia;
    }

    // Metodos
    //guarda y modifica textos
    public void guarda_Texto (String id, String texto) {
        if(! textos.containsKey(id)) num_texts++;
        textos.put(id, texto);
    }
    public void elimina_Texto (String id) {
        if( textos.containsKey(id)) {
            num_texts--;
            textos.remove(id);
        }
    }
    public void elimina_todo () {
        num_texts = 0;
        textos.clear();
    }
    public String getTexto(String id) {
        return textos.get(id);
    }
    public int getNum_texts() {
        return num_texts;
    }

    public boolean existe_texto(String id) {
        return textos.containsKey(id);
    }

    // Método para cargar datos desde un Hashmap a un  archivo de texto
    public void guardarTextoComoArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../FONTS/Persistencia/Textos.txt"))) {
            for ( Map.Entry<String, String> entry : textos.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Método para cargar datos desde un archivo de texto a un HashMap
    public void cargarTextoDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("../FONTS/Persistencia/Textos.txt"))) {
            String linea;


            while ((linea = reader.readLine()) != null) {

                //en caso de que el texto contenga un simbolo '=' juntamos las distintas partes
                int indicePrimerIgual = linea.indexOf("="); // Encontrar el primer igual
                String clave = linea.substring(0, indicePrimerIgual);
                String valor = linea.substring(indicePrimerIgual + 1);
                textos.put(clave, valor);

            }

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }





}
