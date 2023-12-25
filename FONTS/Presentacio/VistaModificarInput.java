package Presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VistaModificarInput extends JFrame {

    private  P_ControladorCapaPresentacion controlador;
    private  String id_teclado;
    private JTextArea textoArea;
    private JTextField alfabetoField;

    public VistaModificarInput(P_ControladorCapaPresentacion controlador) {
        this.controlador = controlador;
    }
    public void inicializar(String tecladoId, char[] distribucion, double coste, int[][] ordenPos,
                                          TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {

        this.id_teclado = tecladoId;
        // Configurar la ventana
        setTitle("EDICIÓN INPUT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Crear el panel principal
        JPanel panel = new JPanel();
        //que clase de input se uso para generar el teclado en id_teclado
        int tipo_input = controlador.consulta_tipo_input_teclado(id_teclado);

        //texto
        if (tipo_input == 1 ) {
            String texto = controlador.getTexto(id_teclado);

            //vista caso 1
            textoArea = new JTextArea(texto);
            textoArea.setLineWrap(true);
            textoArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textoArea);

            textoArea.setColumns(40); // Ajusta el ancho del área de texto
            textoArea.setRows(20); // Ajusta la altura del área de texto
            panel.add(scrollPane);

            //boton de guardado

        }
        //lista_palabras
        else if (tipo_input == 2) {
            String cadena_palabras = controlador.getTextoFrecPalabras(id_teclado);
            List<Map.Entry<String, Integer>> lista_palabras = convierte_lista(cadena_palabras);


            //vista caso 2
            textoArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textoArea);

            for (Map.Entry<String, Integer> entry : lista_palabras) {
                textoArea.append(entry.getKey() + " " + entry.getValue() + "\n");
            }

            textoArea.setLineWrap(true);
            textoArea.setWrapStyleWord(true);
            textoArea.setColumns(40);
            textoArea.setRows(15);

            panel.add(scrollPane);


        }
        //texto + alfabeto
        else if(tipo_input == 3) {
            String texto = controlador.getTexto(id_teclado);
            char[] alfabeto = controlador.getAlfabeto(id_teclado);

            //vista caso 3

            textoArea = new JTextArea(texto);
            textoArea.setLineWrap(true);
            textoArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textoArea);

            textoArea.setColumns(40);
            textoArea.setRows(15); // Reducido para dejar espacio al nuevo campo de texto

            panel.add(scrollPane);

            // Nuevo campo para modificar el alfabeto
            JPanel alfabetoPanel = new JPanel();
            JLabel alfabetoLabel = new JLabel("Alfabeto:");
            alfabetoField = new JTextField(new String(alfabeto), 20); // Mostrar el alfabeto actual
            alfabetoPanel.add(alfabetoLabel);
            alfabetoPanel.add(alfabetoField);
            panel.add(alfabetoPanel);
            //boton de guardado

        }
        //lista_palabras + alfabeto
        else if(tipo_input == 4) {
            String cadena_palabras = controlador.getTextoFrecPalabras(id_teclado);
            List<Map.Entry<String, Integer>> lista_palabras = convierte_lista(cadena_palabras);
            char[] alfabeto = controlador.getAlfabeto(id_teclado);

            //vista caso 4
            textoArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textoArea);

            for (Map.Entry<String, Integer> entry : lista_palabras) {
                textoArea.append(entry.getKey() + " " + entry.getValue() + "\n");
            }

            textoArea.setLineWrap(true);
            textoArea.setWrapStyleWord(true);
            textoArea.setColumns(40);
            textoArea.setRows(15);

            panel.add(scrollPane);

            // Nuevo campo para modificar el alfabeto
            JPanel alfabetoPanel = new JPanel();
            JLabel alfabetoLabel = new JLabel("Alfabeto:");
            alfabetoField = new JTextField(new String(alfabeto), 20); // Mostrar el alfabeto actual
            alfabetoPanel.add(alfabetoLabel);
            alfabetoPanel.add(alfabetoField);
            panel.add(alfabetoPanel);
            //boton guardar

        }


        JButton guardarPalabrasAlfabetoButton = new JButton("Guardar");
        guardarPalabrasAlfabetoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tipo_input == 1) {
                        controlador.generarTeclado(tecladoId, textoArea.getText(), 1);
                    }
                    else if (tipo_input == 2) {
                        String palabras = palabras();
                        controlador.generarTeclado(tecladoId, palabras, 2);
                    }
                    else if (tipo_input == 3) {
                        controlador.generarTeclado(tecladoId, alfabetoField.getText(), textoArea.getText(), 3);
                    }
                    else if (tipo_input == 4) {
                        String palabras = palabras();
                        controlador.generarTeclado(tecladoId, alfabetoField.getText(), palabras, 4);
                    }
                    // Cerrar y llamar a la vista principal
                    dispose();
                }
            });
        panel.add(guardarPalabrasAlfabetoButton);




        // Hacer visible la ventana
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null); // Centrar en la pantalla
        setVisible(true);
    }
    //metodo que convierte las lista de palabras con frecuencias de un string a una lista de pairs
    private List<Map.Entry<String, Integer>> convierte_lista(String palabras_seguidas) {
        String[] palabras = palabras_seguidas.split("\\s+");

        Map<String, Integer> conteoPalabras = new HashMap<>();
        for (String palabra : palabras) {
            conteoPalabras.put(palabra, conteoPalabras.getOrDefault(palabra, 0) + 1);
        }
        return new ArrayList<>(conteoPalabras.entrySet());
    }

    //convierte de nuevo la lista de palabras con frecuencias a un string y lo guarda
    private String palabras() {
        // guardar las palabras modificadas
        String palabrasModificadas = textoArea.getText(); // Obtener el texto modificado del JTextArea
        String[] lineas = palabrasModificadas.split("\\n");
        String cadena_palabras = "";
        // Actualizar la lista de palabras con las modificaciones
        for (String linea : lineas) {
            String[] partes = linea.split(" ");

            if (partes.length == 2) {
                for (int i = 0; i < Integer.parseInt(partes[1]); i++) {
                    cadena_palabras = cadena_palabras + partes[0] + " ";
                }
            }
        }
        return cadena_palabras.trim();

        // Aquí debes agregar la lógica para guardar la lista de palabras modificada
        // Por ejemplo, llamar a un método en el controlador para realizar la operación de guardado.
    }


}
