package Presentacio;


// ========================================================================================================================
// El origen de todo, desde aqui se llama al controlador de la capa presentacion que este inicializa todas las vistas
// y aparece la interfaz grafica hecha con swing, de nuestra aplicacion para generar teclados.
// ========================================================================================================================

public class Main {  // clase que se ejecuta para ver el funcionamiento de todo el proyecto

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        // Delegar todo el funcionamiento al controlador de la capa presentacion, este hace toda la logica
                        P_ControladorCapaPresentacion ctrlPresentacion = P_ControladorCapaPresentacion.getInstance();
                        ctrlPresentacion.cargarArchivos();
                        ctrlPresentacion.inicializarPresentacion();
                    }
                });
    }
}
