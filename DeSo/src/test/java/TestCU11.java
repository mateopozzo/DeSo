import java.util.List;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.GestorAlojamiento;

public class TestCU11 {


    private AlojadoDAOJSON alojadoDAO;
    private GestorAlojamiento ge;
    private List<? extends Alojado> listaAlojados;

    public TestCU11() {
        alojadoDAO = new AlojadoDAOJSON();
        ge = new GestorAlojamiento(alojadoDAO);
    }

    private void poblarBase(){
        /*
        * Metodo interno para poblar la base
        * */
        PoblacionDeAlojados poblacion = new PoblacionDeAlojados();
        listaAlojados = poblacion.crearNHuespedes();
        for(var a:listaAlojados){
            AlojadoDTO dto=new AlojadoDTO(a);
            alojadoDAO.crearAlojado(dto);
        }
    }

    public void pruebaComun() {
        /*
        * Prueba crea 5 huespedes y ejecuta el CU11 para cada huesped
        * Workflow
        * 1-poblar alojados
        * 2-darles algunos checkin
        * 3-eliminarlos
        * */
        poblarBase();
        for(var x:listaAlojados){
            ge.darDeBajaHuesped(x);
        }
    }

    public void eliminarHuespedInexistente(){
        /*
        * Prueba de eliminacion de huesped no persistido
        * Workflow
        * 1-crear huesped aleatorio
        * 2-pasar como parametro de CU11
        * */
        poblarBase();
        Alojado aleatorio=GeneradorDatosAleatorios.generarHuespedAleatorio();

        System.out.println("PRUEBA: Huesped a eliminar " +
                aleatorio.getDatos().getDatos_personales().getNombre() + " " +
                aleatorio.getDatos().getDatos_personales().getApellido()
        );
        ge.darDeBajaHuesped(aleatorio);
    }


}
