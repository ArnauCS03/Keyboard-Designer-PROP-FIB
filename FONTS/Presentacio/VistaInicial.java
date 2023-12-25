package Presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Excepcions.*;

// Heredar the JFrame hace que no tengamos que crear el objeto JFrame "frame" y
// tener que poner frame.[funcion] todo el rato
public class VistaInicial extends JFrame {

    private JButton btnGenerarTeclado;
    private JButton btnCargarTeclado;
    private JButton btnEliminarTeclados;
    private JPanel panelPrincipal;
    private JPanel panelBotonEliminar;
    private P_ControladorCapaPresentacion controladorPresentacion;

    public VistaInicial(P_ControladorCapaPresentacion controladorPresentacion) {
        this.controladorPresentacion = controladorPresentacion;
    }


    public void inicializar() {
        // ponemos titulo a la vista, tamaño y posicion centrada
        setTitle("Vista Inicial");
        setSize(600,400);
        setLocationRelativeTo(null);

        listenerVentanaCerrar();

        // si usuario cierra ventana se cierra toda la aplicacion
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // creamos el panel principal del frame
        panelPrincipal = new JPanel(new FlowLayout());

        //panelBotonEliminar = new JPanel(new BorderLayout());

        // declaramos los dos botones que habran en la vista
        btnGenerarTeclado = new JButton("Generar Teclado");
        btnCargarTeclado = new JButton("Cargar Teclado");
        btnEliminarTeclados = new JButton("Eliminar Teclados");

        btnEliminarTeclados.setBackground(Color.RED);

        // Listeners para los dos botones
        listenerBtnGenerar();
        listenerBtnCargar();
        listenerBtnEliminarTeclados();

        // Agregamos los botones al panel
        panelPrincipal.add(btnGenerarTeclado);
        panelPrincipal.add(btnCargarTeclado);

        panelPrincipal.add(btnEliminarTeclados);

        // agregamos el panel al frame
        add(panelPrincipal);

        setVisible(true);
    }


    // Listener btn generar
    private void listenerBtnGenerar() {
        btnGenerarTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boton Generar teclado pulsado");
                // llamamos a la controladora de presentacion para que llame a esa vista
                controladorPresentacion.inicializarVistaGenerarTeclado();
                // Cerramos vista inicial
                dispose();
            }
        });
    }

    // Listener btn cargar
    private void listenerBtnCargar() {
        btnCargarTeclado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boton Cargar teclado pulsado");
                controladorPresentacion.inicializarVistaSeleccionarTeclado();
                // Cerramos vista inicial (hacemos no visible)
                dispose();
            }
        });
    }


    // Listener btn eliminar teclados
    private void listenerBtnEliminarTeclados() {
        btnEliminarTeclados.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Boton eliminar teclados");

                // confirmar eliminacion
                int respuesta = JOptionPane.showConfirmDialog(VistaInicial.this,
                        "¿Estas seguro que deseas eliminar todos los teclados?",
                        "Eliminacion teclados",
                        JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(VistaInicial.this, "Todos los teclados generados han sido eliminados", "Teclados Eliminados", JOptionPane.INFORMATION_MESSAGE);
                    controladorPresentacion.eliminarTodo();
                }
            }
        });
    }

    private void listenerVentanaCerrar() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Mostrar un mensaje al usuario
                int respuesta = JOptionPane.showConfirmDialog(VistaInicial.this,
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
}
