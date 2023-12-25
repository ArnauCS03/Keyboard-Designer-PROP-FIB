package Presentacio;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import Excepcions.*;

// Heredar the JFrame hace que no tengamos que crear el objeto JFrame "frame" y
// tener que poner frame.[funcion] todo el rato
public class VistaGenerarTeclado extends JFrame {

    private JTextArea textArea;
    private JButton btnLeerPantalla;
    private JButton btnLeerFichero;
    private JButton btnGenerar;
    private JButton confirmarNombre;
    private JTextField textFieldNombre;
    private String nombreTeclado;
    private boolean alfabetoMasTexto;
    private boolean inputCorrecto;
    private boolean nombreConfirmado;

    private P_ControladorCapaPresentacion controladorPresentacion;
    private String tecladoId;
    private int idInput;  // diferenciar el input que ha llegado, 1 texto  2 lista palabras freq  3 alfabeto+texto  4 alfabeto+lista palabras freq
    private String textoInput;
    private String listaPalabFreqInput;
    private String alfabetoInput;


    public VistaGenerarTeclado(P_ControladorCapaPresentacion controladorPresentacion) {
        this.alfabetoMasTexto = false;
        this.inputCorrecto = false;
        this.nombreConfirmado = false;
        this.controladorPresentacion = controladorPresentacion;
    }

    public void inicializar() {
        // Configurar la ventana
        setTitle("Vista Generar Teclado");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // si usuario cierra ventana se cierra toda la aplicacion
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Crear un panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        // Declarar botones
        btnLeerPantalla = new JButton("LeerPantalla");
        btnLeerFichero = new JButton("LeerFichero");
        btnGenerar = new JButton("Generar");

        // Listeners para los botones leerpantalla leerfichero
        listenerBtnPantalla();
        listenerBtnFichero();

        // Listener para boton generar
        listenerBtnGenerar();

        // Agregar botones al panel de botones
        panelBotones.add(btnLeerPantalla);
        panelBotones.add(btnLeerFichero);
        panelBotones.add(btnGenerar);

        // Panel para el nombre y su conjunto
        JPanel panelNombre = new JPanel(new FlowLayout());
        // Crear etiqueta y recuadro de texto para introducir nombre
        JLabel labelNombre = new JLabel("Introducir Nombre Teclado:");
        textFieldNombre = new JTextField();
        // Damos tamanyo que queramos
        textFieldNombre.setPreferredSize(new Dimension(150,20));
        // Boton para confimar nombre (panelNombre)
        confirmarNombre = new JButton("Confirmar");

        // Listener para el textField si le da Enter
        listenerTextField();
        // Listener para boton confirmar
        listenerBtnConfirmarNombre();

        // Agregar la etiqueta y el campo de texto al panel de nombre (Arriba de la pantalla)
        panelNombre.add(labelNombre, BorderLayout.NORTH);
        panelNombre.add(textFieldNombre);
        panelNombre.add(confirmarNombre);

        // Agregar el panel de nombre y el panel de botones al panel principal (abajo de la pantalla)
        // panel nombre arriba y panel botones abajo
        panelPrincipal.add(panelNombre, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // botones de leer y generar enabled(false)
        btnLeerFichero.setEnabled(false);
        btnLeerPantalla.setEnabled(false);
        btnGenerar.setEnabled(false);

        // Agregar el panel principal
        add(panelPrincipal);

        // Hacer visible la ventana
        setVisible(true);
    }


    // Listener para boton pantalla
    private void listenerBtnPantalla() {
        btnLeerPantalla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // botón LeerPantalla
                try {
                    // pasamos false para que sepa que no es de fichero lo que leeremos
                    mostrarOpciones(false);
                } catch (ExcepFichero ex) {
                    ex.printStackTrace();
                    // Si hay error mensaje de error
                    JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Error al leer el archivo\nMás info en tu terminal", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Listener para boton fichero
    private void listenerBtnFichero() {
        btnLeerFichero.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // botón LeerFichero
                try {
                    // true ya que leeremos d fichero
                    mostrarOpciones(true);
                    // volvermos a activar botones
                    btnLeerFichero.setEnabled(true);
                    btnLeerPantalla.setEnabled(true);
                } catch (ExcepFichero ex) {
                    ex.printStackTrace();
                    // si hay error mensaje de error
                    JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Error al leer el archivo\nMás info en tu terminal", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Listener para boton generar
    private void listenerBtnGenerar() {
        btnGenerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Generar pulsado");

                // Generar teclado,  quatro casos con los diferentes inputs
                if (idInput == 1) {
                    controladorPresentacion.generarTeclado(tecladoId, textoInput, idInput);
                }
                else if (idInput == 2) {
                    controladorPresentacion.generarTeclado(tecladoId, listaPalabFreqInput, idInput);
                }
                else if (idInput == 3) {
                    controladorPresentacion.generarTeclado(tecladoId, alfabetoInput, textoInput, idInput);
                }
                else if (idInput == 4) {
                    controladorPresentacion.generarTeclado(tecladoId, alfabetoInput, listaPalabFreqInput, idInput);
                }

                // informar que se guarda el teclado
                // el teclado ya se guarda automaticamente cuando se modifica/crea...
                JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "El teclado ha sido guardado automaticamente", "Teclado Guardado", JOptionPane.INFORMATION_MESSAGE);

                // Cerramos vista
                dispose();
            }
        });
    }

    // Verificar nombre correcto
    private boolean nombreOk(String nombre) {
        if (controladorPresentacion.existeTeclado(nombre)) return false;
        if (nombre.equals("")) return false;
        else if (nombre.contains(" ")) return false;
        else return true;
    }

    // Listener para textField si pulsa enter
    private void listenerTextField() {
        textFieldNombre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // primer if para verificar que el nombre ya ha sido introducido
                if (!nombreConfirmado) {
                    nombreTeclado = textFieldNombre.getText();
                    // verificamos nombre formato ok
                    if (!nombreOk(nombreTeclado)) {
                        // mensaje error si no esta ok
                        JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Nombre NO aceptado, sin espacios, no vacio ni repetido", "Nombre NO aceptado", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        System.out.println("Nombre guardado: " + nombreTeclado + ".");
                        nombreConfirmado = true;
                        // Asignamos el nombre al ID
                        tecladoId = nombreTeclado;
                        // btnleer los 2 enabled: true (activamos los botones para leer)
                        btnLeerPantalla.setEnabled(true);
                        btnLeerFichero.setEnabled(true);
                        // mensaje de nombre aceptado
                        JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Nombre aceptado", "Nombre aceptado", JOptionPane.INFORMATION_MESSAGE);
                        // Bloqueamos que no se pueda volver a poner nombre
                        confirmarNombre.setEnabled(false);
                        textFieldNombre.setEditable(false);
                }
                }
            }
        });
    }

    // Listener para boton confirmar nombre (misma funcionalidad que la funcion listener text field)
    private void listenerBtnConfirmarNombre() {
        confirmarNombre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nombreTeclado = textFieldNombre.getText();
                // verificamos nombre formato ok
                System.out.println(nombreTeclado);
                if (!nombreOk(nombreTeclado)) {
                    JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Nombre NO aceptado", "Nombre NO aceptado", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    System.out.println("Nombre guardado: " + nombreTeclado + ".");
                    nombreConfirmado = true;
                    // Asignamos el nombre al ID
                    tecladoId = nombreTeclado;
                    // btnleer los 2 enabled: true
                    btnLeerPantalla.setEnabled(true);
                    btnLeerFichero.setEnabled(true);
                    JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Nombre aceptado", "Nombre aceptado", JOptionPane.INFORMATION_MESSAGE);
                    // Bloquemos que no se pueda volver a poner nombre
                    confirmarNombre.setEnabled(false);
                    textFieldNombre.setEditable(false);
                }
            }
        });
    }


    // Mostrar opciones
    private void mostrarOpciones(boolean esDeFichero) throws ExcepFichero {
        // Deshabilitar los botones (para que no se pueda volver a pulsar hasta que acabe funcion)
        btnLeerPantalla.setEnabled(false);
        btnLeerFichero.setEnabled(false);

        // Crear un array con las opciones
        String[] opciones = {"Texto", "Frecuencia de Palabras", "Alfabeto + Texto", "Alfabeto + FrecPalabras"};

        // Mostrar un cuadro de diálogo de opciones
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Que input desea introducir?",
                "Opciones",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        // Si el usuario selecciona una opción, abrir la caja de texto correspondiente
        if (seleccion != null) {
            if (seleccion.equals("Texto")) {
                idInput = 1;
                // Si leemos de fichero llamamos a seleccionarArchivo sino mostrarTextArea
                if (esDeFichero) seleccionarArchivo("texto");
                else mostrarTextArea("Inserta texto:");
            }
            else if (seleccion.equals("Frecuencia de Palabras")) {
                idInput = 2;
                // Si leemos de fichero llamamos a seleccionarArchivo sino mostrarTextArea
                if (esDeFichero) seleccionarArchivo("frecPalab");
                else mostrarTextArea("Inserta frecuencia de palabras:");
            }
            else if (seleccion.equals("Alfabeto + Texto")) {
                idInput = 3;
                // Asignamos true para que en mostrar text area sepa que hacer
                alfabetoMasTexto = true;
                // Si leemos de fichero llamamos a seleccionarArchivo sino mostrarTextArea
                if (esDeFichero) seleccionarArchivo("alfab+text");
                else mostrarTextArea("Inserta alfabeto:");
            }
            else if (seleccion.equals("Alfabeto + FrecPalabras")) {
                idInput = 4;
                // Si leemos de fichero llamamos a seleccionarArchivo sino mostrarTextArea
                if (esDeFichero) seleccionarArchivo("alfab+frec");
                else mostrarTextArea("Inserta alfabeto:");
            }
        }
    }


    private void verificarYtratarInput(String opcion) {
        // Obtener el texto
        String texto = textArea.getText();

        // Tratamos el texto
        P_VerificarFormatoPantalla v = new P_VerificarFormatoPantalla();
        if (opcion.equals("Inserta texto:")) {
            // Si formato no esta correcto mensajito en cajita
            if (!v.formatoOkTexto(texto)) {
                JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Ej Formato correcto (SIN NUMEROS):\n-> hola que tal...", "ERROR FORMATO INCORRECTO", JOptionPane.ERROR_MESSAGE);
                btnLeerPantalla.setEnabled(true);
                btnLeerFichero.setEnabled(true);
            }
            else {
                System.out.println("Input: "+texto);

                // asignamos a nuestro atributo la info
                this.textoInput = texto;

                // input correcto para btn generar
                inputCorrecto = true;
                btnGenerar.setEnabled(true);
            }
        }
        else if (opcion.equals("Inserta alfabeto:")) {
            // Si formato no esta correcto mensajito en cajita
            if (!v.formatoOkAlfabeto(texto)) {
                JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Ej Formato correcto (NO MAYUS, NO REPETIDOS):\n-> abcd... \n(En una sola linea)", "ERROR FORMATO INCORRECTO", JOptionPane.ERROR_MESSAGE);
                btnLeerPantalla.setEnabled(true);
                btnLeerFichero.setEnabled(true);
            }
            else {
                System.out.println("Input: "+texto);

                // asignamos a nuestro atributo la info
                this.alfabetoInput = texto;

                // cuandoa se haya leído por primera vez el alfabeto llamamos a la que corresponda otra vez
                if (alfabetoMasTexto) {
                    mostrarTextArea("Inserta texto:");
                    alfabetoMasTexto = false;
                }
                else {
                    mostrarTextArea("Inserta frecuencia de palabras:");
                }
            }
        }
        else if (opcion.equals("Inserta frecuencia de palabras:")) {
            // Si formato no esta correcto mensajito en cajita
            if (!v.formatoOkFrecPalabras(texto)) {
                JOptionPane.showMessageDialog(VistaGenerarTeclado.this, "Ej Formato correcto (PALABRA NUM PALABRA NUM...):\n-> hola 2 que 1 tal 3...\n(En una sola linea, SOLO LETRAS)", "ERROR FORMATO INCORRECTO", JOptionPane.ERROR_MESSAGE);
                btnLeerPantalla.setEnabled(true);
                btnLeerFichero.setEnabled(true);
            }
            else {
                System.out.println("Input: "+texto);
                // Asignamos el string resultante: "hola 2" --> "hola hola"
                texto = v.transfFrecPalabras(texto);

                // asignamos a nuestro atributo la info
                this.listaPalabFreqInput = texto;

                // input correcto para btn generar
                inputCorrecto = true;
                btnGenerar.setEnabled(true);
            }
        }
    }


    // Mostrar cuadro texto
    private void mostrarTextArea(String opcion) {
        // Configuramos el frame como queramos
        JFrame frame = new JFrame(opcion);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear JTextArea y JScrollPane (area de texto y el scroll si fuera necesario)
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Crear botón para confirmar la entrada
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llama a la funcion para verificar el formato del texto y guardarlo
                verificarYtratarInput(opcion);
                // Cerrar la ventana
                frame.dispose();
            }
        });

        // Agregamos que escuche la tecla ENTER para que sea igual que el boton de confirmar
        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Llama a la funcion para verificar el formato del texto y pasarlo a ctrlPresentacion
                    verificarYtratarInput(opcion);
                    // Cerrar la ventana
                    frame.dispose();
                }
            }
        });

        // Crear panel para contener el JTextArea y el botón
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnConfirmar, BorderLayout.SOUTH);
        // Agregar panel al JFrame
        frame.add(panel);
        // Hacer visible el JFrame
        frame.setVisible(true);
    }


    // Mostrar file chooser
    private void seleccionarArchivo(String tipo) throws ExcepFichero {
        // Crear un JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Mostrar el cuadro de diálogo para seleccionar archivos
        int result = fileChooser.showOpenDialog(this);

        // Volver a activar botones
        btnLeerPantalla.setEnabled(true);
        btnLeerFichero.setEnabled(true);

        // Verificar si se ha seleccionado un archivo
        if (result == JFileChooser.APPROVE_OPTION) {
            // Obtener el archivo seleccionado
            File selectedFile = fileChooser.getSelectedFile();

            // si es texto llamaremos funcion de leer texto
            if (tipo.equals("texto")) {
                // input sera tipo 1 y llamamos a la controladora para leer
                if (idInput == 1) {
                    controladorPresentacion.leerDeFicheroTexto(selectedFile);
                    String t = controladorPresentacion.getTexto();

                    this.textoInput = t;

                    System.out.println("Input: "+t);
                }
                else if (idInput == 3) {
                    // input sera tipo 3 y llamamos a la controladora para leer
                    controladorPresentacion.leerDeFicheroTextoDespuesAlfabeto(selectedFile);
                    String t = controladorPresentacion.getTexto();

                    this.textoInput = t;

                    System.out.println("Input: "+t);
                }

                // input ok (btn generar)
                inputCorrecto = true;
                btnGenerar.setEnabled(true);
            }
            // si es frecPalab llamaremos funcion de leer frecPalabras
            else if (tipo.equals("frecPalab")) {
                // input sera tipo 2 y llamamos a la controladora para leer
                if (idInput == 2) {
                    controladorPresentacion.leerDeFicheroFrecPalabras(selectedFile);
                    String t = controladorPresentacion.getTextoFrecPalabras();

                    this.listaPalabFreqInput = t;

                    System.out.println("Input: "+t);
                }
                else if (idInput == 4) {
                    // input sera tipo 4 y llamamos a la controladora para leer
                    controladorPresentacion.leerDeFicheroPalabrasDespuesAlfabeto(selectedFile);
                    String t = controladorPresentacion.getTextoFrecPalabras();

                    this.listaPalabFreqInput = t;

                    System.out.println("Input: "+t);
                }

                // input ok (btn generar)
                inputCorrecto = true;
                btnGenerar.setEnabled(true);
            }
            // si es alfab + text :
            else if (tipo.equals("alfab+text")) {

                // primero lee alfabeto y trata
                controladorPresentacion.leerDeFicheroAlfabeto(selectedFile);
                char[] t = controladorPresentacion.getAlfabeto();

                this.alfabetoInput = new String(t);

                System.out.println("Input: "+t);
                // ahora lee texto volvemos llamar a funcion
                seleccionarArchivo("texto");

            }
            // si es alfab + frec :
            else if (tipo.equals("alfab+frec")) {

                // primero lee alfabeto y trata
                controladorPresentacion.leerDeFicheroAlfabeto(selectedFile);
                char[] t = controladorPresentacion.getAlfabeto();

                this.alfabetoInput = new String(t);

                System.out.println("Input: "+t);
                // ahora lee frec palabras volvemos llamar a funcion
                seleccionarArchivo("frecPalab");

            }

        }
    }


}
