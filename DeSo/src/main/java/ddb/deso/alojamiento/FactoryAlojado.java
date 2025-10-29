package ddb.deso.alojamiento;

public class FactoryAlojado {
    public static final int HUESPED=0;
    public static final int INVITADO=1;

    public static int[] tipo = {HUESPED,INVITADO};

    public static Alojado create (int tipo, Alojado inst_alojado){
        return switch (tipo) {
            case HUESPED -> new Huesped(inst_alojado.getDatos());
            case INVITADO -> new Invitado(inst_alojado.getDatos());
            default -> null;
        };
    }

}


