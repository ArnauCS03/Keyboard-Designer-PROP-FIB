package Domini;

import Persistencia.D_ControladorCapaPersistencia;
import Presentacio.P_ControladorCapaPresentacion;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;


public class A_ControladorCapaDominio {

    // instancias de las clases que conecta
    private P_ControladorCapaPresentacion _ctrlCapaPresentacion;
    private D_ControladorCapaPersistencia _ctrlCapaPersistencia;
    private ProcesarTexto pTexto;
    private A_AlgoritmeGeneral algGeneral;


    // CONSTRUCTORA

    // constructora privada, se accede a la instancia con getInstance()
    private A_ControladorCapaDominio() {
        this._ctrlCapaPresentacion = P_ControladorCapaPresentacion.getInstance();
        this._ctrlCapaPersistencia = D_ControladorCapaPersistencia.getInstance();
    }

    // metodo static que ayuda al getter de la instancia a obtener la unica instancia del controlador de capa dominio (avaluacion Lazy, hasta que no se llama no se crea la instancia)
    private static class SingletonHelper {
        private static final A_ControladorCapaDominio instance = new A_ControladorCapaDominio();
    }

    // Getter de la constructora del controlador, para que solo haya una instancia, aplicando el patron singleton.  Siempre que una clase quiera la instancia del controlador, tiene que llamar a este metodo.
    public static A_ControladorCapaDominio getInstance() {
        return SingletonHelper.instance;
    }



    // GENERAR teclado

    // Principal logica de la capa Dominio, primero procesar el input leido para sacar informacion como la matriz de flujos que necesita el algoritmo
    // se genera el teclado llamando al Algoritmo General que luego se decide si se genera con QAP o con Simulated Annealing
    // se guarda el teclado hecho y su input asociado a capa Persistencia
    // siempre que se genera un teclado, al final se abre la VistaPrincipal, para ver el resultado

    public void generarTeclado(String tecladoId, String textoGeneralInput, int idInput) {

        // procesar

        this.pTexto = new ProcesarTexto(textoGeneralInput);

        TreeMap<Character, Integer> frecuenciaLetras = pTexto.getFrecLetras();

        HashMap<Character, Integer> indiceLetras = pTexto.getIndiceLetras();

        double[][] matrizFlujos = pTexto.getMatrizFlujos();

        // generar

        this.algGeneral = new A_AlgoritmeGeneral(frecuenciaLetras, indiceLetras, matrizFlujos);
        algGeneral.generarTeclado();


        // Guardar

        char[] distr = algGeneral.getDistribution();
        double coste = algGeneral.getCost();
        int[][] posiciones = algGeneral.getPositions();


        if (idInput == 1) {
            guardarTecladoYTexto(tecladoId, distr, coste, posiciones, textoGeneralInput);
        }
        else if (idInput == 2) {
            guardarTecladoYPalabrasFreq(tecladoId, distr, coste, posiciones, textoGeneralInput);
        }

        // LLamar a Vista Principal
        this._ctrlCapaPresentacion = P_ControladorCapaPresentacion.getInstance();
        _ctrlCapaPresentacion.inicializarVistaPrincipal(tecladoId, distr, coste, posiciones, frecuenciaLetras, indiceLetras, matrizFlujos);
    }


    public void generarTeclado(String tecladoId, String alfabetoInput, String textoGeneralInput, int idInput) {
        // el alfabeto leido estaba como string, lo pasamos a array de caracteres, nos va mejor y es como se usa en el algoritmo
        char[] alfabeto = alfabetoInput.toCharArray();

        // procesar
        this.pTexto = new ProcesarTexto(alfabeto, textoGeneralInput);

        TreeMap<Character, Integer> frecuenciaLetras = pTexto.getFrecLetras();

        HashMap<Character, Integer> indiceLetras = pTexto.getIndiceLetras();

        double[][] matrizFlujos = pTexto.getMatrizFlujos();

        // generacion

        this.algGeneral = new A_AlgoritmeGeneral(frecuenciaLetras, indiceLetras, matrizFlujos);
        algGeneral.generarTeclado();

        // Guardar

        char[] distr = algGeneral.getDistribution();
        double coste = algGeneral.getCost();
        int[][] posiciones = algGeneral.getPositions();

        if (idInput == 3) {
            guardarTecladoYAlfabetoYTexto(tecladoId, distr, coste, posiciones, textoGeneralInput, alfabeto);
        }
        else if (idInput == 4) {
            guardarTecladoYAlfabetoYPalabrasFreq(tecladoId, distr, coste, posiciones, textoGeneralInput, alfabeto);
        }

        // LLamar a Vista Principal
        this._ctrlCapaPresentacion = P_ControladorCapaPresentacion.getInstance();
        _ctrlCapaPresentacion.inicializarVistaPrincipal(tecladoId, distr, coste, posiciones, frecuenciaLetras, indiceLetras, matrizFlujos);
    }


    // TECLADO MODIFICADO

    public double guardarTeclatModificat(char[] distribucion, int[][] ordenPos, String tecladoId,
                                         TreeMap<Character, Integer> frecuenciaLetras, HashMap<Character, Integer> indiceLetras, double[][] matrizFlujos) {
        // calcular coste del teclado modificado
        algGeneral = new A_AlgoritmeGeneral(frecuenciaLetras, indiceLetras, matrizFlujos);

        double costeDelModificado = algGeneral.getCostTeclado(distribucion, ordenPos, indiceLetras, matrizFlujos);

        // guardar el teclado modificado con el coste modificado
        guardarTecladoModificado(tecladoId, distribucion, costeDelModificado, ordenPos);

        return costeDelModificado;
    }





    // GUARDAR datos en capa Persistencia

    public void guardarTodoAFichero() {
        _ctrlCapaPersistencia.guardadoFinal();
    }
    public void cargarArchivos() {_ctrlCapaPersistencia.cargaInicial();}
    // la capa presentacion nos pasa lo que se quiere guardar, enviarlo a la capa Persistencia

    // pasar a la capa de Persistencia el teclado generado y el texto del input
    public void guardarTecladoYTexto(String tecladoId, char[] distribucion, double cost, int[][] ordenPos, String texto) {
        _ctrlCapaPersistencia.guardarTeclado(tecladoId, distribucion, cost, ordenPos);
        _ctrlCapaPersistencia.guardarTexto(tecladoId, texto);
    }
    // pasar a la capa de Persistencia el teclado generado y la lista de palabras con freq del input
    public void guardarTecladoYPalabrasFreq(String tecladoId, char[] distribucion, double cost, int[][] ordenPos, String textoFrecPalabras) {
        _ctrlCapaPersistencia.guardarTeclado(tecladoId, distribucion, cost, ordenPos);
        _ctrlCapaPersistencia.guardarPalabrasFreq(tecladoId, textoFrecPalabras);
    }
    // pasar a la capa de Persistencia el teclado generado, el alfabeto y el texto del input
    public void guardarTecladoYAlfabetoYTexto(String tecladoId, char[] distribucion, double cost, int[][] ordenPos, String texto, char[] alfabeto) {
        _ctrlCapaPersistencia.guardarTeclado(tecladoId, distribucion, cost, ordenPos);
        _ctrlCapaPersistencia.guardarAlfabeto(tecladoId, alfabeto);
        _ctrlCapaPersistencia.guardarTexto(tecladoId, texto);
    }
    // pasar a la capa de Persistencia el teclado generado, el alfabeto y la lista de palabras con freq del input
    public void guardarTecladoYAlfabetoYPalabrasFreq(String tecladoId, char[] distribucion, double cost, int[][] ordenPos, String textoFrecPalabras, char[] alfabeto) {
        _ctrlCapaPersistencia.guardarTeclado(tecladoId, distribucion, cost, ordenPos);
        _ctrlCapaPersistencia.guardarAlfabeto(tecladoId, alfabeto);
        _ctrlCapaPersistencia.guardarPalabrasFreq(tecladoId, textoFrecPalabras);
    }



    // ELIMINA  vaciar los datos guardados de la capa persistencia

    // elimina el teclado en concreto identificado con su nombre y tambien se eliminara el input que se ha usado para crearlo
    public void eliminarTeclado(String tecladoId) {
        _ctrlCapaPersistencia.eliminarTeclado(tecladoId);
    }

    // Indicar a capa Persistencia, que se desea eliminar todos los datos guardados, todos los teclados y sus inputs eliminados
    public void eliminarTodo() {
        _ctrlCapaPersistencia.eliminarTodo();
    }




    // GETTERS de los resultados del teclado generado acabado


    // getter de la distribucion hecha del teclado, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public char[] getDistribucion() { return algGeneral.getDistribution(); }

    // getter del coste del teclado, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public double getCost() { return algGeneral.getCost(); }

    // getter del orden de las posiciones, asi el controlador de la capa presentacion puede consultarlo una vez generado el teclado
    public int[][] getOrdenPos() { return algGeneral.getPositions(); }

    public TreeMap<Character, Integer> getFrecuenciaLetras(String nombre) {
        ProcesarTexto procesado = new ProcesarTexto();
        int input = _ctrlCapaPersistencia.consulta_tipo_input_teclado(nombre);
        if (input == 1) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 2) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 3) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador, contenido);
        }
        else if (input == 4) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador,contenido);
        }
        return procesado.getFrecLetras();

    }

    public HashMap<Character, Integer> getIndiceLetras(String nombre) {
        ProcesarTexto procesado = new ProcesarTexto();
        int input = _ctrlCapaPersistencia.consulta_tipo_input_teclado(nombre);
        if (input == 1) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 2) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 3) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador, contenido);
        }
        else if (input == 4) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador,contenido);
        }
        return procesado.getIndiceLetras();
    }

    public double[][] getMatrizFlujos(String nombre) {
        ProcesarTexto procesado = new ProcesarTexto();
        int input = _ctrlCapaPersistencia.consulta_tipo_input_teclado(nombre);
        if (input == 1) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 2) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            procesado = new ProcesarTexto(contenido);
        }
        else if (input == 3) {
            String contenido = _ctrlCapaPersistencia.getTexto(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador, contenido);
        }
        else if (input == 4) {
            String contenido = _ctrlCapaPersistencia.getListaPalabras(nombre);
            char[] alfabeto_generador = _ctrlCapaPersistencia.getAlfabeto(nombre);
            procesado = new ProcesarTexto(alfabeto_generador,contenido);
        }
        return procesado.getMatrizFlujos();
    }

    // GETTERS

    // pide a la capa persistencia, todos los nombres (identificadores) de los teclados creados.
    public List<String> getTecladoNombres() {
        return _ctrlCapaPersistencia.consulta_lista_teclados();
    }

    // getters de los inputs que estan guardados en capa Persistencia, que estan identificados con el nombre del teclado (id)
    public String getTexto(String id) { return _ctrlCapaPersistencia.getTexto(id); }

    public String getTextoFrecPalabras(String id) { return _ctrlCapaPersistencia.getListaPalabras(id); }

    public char[] getAlfabeto(String id) { return _ctrlCapaPersistencia.getAlfabeto(id); }


    // obtiene de la capa de Persistencia el identificador del input del teclado generado
    public Integer consulta_tipo_input_teclado(String id) { return _ctrlCapaPersistencia.consulta_tipo_input_teclado(id); }



    // getters de los 3 parametros que forman parte del teclado generado, que se encuentran en capa persistencia
    public char[] getDistribucionGuardada(String tecladoId) { return _ctrlCapaPersistencia.getDistribucionGuardada(tecladoId); }

    public double getCostGuardado(String tecladoId) {
        return _ctrlCapaPersistencia.getCostGuardado(tecladoId);
    }

    public int[][] getOrdenPosGuardado(String tecladoId) { return _ctrlCapaPersistencia.getOrdenPosGuardado(tecladoId); }

    // Existe teclado
    public boolean existeTeclado(String nombre) {
        return _ctrlCapaPersistencia.existeTeclado(nombre);
    }



    //SETERS

    // guardar a capa Persistencia el input concreto con su identificador de teclado asociado
    public void guardarAlfabeto(String id, char[] alf) { _ctrlCapaPersistencia.guardarAlfabeto(id, alf); }

    public void guardarTexto(String id, String texto) { _ctrlCapaPersistencia.guardarTexto(id, texto); }

    public void guardarListaPalabras(String id, String lista) { _ctrlCapaPersistencia.guardarPalabrasFreq(id, lista); }


    // Si el usuario modifica un teclado, hay que volverlo a guardar en capa Persistencia
    public void guardarTecladoModificado(String tecladoId, char[] distribucion, double coste, int[][] ordenPos) {
        _ctrlCapaPersistencia.guardarTeclado(tecladoId, distribucion, coste, ordenPos);
    }

}
