package Presentacio;

import Domini.A_ControladorCapaDominio;
import Excepcions.ExcepFichero;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;


public class P_ControladorCapaPresentacion {

    // instancias de las clases que conecta
    private A_ControladorCapaDominio _ctrlCapaDomini;
    private VistaInicial vistaInicial;
    private VistaGenerarTeclado vistaGenerarTeclado;
    private VistaSeleccionarTeclado vistaSeleccionarTeclado;
    private VistaPrincipal vistaPrincipal;
    private VistaModificarTeclado vistaModificarTeclado;
    private VistaModificarInput vistaModificarInput;
    private P_LeerInput leerInp;



    // CONSTRUCTORA

    // constructora privada, se accede a la instancia con getInstance()
    private P_ControladorCapaPresentacion() {
        this.leerInp = new P_LeerInput();

        // this.vistaInicial = new VistaInicial(this);  // le pasa la instancia del controlador
        // this.vistaGenerarTeclado = new VistaGenerarTeclado(this);
        // this.vistaSeleccionarTeclado = new VistaSeleccionarTeclado(this);
        // this.vistaPrincipal = new VistaPrincipal(this);
        // this.vistaModificarTeclado = new VistaModificarTeclado(this);
        // this.vistaModificarInput = new VistaModificarInput(this);

        this._ctrlCapaDomini = A_ControladorCapaDominio.getInstance();
    }

    // metodo static que ayuda al getter de la instancia a obtener la unica instancia del controlador de capa dominio (avaluacion Lazy, hasta que no se llama no se crea la instancia)
    private static class SingletonHelper {
        private static final P_ControladorCapaPresentacion instance = new P_ControladorCapaPresentacion();
    }

    // Getter de la constructora del controlador, para que solo haya una instancia, aplicando el patron singleton.  Siempre que una clase quiera la instancia del controlador, tiene que llamar a este metodo.
    public static P_ControladorCapaPresentacion getInstance() {
        return P_ControladorCapaPresentacion.SingletonHelper.instance;
    }



    // INICIALIZAR VISTAS

    // Llama a la vista inicial, para que aparezca el primer frame, mostrar la interfaz grafica
    public void inicializarPresentacion() {
        this.vistaInicial = new VistaInicial(this);
        this.vistaInicial.inicializar();
    }

    public void inicializarVistaGenerarTeclado() {
        this.vistaGenerarTeclado = new VistaGenerarTeclado(this);
        this.vistaGenerarTeclado.inicializar();
    }

    public void inicializarVistaSeleccionarTeclado() {
        this.vistaSeleccionarTeclado = new VistaSeleccionarTeclado(this);
        this.vistaSeleccionarTeclado.inicializar();
    }

    public void inicializarVistaPrincipal(String tecladoId, char[] distribucion, double coste, int[][] ordenPos,
                                          TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        System.out.println("Estamos en ctrl Presentacion, vamos a inicializar VistaPrincipal");

        this.vistaPrincipal = new VistaPrincipal(this);
        this.vistaPrincipal.inicializar(tecladoId, distribucion, coste, ordenPos, frecuenciaLetras, indiceLetras, matrizFlujos);
    }

    public void inicializarVistaModificarTeclado(int size, int[][] posiciones, char[] teclado, String id_teclado, double coste,
                                                 TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        this.vistaModificarTeclado = new VistaModificarTeclado(this);
        this.vistaModificarTeclado.inicializar(size, posiciones, teclado, id_teclado, coste, frecuenciaLetras, indiceLetras, matrizFlujos);
    }

    public void inicializarVistaModificarInput(String tecladoId, char[] distribucion, double coste, int[][] ordenPos,
                                               TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        this.vistaModificarInput = new VistaModificarInput(this);
        this.vistaModificarInput.inicializar(tecladoId, distribucion, coste, ordenPos, frecuenciaLetras, indiceLetras, matrizFlujos);
    }




    // LECTURA DE DATOS que vienen de un fichero

    // se especifica que fichero que solo contiene un texto quiere leerse, se lee con la clase P_LeerInput
    public void leerDeFicheroTexto(File fichero) throws ExcepFichero {
        leerInp.leerTexto(fichero);
    }

    // se especifica que fichero que solo contiene lista de palabras con frecuencias quiere leerse, se lee con la clase P_LeerInput
    public void leerDeFicheroFrecPalabras(File fichero) throws ExcepFichero {
        leerInp.leerFrecPalabras(fichero);
    }
    // se especifica que fichero que solo contiene un alfabeto quiere leerse, se lee con la clase P_LeerInput, un vez leido ahora falta un texto o lista palabras extra
    public void leerDeFicheroAlfabeto(File fichero) throws ExcepFichero {
        leerInp.leerAlfabeto(fichero);
    }

    // leer de un fichero que contiene texto, pero se llama una vez se ha leido de un fichero de alfabeto anteriormente
    public void leerDeFicheroTextoDespuesAlfabeto(File fichero) throws ExcepFichero {
        leerInp.leerTexto(fichero);
    }

    // leer de un fichero que contiene lista de palabras con frecuencias, pero se llama una vez se ha leido de un fichero de alfabeto anteriormente
    public void leerDeFicheroPalabrasDespuesAlfabeto(File fichero) throws ExcepFichero {
        leerInp.leerFrecPalabras(fichero);
    }



    // GENERAR TECLADO

    // La logica la sigue haciendo la capa de dominio, enviar alli el identificador del teclado y el input leido y se genera el teclado y luego se puede hacer get de los 3 componentes que forman el teclado generado

    // genera a partir del input sea un texto o una lista palabras frecuencia juntado a texto, se especifica en el idInput
    public void generarTeclado(String tecladoId, String textoGeneralInput, int idInput) {
        _ctrlCapaDomini.generarTeclado(tecladoId, textoGeneralInput, idInput);
    }

    // genera a partir del input de un alfabeto mas un texto o una lista palabras frecuencia juntado a texto, se especifica en el idInput
    public void generarTeclado(String tecladoId, String alfabetoInput, String textoGeneralInput, int idInput) {
        _ctrlCapaDomini.generarTeclado(tecladoId, alfabetoInput, textoGeneralInput, idInput);
    }



    // TECLADO MODIFICADO

    // tenemos la nueva distribucion del teclado, cuando el usuario le ha hecho cambios (swap). Hay que guardar en persistencia el teclado modificado con su nuevo coste que se calcula en Dominio
    // el metodo devuelve el nuevo coste del teclado modificado, a la VistaModificarTeclado
    public double tecladoModificado(char[] distribucion, int[][] ordenPos, String tecladoId,
                                    TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        return _ctrlCapaDomini.guardarTeclatModificat(distribucion, ordenPos, tecladoId, frecuenciaLetras, indiceLetras, matrizFlujos);
    }




    // GUARDAR datos en capa Persistencia

    public void guardarTodoAFichero() { _ctrlCapaDomini.guardarTodoAFichero(); }

    public void cargarArchivos() {_ctrlCapaDomini.cargarArchivos();}



    // ELIMINA  vaciar los datos guardados de la capa persistencia

    // elimina el teclado en concreto identificado con su nombre y tambien se eliminara el input que se ha usado para crearlo
    public void eliminarTeclado(String tecladoId) {
        _ctrlCapaDomini.eliminarTeclado(tecladoId);
    }

    // Indicar a capa Persistencia, que se desea eliminar todos los datos guardados, todos los teclados y sus inputs eliminados
    public void eliminarTodo() {
        _ctrlCapaDomini.eliminarTodo();
    }





    // GETTERS

    // Getters de el input leido por la pantalla o de fichero, son sencillos sin parametros
    public String getTexto() { return leerInp.getTexto(); }

    public String getTextoFrecPalabras() { return leerInp.getTextoFrecPalabras(); }

    public char[] getAlfabeto() { return leerInp.getAlfabeto(); }


    // Getters del input que esta guardado en capa Persistencia, hay que pasarle el identificador del teclado con el que estan creados (cada teclado esta asociado a un input)
    public String getTexto(String id) { return _ctrlCapaDomini.getTexto(id); }

    public String getTextoFrecPalabras(String id) { return _ctrlCapaDomini.getTextoFrecPalabras(id);}

    public char[] getAlfabeto(String id) {return _ctrlCapaDomini.getAlfabeto(id); }

    // devuelve el int del tipo de input que se uso para generar un teclaco con nombre: id
    public Integer consulta_tipo_input_teclado(String id) { return _ctrlCapaDomini.consulta_tipo_input_teclado(id); }


    // pide a la capa persistencia, todos los nombres (identificadores) de los teclados creados.
    public List<String> getTecladoNombres() { return _ctrlCapaDomini.getTecladoNombres(); }


    // getters de los resultados del teclado generado acabado

    // getter de la distribucion hecha del teclado, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public char[] getDistribucion() { return _ctrlCapaDomini.getDistribucion(); }

    // getter del coste del teclado, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public double getCost() { return _ctrlCapaDomini.getCost(); }

    // getter del orden de las posiciones, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public int[][] getOrdenPos() { return _ctrlCapaDomini.getOrdenPos(); }


    // getters de los 3 parametros que forman parte del teclado generado, que se encuentran en capa persistencia
    public char[] getDistribucionGuardada(String tecladoId) { return _ctrlCapaDomini.getDistribucionGuardada(tecladoId); }

    public double getCostGuardado(String tecladoId) {
        return _ctrlCapaDomini.getCostGuardado(tecladoId);
    }

    public int[][] getOrdenPosGuardado(String tecladoId) {
        return _ctrlCapaDomini.getOrdenPosGuardado(tecladoId);
    }

    /////
    public TreeMap<Character, Integer> getFrecuenciaLetras(String nombre) {
        return _ctrlCapaDomini.getFrecuenciaLetras(nombre);
    }

    public HashMap<Character, Integer> getIndiceLetras(String nombre) {
        return _ctrlCapaDomini.getIndiceLetras(nombre);
    }

    public double[][] getMatrizFlujos(String nombre) {
        return _ctrlCapaDomini.getMatrizFlujos(nombre);
    }

    // Mirar si existe teclado
    public boolean existeTeclado(String nombre) {
        return _ctrlCapaDomini.existeTeclado(nombre);
    }


    // SETERS

    // guardar a la capa de Persistencia el input particular leido, y el identificador el teclado que se queria generar
    public void guardarAlfabeto(String id, char[] alf) { _ctrlCapaDomini.guardarAlfabeto(id, alf); }

    public void guardarTexto(String id, String texto) { _ctrlCapaDomini.guardarTexto(id, texto); }

    public void guardarListaPalabras(String id, String lista) { _ctrlCapaDomini.guardarListaPalabras(id, lista); }

}
