package Presentacio;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import Domini.A_ControladorCapaDominio;
import Presentacio.P_ControladorCapaPresentacion;

public class VistaModificarTeclado extends JFrame {

    // Botones para todas las teclas del alfabeto
    private JButton[] teclas;
    // Botón para eliminar la seleccion
    private JButton botonBorrarSelec;
    // Botón para aplicar los cambios
    private JButton botonAplicaCanvi;
    // Botón para volver a la vista de atrás
    private JButton cancelarButton;

    // Label para mostrar el coste del teclado
    private JLabel label;

    // Panel para las teclas del teclado 
    private JPanel panelTeclas;
    // Panel del frame de la ventana
    private JPanel panel;

    // Contador para saber cuantas teclas ha pulsado
    private int p;
    // Primer indice del swap
    private int first;
    // Segundo indice del swap
    private int second;
    // Coste del teclado
    private double cost;
    // Tamaño del alfabeto
    private int size;
    // Plantilla para las posiciones del teclado
    private int[][] posiciones;
    // Distribucion del teclado
    private char[] teclado;
    // Nombre del teclado
    private String id_teclado;
    // Constraints para la ventana
    private GridBagConstraints gbc;
    // Guardaremos las teclas que clickamos del teclado para no repetir
    private Set<Integer> indicesClicados;
    // Instancia de la controladora de presentación
    P_ControladorCapaPresentacion ctrlPresent;

    // Constructora para la vista de modificar el teclado
    public VistaModificarTeclado(P_ControladorCapaPresentacion ctrlPresent) {
        this.ctrlPresent = ctrlPresent;
    }

    // Inicializamos la vista
    public void inicializar(int size, int[][] posiciones, char[] teclado, String id_teclado, double coste,
                            TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        this.size = size;
        this.posiciones = posiciones;
        this.teclado = teclado;
        this.id_teclado = id_teclado;

        // Configurar la ventana
        setTitle("EDICIÓN TECLADO");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        

        // Configuracion de constraints
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Inicializar variables
        p = 0;
        indicesClicados = new HashSet<>();
        DecimalFormat df = new DecimalFormat("#.##");


        // Crear el panel principal
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        // Configuración del botón para borrar las teclas seleccionadas
        botonBorrarSelec = new JButton("Esborra selecció");
        botonBorrarSelec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizamos variables
                p = 0;
                indicesClicados = new HashSet<>();
                
                // Volvemos a pintar de negro las teclas
                for (JButton boton : teclas) {
                    boton.setBackground(Color.BLACK);
                    boton.setForeground(Color.WHITE);
                }
            }
        });


        // Configuración del botón para aplicar cambios
        botonAplicaCanvi = new JButton("Aplica canvi!");
        botonAplicaCanvi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizamos variables
                first = second = -1;

                // Obtenemos los indices a cambiar para hacer el swap
                for (int i = 0; i < size; i++) {
                    // Nos quedamos con los indices de las teclas seleccionadas
                    if (teclas[i].getBackground().equals(Color.ORANGE)) {
                        if (first == -1) first = i;
                        else second = i;
                    }
                }
                // Por si click a "Aplica canvi!" antes de tiempo
                if (first == -1 || second == -1) { first = second = 0;}
                else {
                    // Indices correctos
                    if (first != -1 && second != -1) {
                        p = 0;
                        indicesClicados = new HashSet<>();
                        System.out.println("Swap realizado entre las posiciones " + first + " y " + second + " del teclado");

                        // Volvemos a pintar de negro las teclas
                        for (JButton boton : teclas) {
                            boton.setBackground(Color.BLACK);
                            boton.setForeground(Color.WHITE);
                        }
                    }
                }

                // Swap entre teclas del teclado 
                char aux = teclado[first];
                teclado[first] = teclado[second];
                teclado[second] = aux;

                // Guardamos el teclado en nuestra capa de persistencia
                cost = ctrlPresent.tecladoModificado(teclado, posiciones, id_teclado, frecuenciaLetras, indiceLetras, matrizFlujos);
                label.setText("Coste: " + df.format(cost));

                // Impresion de datos por terminal
                System.out.println("Teclado: " + Arrays.toString(teclado));
                System.out.println("Coste: " + df.format(cost) + "\n");

                // Actualizamos la ventana
                actualizarSwapTeclado();
            }
        });



        // Configuramos el botón de volver a la página anterior
        cancelarButton = new JButton("Volver");
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volvemos a la vista anterior, que es la VistaPrincipal
                ctrlPresent.inicializarVistaPrincipal(id_teclado, teclado, cost, posiciones, frecuenciaLetras, indiceLetras, matrizFlujos);
                dispose();
            }
        });



        // BOTON INVISIBLE PARA EL ESPACIADO
        JButton inv = new JButton();
        // Deshabilitamos el botón
        inv.setEnabled(false);
        // No pintamos sus bordes
        inv.setBorderPainted(false);


        // Crear panel para los botones
        JPanel panelBotones = new JPanel();
        // Añadir el botón "Esborra selecció" al panel de botones
        panelBotones.add(botonBorrarSelec);
        // Añadir el botón "Aplica canvi!" al panel de botones
        panelBotones.add(botonAplicaCanvi);
        // Añadir el botón "Cancelar" al panel de botones
        panelBotones.add(inv);
        // Añadir el botón de espaciado al panel de botones
        panelBotones.add(cancelarButton);
        // Añadimos el panel de botones al frame de la ventana
        panel.add(panelBotones);
        
        // Crear un JLabel con el coste del teclado
        label = new JLabel("Coste: " + df.format(coste));
        // Alinear el texto al centro del JLabel
        label.setHorizontalAlignment(JLabel.CENTER);
        // Agregar el JLabel a la ventana
        panel.add(label);

        // Crear panel para poner el teclado
        panelTeclas = new JPanel(new GridBagLayout());
        // Inicializar el array de botones
        teclas = new JButton[size];
        // Crear y agregar botones al JFrame
        for (int i = 0; i < size; i++) {
            // Asignamos una letra a cada botón
            teclas[i] = new JButton("" + (char)(teclado[i]));
            teclas[i].setBackground(Color.BLACK);  
            teclas[i].setForeground(Color.WHITE);  
            gbc.gridx = posiciones[i][0]; 
            gbc.gridy = posiciones[i][1]; 
            teclas[i].addActionListener(new BotonListener(i));
            panelTeclas.add(teclas[i], gbc);
        }
        // Añadimos el teclado con las teclas al panel
        panel.add(panelTeclas);

        // Agregamos el panel al frame
        add(panel);
        // Centramos la pantalla
        setLocationRelativeTo(null);
        // Hacemos visible la ventana
        setVisible(true);
    }


    // AUXILIAR FUNCTIONS

    // Volver a imprimir el teclado al swapear letras
    private void actualizarSwapTeclado() {
        // Eliminamos el teclado anterior
        panelTeclas.removeAll();
        // Crear y agregar botones al JFrame
        for (int i = 0; i < size; i++) {
            teclas[i] = new JButton("" + (char)(teclado[i])); // Asignar letra a cada botón --> ira el de distribución
            teclas[i].setBackground(Color.BLACK);  // Fondo negro
            teclas[i].setForeground(Color.WHITE);  // Texto blanco
            gbc.gridx = posiciones[i][0]; // Fila
            gbc.gridy = posiciones[i][1]; // Columna
            teclas[i].addActionListener(new BotonListener(i));
            panelTeclas.add(teclas[i], gbc);
        }
        // Añadimos el teclado nuevo con las teclas swapeadas
        panel.add(panelTeclas);
        // Actualizamos la pantalla por si han habido cambios
        revalidate();
        repaint();
    }

    // Función a implementar para cualquier botón
    private class BotonListener implements ActionListener {
        // Índice del botón dentro de su conjunto
        private int indice;
        // Obtenemos el índice en cuestión
        public BotonListener(int indice) {
            this.indice = indice;
        }
        // Controlamos los clicks de la ejecución
        @Override
        public void actionPerformed(ActionEvent e) {
            // El botón no se repite y no hay más de dos pulsados
            if (p < 2 && !indicesClicados.contains(indice)) {
                indicesClicados.add(indice);
                ++p;
                JButton botonClicado = (JButton) e.getSource();
                botonClicado.setBackground(Color.ORANGE);
            }
            else {
                // Error: se quiere swapear con la misma tecla y no se puede
                if (p < 2) JOptionPane.showMessageDialog(null, "Error! No se puede swapear con la misma tecla", "Missatge d'error", JOptionPane.ERROR_MESSAGE);
                // Error: se quiere swapear más de dos teclas y no se puede
                else JOptionPane.showMessageDialog(null, "Error! No se puede swapear entre más de dos teclas", "Missatge d'error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
