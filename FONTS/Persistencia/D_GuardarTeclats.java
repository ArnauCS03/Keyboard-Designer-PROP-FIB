package Persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class D_GuardarTeclats {


    //Atributo: Listado de teclados con su respectivo identificador
    HashMap<String, SimpleEntry<SimpleEntry<char[], int[][]>, Double> > teclados;

    int num_teclats;


    //Singleton
    private D_GuardarTeclats() {
        teclados = new HashMap<>();
        num_teclats = 0;
    }
    public void elimina_Teclado (String id) {
        if( teclados.containsKey(id)) {
            num_teclats--;
            teclados.remove(id);
        }
    }
    public void elimina_todo () {
        num_teclats = 0;
        teclados.clear();
    }
    private static class singletonCreatorTeclats {
        private static final Persistencia.D_GuardarTeclats instancia = new Persistencia.D_GuardarTeclats();
    }
    public static Persistencia.D_GuardarTeclats getInstancia() {
        return singletonCreatorTeclats.instancia;
    }

    // Metodos
    //guarda y modifica teclados
    public void guarda_Teclado (String id, char[] alfabeto,  int[][] posiciones, double coste) {
        if(! teclados.containsKey(id)) num_teclats++;
        teclados.put(id, new SimpleEntry<SimpleEntry<char[], int[][]>, Double>( new SimpleEntry<char[], int[][]>(alfabeto, posiciones), coste));
    }
    public char[] getDistribucionTeclado(String id) {
        return teclados.get(id).getKey().getKey();
    }
    public int[][] getPosiconesTeclado(String id) {
        return teclados.get(id).getKey().getValue();
    }
    public double getCosteTeclado(String id) {
        return teclados.get(id).getValue();
    }
    public int getNum_teclats() {
        return num_teclats;
    }

    public boolean existe_teclado(String id) {
        return teclados.containsKey(id);
    }

    public List<String> listado_teclados() {
        return (new ArrayList<>(teclados.keySet()));
    }

    // Método para cargar datos desde un Hashmap a un  archivo de teclado
    public void guardarTecladoComoArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../FONTS/Persistencia/Teclados.txt"))) {
            for ( Map.Entry<String, SimpleEntry <SimpleEntry <char[], int[][]>, Double>> entry : teclados.entrySet()) {
                writer.write(entry.getKey() + "=" );
                for(char c : entry.getValue().getKey().getKey()) writer.write(c);
                writer.newLine();
                boolean primero = true;
                for(int[] p : entry.getValue().getKey().getValue()) {
                    if(!primero) writer.write(" ");
                    else primero = false;
                    writer.write(p[0] + "," + p[1]);
                }
                writer.newLine();
                writer.write(Double.toString(entry.getValue().getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Método para cargar datos desde un archivo de teclado a un HashMap
    public void cargarTecladoDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("../FONTS/Persistencia/Teclados.txt"))) {
            String linea;


            while ((linea = reader.readLine()) != null) {

                int indicePrimerIgual = linea.indexOf("="); // Encontrar el primer igual
                String clave = linea.substring(0, indicePrimerIgual);
                String valor = linea.substring(indicePrimerIgual + 1);
                char[] alf = valor.toCharArray();
                linea = reader.readLine();
                String[] pos_cadena = linea.split(" ");
                int[][] pos = new int[pos_cadena.length][2];

                for (int i = 0; i < pos.length; i++) {
                    String[] numeros = pos_cadena[i].split(",");
                    pos[i][0] = Integer.parseInt(numeros[0]);
                    pos[i][1] = Integer.parseInt(numeros[1]);
                }
                linea = reader.readLine();
                double cost = Double.parseDouble(linea);
                teclados.put(clave, new SimpleEntry<>(new SimpleEntry<>(alf, pos), cost));

            }

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }




}
