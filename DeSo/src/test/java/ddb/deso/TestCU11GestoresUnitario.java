package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.AlojadoNoEliminableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestCU11GestoresUnitario {

    @Mock
    private AlojadoDAO alojadoDAO;

    @InjectMocks
    GestorAlojamiento gestorAlojamiento;

    private Alojado alojadoEjemplo = null;

    private DatosCheckIn crearCheckIn() {
        return new DatosCheckIn(LocalDate.now());
    }

    private DatosCheckOut crearCheckOut() {
        return new DatosCheckOut(LocalDateTime.now());
    }

    @Test
    public void pasoAlojadoNulo(){
        AlojadoDTO entidadNula = null;
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadNula));
    }

    @Test
    public void pasoAlojadoSinNroDoc(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadSinDNI = new AlojadoDTO(alojadoEjemplo);
        entidadSinDNI.setNroDoc(null);
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadSinDNI));
    }

    @Test
    public void pasoAlojadoConNroDocVacio(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadSinDNI = new AlojadoDTO(alojadoEjemplo);
        entidadSinDNI.setNroDoc("");
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadSinDNI));
    }

    @Test public void pasoAlojadoSinTipoDoc(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadSinTipoDoc = new AlojadoDTO(alojadoEjemplo);
        entidadSinTipoDoc.setTipoDoc(null);
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadSinTipoDoc));
    }

    @Test public void DAO_NoEncuentraEntidad(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(null);
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void DAO_EncuentraEntidadConCheckIn(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().nuevoCheckIn(crearCheckIn());
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertThrows(AlojadoNoEliminableException.class, ()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void DAO_EncuentraEntidadConCheckOut(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().nuevoCheckOut(crearCheckOut());
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertThrows(AlojadoNoEliminableException.class, ()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void DAO_EncuentraEntidadConOtroNroDoc(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        entidadExistente.setNroDoc(alojadoEjemplo.getDatos().getNroDoc() + "0");
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void DAO_EncuentraEntidadConOtroTipoDoc(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        entidadExistente.setTipoDoc(TipoDoc
                .values()[
                (int) ((alojadoEjemplo.getDatos().getTipoDoc().ordinal()+1)%(Arrays.stream(TipoDoc.values()).count()))
                ]
        );
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertThrows(AlojadoInvalidoException.class, ()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }


    @Test public void CaminoFelizDAO_EncuentraEntidadSinEstadias(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().setCheckIns(null);
        alojadoEjemplo.getDatos().setCheckOuts(null);
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertDoesNotThrow(()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void CaminoFelizDAO_EncuentraEntidadConCheckInVacio(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().setCheckIns(List.of());
        alojadoEjemplo.getDatos().setCheckOuts(null);
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertDoesNotThrow(()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void CaminoFelizDAO_EncuentraEntidadConCheckOutVacio(){
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().setCheckIns(null);
        alojadoEjemplo.getDatos().setCheckOuts(List.of());
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        assertDoesNotThrow(()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }

    @Test public void CaminoFelizDAO_EncuentraEntidadConDatosNoModificados(){
        // Si el conserje modifica los datos del alojado (exceptuando NroDoc y TipoDoc)
        // Debe encontrar la entidad sin modificar y borrarla
        alojadoEjemplo = GeneradorDatosAleatorios.generarAlojadoAleatorio();
        alojadoEjemplo.getDatos().setCheckIns(null);
        alojadoEjemplo.getDatos().setCheckOuts(null);
        AlojadoDTO entidadExistente = new AlojadoDTO(alojadoEjemplo);
        when(alojadoDAO.buscarPorDNI(entidadExistente.getNroDoc(),entidadExistente.getTipoDoc()))
                .thenReturn(alojadoEjemplo);
        entidadExistente.setOcupacion("Otra ocupacion");
        entidadExistente.setNombre("Otro nombre");
        entidadExistente.setLocalidad("Otra localidad");
        assertDoesNotThrow(()->gestorAlojamiento.eliminarAlojado(entidadExistente));
    }


}
