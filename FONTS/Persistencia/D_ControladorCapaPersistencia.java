package Persistencia;


import Domini.A_ControladorCapaDominio;
import java.util.List;


public class D_ControladorCapaPersistencia {

    // instancias de la clases que conecta
    private A_ControladorCapaDominio _ctrlCapaDomini;
    private D_GuardarTeclats memoriaTeclats;
    private D_GuardarTexts memoriaTexts;
    private D_GuardarLlistaParaulesFrequencies memoriaParaulesFreq;
    private D_GuardarAlfabets memoriaAlfabets;


    // CONSTRUCTORA

    // constructora privada, se accede a la instancia con getInstance()
    private D_ControladorCapaPersistencia() {
        this._ctrlCapaDomini = A_ControladorCapaDominio.getInstance();

        // las clases que se encargaran de guardar los datos en ficheros
        this.memoriaTeclats = D_GuardarTeclats.getInstancia();
        this.memoriaTexts = D_GuardarTexts.getInstancia();
        this.memoriaParaulesFreq = D_GuardarLlistaParaulesFrequencies.getInstancia();
        this.memoriaAlfabets = D_GuardarAlfabets.getInstancia();
    }

    public D_GuardarAlfabets getMemoriaAlfabets() {
        return memoriaAlfabets;
    }

    // metodo static que ayuda al getter de la instancia a obtener la unica instancia del controlador de capa dominio (avaluacion Lazy, hasta que no se llama no se crea la instancia)
    private static class SingletonHelper {
        private static final D_ControladorCapaPersistencia instance = new D_ControladorCapaPersistencia();
    }

    // Getter de la constructora del controlador, para que solo haya una instancia, aplicando el patron singleton.  Siempre que una clase quiera la instancia del controlador, tiene que llamar a este metodo.
    public static D_ControladorCapaPersistencia getInstance() {
        return D_ControladorCapaPersistencia.SingletonHelper.instance;
    }



    // GUARDAR datos del teclado generado y el input leido

    // usar la clase Teclats para guardar en un fichero el teclado ya hecho, con los simbolos desde el centro en orden (distribucion), su coste y las posiciones en orden de cada simbolo colocado (ordenPos)
    public void guardarTeclado(String nombre_teclado, char[] distribucion, double coste, int[][] ordenPos) {
        memoriaTeclats.guarda_Teclado(nombre_teclado,distribucion, ordenPos,coste );
    }

    // usar la clase Texts para guardar en un fichero el input del texto leido
    public void guardarTexto(String nombre_teclado,String texto) {
        memoriaTexts.guarda_Texto(nombre_teclado,texto);
    }

    // usar la clase ParaulesIFrequencies para guardar en un fichero el input de la lista de palabras con freq (juntado todo como string, mas facil para tratar y guardar)
    public void guardarPalabrasFreq(String nombre_teclado,String listaPalabras) {
        memoriaParaulesFreq.guarda_ListaPalabras(nombre_teclado,listaPalabras);
    }

    // usar la clase Alfabetos para guardar en un fichero el input del alfabeto leido
    public void guardarAlfabeto(String nombre_teclado, char[] alfabeto) {
        memoriaAlfabets.guarda_Alfabeto(nombre_teclado,alfabeto);
    }


    //GUARDAR datos a ficheros de texto

    public void guardadoFinal() {
        memoriaTeclats.guardarTecladoComoArchivo();
        memoriaAlfabets.guardarAlfabetoComoArchivo();
        memoriaTexts.guardarTextoComoArchivo();
        memoriaParaulesFreq.guardarListaPalabrasComoArchivo();
    }

    //CARGAR datos de ficheros de texto

    public void cargaInicial () {
        memoriaParaulesFreq.cargarListaPalabrasDesdeArchivo();
        memoriaTexts.cargarTextoDesdeArchivo();
        memoriaAlfabets.cargarAlfabetoDesdeArchivo();
        memoriaTeclats.cargarTecladoDesdeArchivo();
    }


    //ELIMINAR

    //eliminamos un teclado junto a cada uno de sus inputs si es que tiene
    public void eliminarTeclado (String nombre_teclado) {
        memoriaAlfabets.elimina_Alfabeto(nombre_teclado);
        memoriaTeclats.elimina_Teclado(nombre_teclado);
        memoriaTexts.elimina_Texto(nombre_teclado);
        memoriaParaulesFreq.elimina_ListaPalabras(nombre_teclado);
    }

    //eliminamos toda la base de datos
    public void eliminarTodo () {
        memoriaAlfabets.elimina_todo();
        memoriaParaulesFreq.elimina_todo();
        memoriaTexts.elimina_todo();
        memoriaTeclats.elimina_todo();
    }


    //GETTERS

    public List<String> consulta_lista_teclados() {
        return memoriaTeclats.listado_teclados();
    }

    public Integer consulta_tipo_input_teclado(String id) {
        if (memoriaAlfabets.existe_alfabeto(id)) {
            //es un alfabeto+lista_palabras
            if(memoriaParaulesFreq.existe_listaPalabras(id)) return 4;
            //es un alfabeto+texto
            else if(memoriaTexts.existe_texto(id)) return 3;
        }
        //es un texto
        else if (memoriaTexts.existe_texto(id)) return 1;
        //es una lista_palabras
        return 2;
    }

    public char[] getAlfabeto(String id) { return memoriaAlfabets.getAlfabeto(id); }

    public String getTexto(String id) { return memoriaTexts.getTexto(id); }

    public String getListaPalabras(String id) { return memoriaParaulesFreq.getListaPalabras(id); }


    // getters de los 3 parametros que forman parte del teclado generado, que se encuentran en D_GuardarTeclats
    public char[] getDistribucionGuardada(String tecladoId) {
        return memoriaTeclats.getDistribucionTeclado(tecladoId);    // todo getter distribucion en D_GuardarTeclats es correcto?
    }

    public double getCostGuardado(String tecladoId) {
        return memoriaTeclats.getCosteTeclado(tecladoId);
    }

    public int[][] getOrdenPosGuardado(String tecladoId) {
        return memoriaTeclats.getPosiconesTeclado(tecladoId);
    }

    public boolean existeTeclado(String nombre) {
        return memoriaTeclats.existe_teclado(nombre);
    }

}
