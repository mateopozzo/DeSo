package ddb.deso;

//import static ddb.deso.gestores.GestorAlojamiento.buscarAlojado;


import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.gestores.GestorAlojamiento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TestCU02 {
    @Autowired
    private GestorAlojamiento gestorAlojamiento;

    @Autowired
    public TestCU02(GestorAlojamiento gestorAlojamiento){
        this.gestorAlojamiento = gestorAlojamiento;
    }

    @Test
    public void busquedaRegular() {
        /**CON TILDE EN BDD, SIN TILDE EN BÚSQUEDA - Todos los criterios ------")*/
        // "BÚSQUEDA: Gómez, Juan LE 47183532 ----------------------------\n");
        CriteriosBusq crit2 = new CriteriosBusq("Gómez", "Juan", TipoDoc.LE, "47183532");
        gestorAlojamiento.buscarHuesped(crit2);
    }

    @Test
    public void busquedaConTildes  (){
        /** CON TILDE EN BUSQUEDA, SIN TILDE EN BDD - Todos los criterios ------") */
        System.out.println("BÚSQUEDA: Fernandez, Domingo DNI 87800466 ----------------------------\n");
        CriteriosBusq crit5 = new CriteriosBusq("Fernández", "Domingo", TipoDoc.DNI, "87800466");
        gestorAlojamiento.buscarHuesped(crit5);
    }

    @Test
    public void busquedaCriterioUnicoNombre() {
        /**SOLO NOMBRE - MÁS DE UNA COINCIDENCIA ------------------------------")*/
        //"BÚSQUEDA: Juan ----------------------------------------------\n")
        CriteriosBusq crit3 = new CriteriosBusq(null,"Juan", null, null);
        gestorAlojamiento.buscarHuesped(crit3);
    }

    @Test
    public void busquedaCriterioUnicoDocumento() {
        /**SOLO NUM DOC ------------------------------")*/
        //BÚSQUEDA: 45510538 --------------------------------------------------\n");
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "45510538");
        gestorAlojamiento.buscarHuesped(crit4);
    }

    @Test
    public void buscarSinExito(){
        /**SIN COINCIDENCIAS ---------------------------------------------------")*/
        //System.out.println("BÚSQUEDA: Perez Mateo OTRO 5940490 ---------\n");
        CriteriosBusq crit1 = new CriteriosBusq("Perez","Carlos", TipoDoc.LC, "5940490");
        gestorAlojamiento.buscarHuesped(crit1);
    }

    @Test
    public void buscarTodos() {
        /**"ENCUENTRA TODOS ---------------------------------------------------");*/
        //System.out.println("BÚSQUEDA: Todos los campos null ---------\n");
        CriteriosBusq crit7 = new CriteriosBusq(null,null, null, null);
        gestorAlojamiento.buscarHuesped(crit7);
    }

}
