package ddb.deso.alojamiento;
import ddb.deso.TipoDoc;
/**
 * Clase que encapsula los **criterios de búsqueda** para identificar a una
 * persona alojada, incluyendo datos personales básicos.
 * <p>
 * Contiene campos para el apellido, nombre, tipo de documento y número de documento.
 * </p>
 *
 * @author gael
 */
public class CriteriosBusq {
    private String apellido;
    private String nombre;
    private TipoDoc tipoDoc;
    private String nroDoc;
    /**
     * Constructor que inicializa los criterios de búsqueda.
     * Los campos se inicializan solo si los valores de entrada no son nulos ni vacíos.
     *
     * @param apellido El apellido a establecer como criterio de búsqueda.
     * @param nombre El nombre a establecer como criterio de búsqueda.
     * @param tipoDoc El tipo de documento ({link ddb.deso.TipoDoc}) a establecer.
     * @param num_documento El número de documento a establecer.
     */
    public CriteriosBusq(String apellido, String nombre, TipoDoc tipoDoc, String num_documento) {
        if (no_es_vacio(nombre)){
            this.setNombre(nombre);
        }
        if (no_es_vacio(apellido)){
            this.setApellido(apellido);
        }
        if (tipoDoc != null && no_es_vacio(tipoDoc.toString())){
            this.setTipoDoc(tipoDoc);
        }
        if (no_es_vacio(num_documento)){
            this.setNroDoc(num_documento);
        }
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombres) {
        this.nombre = nombres;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }


}
