/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;
import java.util.Scanner;
/**
 *
 * @author mat
 */
public class GestorAlojamiento {

public void darDeAltaHuesped(){

   Huesped h=new Huesped();
   Scanner entrada = new Scanner(System.in);

   System.out.println("Apellido:");String apellido = entrada.nextLine();
   
   System.out.println("Nombre:");String nombre = entrada.nextLine();

   System.out.println("Tipo de documento:");String tipo_documento = entrada.nextLine();

System.out.println("Numero de documento:");String num_documento = entrada.nextLine();

System.out.println("Cuit:");String cuit = entrada.nextLine(); //no obligatorio

System.out.println("Posicion frente al IVA:");String iva = entrada.nextLine();

System.out.println("Fecha de Nacimiento:");String fecha_nacimiento = entrada.nextLine();


System.out.println("Direccion:");
System.out.println("Calle:"); String calle = entrada.nextLine();

System.out.println("Numero:"); String numero = entrada.nextLine();

System.out.println("Departamento:"); String departamento = entrada.nextLine();

System.out.println("Piso:"); String piso = entrada.nextLine();

System.out.println("Codigo:"); String codigo = entrada.nextLine();

System.out.println("postal:"); String postal = entrada.nextLine();

System.out.println("Localidad:"); String localidad = entrada.nextLine();

System.out.println("Provincia:"); String provincia = entrada.nextLine();

System.out.println("Pais:"); String pais = entrada.nextLine();

DatosResidencia dr= new DatosResidencia(calle,departamento 
,localidad,provincia,pais,numero,piso,postal);


System.out.println("Telefono:"); String telefono = entrada.nextLine();

System.out.println("Email:"); String email = entrada.nextLine(); //no obligatorio

System.out.println("Ocupacion:"); String ocupacion = entrada.nextLine();

System.out.println("Nacionalidad"); String nacionalidad = entrada.nextLine();

/*System.out.println("SIGUIENTE O CANCELAR  "); String boton = entrada.nextLine();
if (siguiente){} */


}


}
