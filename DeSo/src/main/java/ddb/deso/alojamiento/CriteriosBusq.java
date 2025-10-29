package ddb.deso.alojamiento;
import ddb.deso.TipoDoc;

public class CriteriosBusq {
    private String apellido;
    private String nombre;
    private TipoDoc tipoDoc;
    private String nroDoc;

    public CriteriosBusq(String apellido, String nombre, TipoDoc tipoDoc, String num_documento) {
        if (no_es_vacio(nombre)){
            this.setNombre(nombre);
        }
        if (no_es_vacio(apellido)){
            this.setApellido(apellido);
        }
        if (no_es_vacio(tipoDoc.toString())){
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
