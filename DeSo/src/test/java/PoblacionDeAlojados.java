
import ddb.deso.*;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Clase para prueba de clase {@link Alojado} y clases descendientes
 * 
 * @author mat
 */
public class PoblacionDeAlojados {

    public PoblacionDeAlojados() {
    }

    public List<Huesped> crearNHuespedes(){

        List<Huesped> ret = new ArrayList<>();

        // --- Huesped 1 (Original) ---
        String calle1 = "Lopez y Planes";
        String depto1 = "Capital";
        String localidad1 = "Santa Fe";
        String provincia1="Santa Fe";
        String pais1 = "Argentina";
        String nro_calle1 = "1456";
        String piso1 = "";
        String nro_postal1 = "3000";
        String telefono1="4742205";
        String correo1="guillermoorellano@utn.frsf.edu.ar";
        String nombre1="Guillermo";
        String apellido1 = "Orellano";
        String nacionalidad1="argentino";
        String posicionIva1="excento";
        String ocupacion1="Docente";
        String nroDoc1="34784093";
        String CUIT1="20-34784093-2";
        TipoDoc tipoDoc1=TipoDoc.DNI;
        LocalDate fechaa1 = LocalDate.now() ;
        List<@NotNull Long> CICO1 = List.of(1L,3L,4L);

        DatosResidencia dr1=new DatosResidencia(calle1,depto1,localidad1,provincia1,pais1,nro_calle1,piso1,nro_postal1);
        DatosPersonales dp1=new DatosPersonales(nombre1,apellido1,nacionalidad1,posicionIva1,ocupacion1,nroDoc1,tipoDoc1,CUIT1,fechaa1);
        DatosContacto dc1=new DatosContacto(telefono1,correo1);
        DatosAlojado da1=new DatosAlojado(dc1,dr1,dp1);
        da1.setId_check_in(CICO1);
        da1.setId_check_out(CICO1);
        Huesped h1 = new Huesped(da1);
        ret.add(h1);


        // --- Huesped 2 ---
        String calle2 = "9 de Julio";
        String depto2 = "Rosario";
        String localidad2 = "Rosario";
        String provincia2="Santa Fe";
        String pais2 = "Argentina";
        String nro_calle2 = "3200";
        String piso2 = "3 B";
        String nro_postal2 = "2000";
        String telefono2="341590123";
        String correo2="anamaria.perez@email.com";
        String nombre2="Ana María";
        String apellido2 = "Pérez";
        String nacionalidad2="argentina";
        String posicionIva2="consumidor final";
        String ocupacion2="Contadora";
        String nroDoc2="25123456";
        String CUIT2="27-25123456-1";
        TipoDoc tipoDoc2=TipoDoc.DNI;
        LocalDate fechaa2 = LocalDate.now().minusYears(5);
        List<@NotNull Long> CICO2 = List.of(2L);

        DatosResidencia dr2=new DatosResidencia(calle2,depto2,localidad2,provincia2,pais2,nro_calle2,piso2,nro_postal2);
        DatosPersonales dp2=new DatosPersonales(nombre2,apellido2,nacionalidad2,posicionIva2,ocupacion2,nroDoc2,tipoDoc2,CUIT2,fechaa2);
        DatosContacto dc2=new DatosContacto(telefono2,correo2);
        DatosAlojado da2=new DatosAlojado(dc2,dr2,dp2);
        da2.setId_check_in(CICO2);
        da2.setId_check_out(CICO2);
        Huesped h2 = new Huesped(da2);
        ret.add(h2);


        // --- Huesped 3 ---
        String calle3 = "Calle Mayor";
        String depto3 = "";
        String localidad3 = "Madrid";
        String provincia3="Madrid";
        String pais3 = "España";
        String nro_calle3 = "50";
        String piso3 = "";
        String nro_postal3 = "28013";
        String telefono3="34915678";
        String correo3="juan.gomez@mail.es";
        String nombre3="Juan";
        String apellido3 = "Gómez";
        String nacionalidad3="español";
        String posicionIva3="responsable inscripto";
        String ocupacion3="Ingeniero";
        String nroDoc3="Y1234567A";
        String CUIT3="B81234567"; // Número de Identificación Fiscal (NIF) para España (simulado)
        TipoDoc tipoDoc3=TipoDoc.PASAPORTE;
        LocalDate fechaa3 = LocalDate.now().minusYears(10);
        List<@NotNull Long> CICO3 = List.of(5L, 6L);

        DatosResidencia dr3=new DatosResidencia(calle3,depto3,localidad3,provincia3,pais3,nro_calle3,piso3,nro_postal3);
        DatosPersonales dp3=new DatosPersonales(nombre3,apellido3,nacionalidad3,posicionIva3,ocupacion3,nroDoc3,tipoDoc3,CUIT3,fechaa3);
        DatosContacto dc3=new DatosContacto(telefono3,correo3);
        DatosAlojado da3=new DatosAlojado(dc3,dr3,dp3);
        da3.setId_check_in(CICO3);
        da3.setId_check_out(CICO3);
        Huesped h3 = new Huesped(da3);
        ret.add(h3);


        // --- Huesped 4 ---
        String calle4 = "Av. Santa Fe";
        String depto4 = "CABA";
        String localidad4 = "Buenos Aires";
        String provincia4="Buenos Aires";
        String pais4 = "Argentina";
        String nro_calle4 = "1900";
        String piso4 = "7 C";
        String nro_postal4 = "1425";
        String telefono4="1148765432";
        String correo4="laura.rodriguez@trabajo.org";
        String nombre4="Laura";
        String apellido4 = "Rodríguez";
        String nacionalidad4="argentina";
        String posicionIva4="monotributista";
        String ocupacion4="Diseñadora";
        String nroDoc4="30987654";
        String CUIT4="20-30987654-7";
        TipoDoc tipoDoc4=TipoDoc.DNI;
        LocalDate fechaa4 = LocalDate.now().minusYears(2);
        List<@NotNull Long> CICO4 = null;

        DatosResidencia dr4=new DatosResidencia(calle4,depto4,localidad4,provincia4,pais4,nro_calle4,piso4,nro_postal4);
        DatosPersonales dp4=new DatosPersonales(nombre4,apellido4,nacionalidad4,posicionIva4,ocupacion4,nroDoc4,tipoDoc4,CUIT4,fechaa4);
        DatosContacto dc4=new DatosContacto(telefono4,correo4);
        DatosAlojado da4=new DatosAlojado(dc4,dr4,dp4);
        da4.setId_check_in(CICO4);
        da4.setId_check_out(CICO4);
        Huesped h4 = new Huesped(da4);
        ret.add(h4);


        // --- Huesped 5 ---
        String calle5 = "Queen St";
        String depto5 = "";
        String localidad5 = "Toronto";
        String provincia5="Ontario";
        String pais5 = "Canadá";
        String nro_calle5 = "200";
        String piso5 = "";
        String nro_postal5 = "M5V 2Z3";
        String telefono5="1411234";
        String correo5="david.smith@travel.ca";
        String nombre5="David";
        String apellido5 = "Smith";
        String nacionalidad5="canadiense";
        String posicionIva5="excento";
        String ocupacion5="Inversor";
        String nroDoc5="AB123456";
        String CUIT5="123456789"; // Número de Identificación (simulado)
        TipoDoc tipoDoc5=TipoDoc.PASAPORTE;
        LocalDate fechaa5 = LocalDate.now().minusMonths(6);
        List<@NotNull Long> CICO5 = null;

        DatosResidencia dr5=new DatosResidencia(calle5,depto5,localidad5,provincia5,pais5,nro_calle5,piso5,nro_postal5);
        DatosPersonales dp5=new DatosPersonales(nombre5,apellido5,nacionalidad5,posicionIva5,ocupacion5,nroDoc5,tipoDoc5,CUIT5,fechaa5);
        DatosContacto dc5=new DatosContacto(telefono5,correo5);
        DatosAlojado da5=new DatosAlojado(dc5,dr5,dp5);
        da5.setId_check_in(CICO5);
        da5.setId_check_out(CICO5);
        Huesped h5 = new Huesped(da5);
        ret.add(h5);

        return ret;
    }

    public void guardarLista(List<? extends Alojado> l){
        AlojadoDAOJSON dao=new AlojadoDAOJSON();
        for(var a:l){
            AlojadoDTO dto=new AlojadoDTO(a);
            dao.crearAlojado(dto);
        }
        return;
    }



//    public Invitado crearUnInvitado() {
//
//        DatosResidencia dr = new DatosResidencia("yo", "soy", "un", "invitado!", "mefalta", "432", "5", "sopa");
//        DatosContacto dc = new DatosContacto(4742205, "polimorfismo@herencia.encap");
//        DatosPersonales dp = new DatosPersonales("asdf", "estado dfgh", "mundo", "no se", "badmington", "1234f.45", TipoDoc.PASAPORTE, "20-13857-12", new Date());
//        DatosAlojado da = new DatosAlojado(dc, dr, dp);
//        Invitado x = new Invitado();
//        x.setDatos(da);
//        return x;
//    }

//    public void guardarUnAlojado(){
//        Huesped alocado = crearUnHuesped();
//        AlojadoDTO dto = new AlojadoDTO(alocado);
//        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
//        guardar.crearAlojado(dto);
//        System.out.println("se deberia haber guardado un Huesped nuevo");
//    }
//    public void guardarAlojadoEInvitado(){
//        Huesped alocado = crearUnHuesped();
//        Invitado invitado = crearUnInvitado();
//        AlojadoDTO dto1 = new AlojadoDTO(alocado);
//        AlojadoDTO dto2 = new AlojadoDTO(invitado);
//        AlojadoDAOJSON guardar = new AlojadoDAOJSON();
//        guardar.crearAlojado(dto1);
//        guardar.crearAlojado(dto2);
//        System.out.println("Se guardaron con exito!");
//    }
//    public void borrarTodo(){
//        AlojadoDAOJSON dao = new AlojadoDAOJSON();
//        List<AlojadoDTO> x = dao.listarAlojados();
//        Iterator i = x.iterator();
//        while(i.hasNext()){
//            dao.eliminarAlojado((AlojadoDTO) i.next());
//        }
//        return;
//    }
}
