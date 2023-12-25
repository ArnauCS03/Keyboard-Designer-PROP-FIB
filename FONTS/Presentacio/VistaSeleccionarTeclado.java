package Presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;

// vista para escoger un teclado ya generado, pulsando sobre su nombre identificador, para abrirlo
public class VistaSeleccionarTeclado extends JFrame {

    private P_ControladorCapaPresentacion controladorPresentacion;
    private JPanel panelPrincipal;


    public VistaSeleccionarTeclado(P_ControladorCapaPresentacion controladorPresentacion) {
        this.controladorPresentacion = controladorPresentacion;
    }

    public void inicializar() {

        setTitle("Vista Seleccionar Teclado");
        setSize(600,400);
        setLocationRelativeTo(null);
        // si usuario cierra ventana se cierra toda la aplicacion
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelPrincipal = new JPanel(new FlowLayout());

        // Lista con los nombres de todos los teclados ya generados
        List<String> tecladoNombres = controladorPresentacion.getTecladoNombres();


        System.out.println("Hay " + tecladoNombres.size() + " teclados hechos");

        // caso que no haya ningun teclado generado, que salga para generar un teclado:
        if (tecladoNombres.isEmpty()) {
            JButton botonGenerarTeclado = new JButton("Generar Teclado");

            // Mostrar la vista SeleccionarTeclado
            setVisible(true);

            // mostrar ventanita error, de que no hay teclado generado
            JOptionPane.showMessageDialog(VistaSeleccionarTeclado.this, "No hay ningun teclado existente\nPulsa antes a Generar Teclado", "Error", JOptionPane.ERROR_MESSAGE);


            //botonGenerarTeclado.setBackground(Color.CYAN);
            botonGenerarTeclado.setFocusPainted(false);

            botonGenerarTeclado.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("boton Generar Teclado pulsado, abrir vista principal");

                    // ir a Vista Generar Teclado
                    controladorPresentacion.inicializarVistaGenerarTeclado();

                    setVisible(false);
                }
            });
            panelPrincipal.add(botonGenerarTeclado);

            add(panelPrincipal);
        }
        // Hay una lista con teclados generados, vamos a crear tantos botones como teclados, con su string identificador (su nombre)
        else {

            // crear tantos botones dinamicamente como teclados ya hechos
            for (String nombre : tecladoNombres) {

                JButton button = new JButton(nombre);

                button.setBackground(Color.CYAN);
                button.setFocusPainted(false);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Tratar pulsar el boton, abrir el teclado

                        System.out.println("boton " + nombre + " pulsado, abrir vista principal");

                        // ir a Vista Principal y mostrar el teclado seleccionado

                        char[] distr = controladorPresentacion.getDistribucionGuardada(nombre);
                        double coste = controladorPresentacion.getCostGuardado(nombre);
                        int [][] posiciones = controladorPresentacion.getOrdenPosGuardado(nombre);
                        TreeMap<Character, Integer> frecuenciaLetras = controladorPresentacion.getFrecuenciaLetras(nombre);
                        HashMap<Character, Integer> indiceLetras = controladorPresentacion.getIndiceLetras(nombre);
                        double[][] matrizFlujos = controladorPresentacion.getMatrizFlujos(nombre);

                        controladorPresentacion.inicializarVistaPrincipal(nombre, distr, coste, posiciones, frecuenciaLetras, indiceLetras, matrizFlujos);

                        dispose();
                    }
                });
                panelPrincipal.add(button);

                add(panelPrincipal);
            }
        }

        // Actualizar el frame para que muestre los botones
        revalidate();
        repaint();

        setVisible(true);
    }

}


