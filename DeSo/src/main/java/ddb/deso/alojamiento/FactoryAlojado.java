package ddb.deso.alojamiento;

public class FactoryAlojado {
    public static final int HUESPED=0;
    public static final int INVITADO=1;

    public static int[] tipo = {HUESPED,INVITADO};

    public static Alojado create (int tipo, DatosAlojado datos){
        return switch (tipo) {
            case HUESPED -> new Huesped(datos);
            case INVITADO -> new Invitado(datos);
            default -> null;
        };
    }

}


