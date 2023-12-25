package Presentacio;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import Excepcions.*;

public class VistaPrincipal extends JFrame {


    private JButton btnModifTeclado;
    private JButton btnModifInput;
    private JButton btnCargarTeclado;
    private JButton btnGenerarNuevo;
    private JButton btnGuardarTeclado;
    private JButton btnEliminarTeclado;
    private JButton btnSalir;
    private P_ControladorCapaPresentacion controladorPresentacion;

    private String tecladoId;
    private char[] distribucion;
    private double coste;
    private int[][] ordenPosiciones;
    private TreeMap<Character, Integer> frecuenciaLetras;
    private HashMap<Character, Integer> indiceLetras;
    private double[][] matrizFlujos;


    public VistaPrincipal(P_ControladorCapaPresentacion controladorPresentacion) {
        this.controladorPresentacion = controladorPresentacion;
    }

    public void inicializar(String tecladoId, char[] distribucion, double coste, int[][] ordenPos,
                            TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {

        this.tecladoId = tecladoId;
        this.distribucion = distribucion;
        this.coste = coste;
        this.ordenPosiciones = ordenPos;
        this.frecuenciaLetras = frecuenciaLetras;
        this.indiceLetras = indiceLetras;
        this.matrizFlujos = matrizFlujos;


        //
        setTitle("Vista Principal");
        setSize(600,400);
        setLocationRelativeTo(null);

        // Listener para cerrar ventana si desea guardar datos o no
        //// Agregar un listener de ventana para manejar el evento de cierre
        listenerVentanaCerrar();

        // crear panel principal
        JPanel panelPrincipal = new JPanel(new FlowLayout());
        // crear panel para botones
        Box panelBotones = Box.createVerticalBox();
        // Formato decimal
        DecimalFormat df = new DecimalFormat("#.##");

        // Creamos botones
        btnModifTeclado = new JButton("Modificar Teclado");
        btnModifInput = new JButton("Modificar Input");
        btnCargarTeclado = new JButton("Cargar Teclado");
        btnGenerarNuevo = new JButton("Generar Nuevo Teclado");
        btnGuardarTeclado = new JButton("Guardar Teclado");
        btnEliminarTeclado = new JButton("Eliminar Teclado");
        btnSalir = new JButton("Salir");

        // anyadimos botones a panel de botones
        panelBotones.add(btnModifTeclado);
        panelBotones.add(btnModifInput);
        panelBotones.add(btnCargarTeclado);
        panelBotones.add(btnGenerarNuevo);
        panelBotones.add(btnGuardarTeclado);
        // Creamos separacion entre los botones
        panelBotones.add(Box.createVerticalStrut(160));
        panelBotones.add(btnEliminarTeclado);
        // Creamos separacion entre los botones
        panelBotones.add(Box.createVerticalStrut(20));
        panelBotones.add(btnSalir);

        // Anyadimos a panel principal el panel de los botones
        panelPrincipal.add(panelBotones);

        // alineamos panel principal enganchado ala izquierda
        FlowLayout flowLayout = (FlowLayout) panelPrincipal.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);


        ////////////////////////////////
        // Listeners para los botones //
        ////////////////////////////////
        listenerModifTeclado();
        listenerModifInput();
        listenerCargarTeclado();
        listenerGenerarNuevo();
        listenerGuardarTeclado();
        listenerEliminarTeclado();
        listenerSalir();

        // Mostramos el teclado:
        System.out.println("Coste: " + df.format(coste) + "\n");

        // Panel para el teclado //
        // // // // // // // // //

        int size = distribucion.length;

        // Configuracion de constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // crear panel para mostrar teclado
        JPanel panelTeclas = new JPanel(new GridBagLayout());

        // Crear panel para poner el teclado
        panelTeclas = new JPanel(new GridBagLayout());
        // Inicializar el array de botones
        JButton[] teclas = new JButton[size];
        // Crear y agregar botones al JFrame
        for (int i = 0; i < size; i++) {
            teclas[i] = new JButton("" + (char)(distribucion[i])); // Asignar letra a cada botón --> ira el de distribución
            teclas[i].setBackground(Color.BLACK);  // Fondo negro
            teclas[i].setForeground(Color.WHITE);  // Texto blanco
            gbc.gridx = ordenPos[i][0]; // Fila
            gbc.gridy = ordenPos[i][1]; // Columna
            panelTeclas.add(teclas[i], gbc);
        }
        panelPrincipal.add(panelTeclas);

        // Agregamos el panel principal
        add(panelPrincipal);

        // Hacer visible la ventana
        setVisible(true);
    }


    private void listenerVentanaCerrar() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Mostrar un mensaje al usuario
                int respuesta = JOptionPane.showConfirmDialog(VistaPrincipal.this,
                        "¿Deseas guardar los cambios antes de cerrar?",
                        "Cierre de ventana",
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    // GUARDAR CAMBIOS !!!
                    // Si el usuario elige "Sí", cerrar la ventana habiendo guardado antes los cambios
                    System.out.println("Teclado Guardado");
                    controladorPresentacion.guardarTodoAFichero();
                    System.exit(0);
                } else {
                    // Si el usuario elige "No", no se guardaran los datos y se cierra
                    System.exit(0);
                }
            }
        });
    }

    ////////////////////////////////
    // COMIENZO DE LOS LISTENERS !!!
    ////////////////////////////////

    private void listenerModifTeclado() {
        btnModifTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                controladorPresentacion.inicializarVistaModificarTeclado(distribucion.length, ordenPosiciones,
                                                                         distribucion, tecladoId, coste, frecuenciaLetras, indiceLetras, matrizFlujos);
            } 

        });
    }

    private void listenerModifInput() {
        btnModifInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                controladorPresentacion.inicializarVistaModificarInput(tecladoId, distribucion, coste, ordenPosiciones,
                                                                       frecuenciaLetras, indiceLetras, matrizFlujos);
            }

        });
    }

    private void listenerCargarTeclado() {
        btnCargarTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                controladorPresentacion.inicializarVistaSeleccionarTeclado();
            }

        });
    }

    private void listenerGenerarNuevo() {
        btnGenerarNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                controladorPresentacion.inicializarVistaGenerarTeclado();
            }

        });
    }

    private void listenerGuardarTeclado() {
        btnGuardarTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // el teclado ya se guarda automaticamente cuando se modifica/crea...
                JOptionPane.showMessageDialog(VistaPrincipal.this, "El teclado ha sido guardado", "Teclado Guardado", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void listenerEliminarTeclado() {
        btnEliminarTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // mensage eliminar
                JOptionPane.showMessageDialog(VistaPrincipal.this, "El teclado se ha eliminado", "Teclado eliminado", JOptionPane.INFORMATION_MESSAGE);

                dispose();
                // Eliminamos el teclado
                controladorPresentacion.eliminarTeclado(tecladoId);

                // Volvemos a la vista inicial
                controladorPresentacion.inicializarPresentacion();
            }
        });
    }

    private void listenerSalir() {
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Mostrar un mensaje al usuario
                int respuesta = JOptionPane.showConfirmDialog(VistaPrincipal.this,
                        "¿Deseas guardar los cambios antes de cerrar?",
                        "Cierre de ventana",
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    // GUARDAR CAMBIOS !!!
                    // Si el usuario elige "Sí", cerrar la ventana habiendo guardado antes los cambios
                    controladorPresentacion.guardarTodoAFichero();
                    System.out.println("Teclado Guardado");
                    System.exit(0);
                } else {
                    // Si el usuario elige "No", no se guardaran los datos y se cierra
                    System.exit(0);
                }
            }

        });
    }



}
