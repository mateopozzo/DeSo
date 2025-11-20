package ddb.deso.alojamiento;

public class DocDuplicadoException extends RuntimeException{
    private final Alojado alojado_existente;
    public DocDuplicadoException(Alojado alojado_existente) {
        super("El tipo y nro doc " + alojado_existente.getDatos().getDatos_personales().getTipoDoc() + " " + alojado_existente.getDatos().getDatos_personales().getNroDoc() + " ya existe");
        this.alojado_existente = alojado_existente;
    }

    public Alojado getAlojadoExistente() {
        return alojado_existente;
    }
}
