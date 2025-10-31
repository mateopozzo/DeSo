# Desarrollo de Software: Diseño de gestión hotelera para Hotel Premier con Java
## Autores 
- ANTUÑA, Felipe

    feliantuna@gmail.com

    [*@felipeantuna*](https://github.com/felipeantuna)


- BECKMANN, Francisco

  franbeckmann@gmail.com

  [*@franbeckmann*](https://github.com/franbeckmann)


- GRASSI, Julieta

  jjulietagrassi@gmail.com

  [*@jjuligrassi*](https://github.com/jjuligrassi)


- POZZO GALDÓN, Mateo

  mateopozzogaldon@gmail.com

  [*@mateopozzo*](https://github.com/mateopozzo)


- SUÁREZ, Gael

  gaelbelletti@gmail.com

  [*@gbelletti*](https://github.com/gbelletti)


- ZEBALLOS, Lautaro

  lgzeballos1@gmail.com

  [*@lau2214*](https://github.com/lau2214)

----

## Tecnologías involucradas:

**Lenguaje:** Java 17+, JDK 21

**IDE:** VSCode (Felipe, Julieta, Lautaro), IntelliJ IDEA (Gael, Mateo)

**Build tool:** Maven

**Documentación:** JavaDoc

**Librerías:** GSON (Manejo de JSON), JLine (Contraseña oculta).

----

## Clonar el repositorio 
Para clonar el repositorio, dirijase al directorio donde quiera clonarlo mediante `cd <nombre-directorio>` y luego, ejecute los siguientes comandos:

```
git clone https://github.com/mateopozzo/DeSo.git
cd DeSo
```

----

## Documentación
En docs se encuentra la documentación relacionada a lo desarrollado en UML para Diseño: Diagrama de clases, diagramas de secuencia, DER.
Cabe mencionar que los integrantes del grupo pertenecen a distintos grupos de diseño, por lo que si hay diferencias entre el diagrama de clase y la implementación que generen dudas, favor de consultar al grupo. 

----

## Casos de uso implementados

----
### Caso de uso 01: Login
Para la implementación se utilizaron las siguientes clases:

**Usuario:** Clase que representa a los usuarios del sistema. Contiene atributos como nombre, contraseña y permisos.

**UsuarioDAO (interfaz):** Define las operaciones de acceso a los datos de los usuarios, como la búsqueda por nombre. Su uso permite desacoplar la lógica de negocio del manejo de archivos, aplicando el patrón DAO.

**UsuarioJSONDAO:** Implementación concreta de UsuarioDAO. Gestiona la lectura de los usuarios desde un archivo usuarios.JSON utilizando la librería Gson para la conversión entre objetos Java y JSON.

**GestorAutenticacion:** Clase de la capa de negocio que centraliza la lógica del caso de uso. Se encarga de validar las credenciales ingresadas, consultando al DAO y lanzando excepciones personalizadas cuando corresponde.


### Excepciones personalizadas:

*UsuarioNoEncontradoException:* Se lanza cuando el nombre de usuario no existe en el sistema.

*CredencialesInvalidasException:* Se lanza cuando la contraseña ingresada no coincide con la registrada.

*InterfazLogin:* Clase perteneciente a la capa de presentación. Se comunica con el usuario por consola para solicitar las credenciales. Utiliza la librería JLine para permitir que la contraseña se escriba de forma oculta, mostrando asteriscos (*) en lugar de los caracteres reales.

*Sesion:* Clase que implementa el patrón Singleton, asegurando que solo exista una única sesión activa en el sistema.

----
### Caso de uso 02: Buscar huésped
Para la implementación se utilizaron las siguientes clases:

**Alojado:** Clase abstracta que forma parte del patrón factory. Sus clases hijas son huésped (encargado de habitación) e invitado.

**AlojadoDAO (interfaz):** Interfaz del patrón DAO que recibe la llamada desde el gestor para ejecutar la implementación de buscarHuespedDAO en AlojadoDAOJSON.

**AlojadoDAOJSON:** Implementación concreta del DAO para una base de datos en archivos JSON donde se encuentra el método buscarHuespedDAO. Este lista todos los huéspedes existentes y por cada uno de ellos llama al método cumpleCriterio que devuelve un booleano: Si los criterios coinciden con los datos de este alojado, retorna TRUE y este se agrega a una lista de "Encontrados". 

**AlojadoDTO:** Objeto DTO que se crea al traer alojados desde la base de datos. Este se entrega a la implementación del DAO, que la devuelve en forma de lista de acuerdo a una condición (ver buscarHuespedDAO) hacia el gestor.

**InterfazBusqueda:** GUI que interactua con el usuario en la toma de datos. También se encarga de mostrar por pantalla resultados, mensajes de error/éxito y llamar a métodos como buscarHuesped.

**GestorAlojamiento:** Gestor que contiene el método buscarHuesped. Este recibe un criterio de búsqueda y llama al método buscarHuespedDAO.

**CriteriosBusq:** Clase que se encarga de recibir los cuatro criterios opcionales. Los cuatro criterios son: Nombre, apellido, tipo de documento, número de documento. Si alguno no fue especificado, se inicializa en null. 



----
### Caso de uso 09: Dar de alta huésped
**Alojado:** Clase abstracta que forma parte del patrón factory. Sus clases hijas son huésped (encargado de habitación) e invitado.

**AlojadoDAO (interfaz):** Interfaz del patrón DAO que recibe la llamada desde el gestor para ejecutar la implementación de crearAlojado en AlojadoDAOJSON.

**AlojadoDAOJSON:** Implementación concreta del DAO para una base de datos en archivos JSON donde se encuentra el método crearAlojado. A esta se le pasa un AlojadoDTO y lo agrega a una lista de alojadoDTO
que esta en la interface AlojadoDAO.

**AlojadoDTO:** Objeto DTO que recibe un huesped o invitado y se instancia. Este es pasado a crearAlojado.

**InterfazBusqueda:** GUI que interactua con el usuario para crear un Alojado a traves de la toma de datos. También se encarga de mostrar por pantalla resultados, mensajes de error/éxito y llamar a métodos como darAltaHuesped.

**GestorAlojamiento:** Gestor que contiene el método darAltaHuesped. A este se le pasa como parametro el invitado instanciado con datos validos y crea un DTO con el Invitado como parametro y llama a crearAlojado.


----
### Caso de uso 10: Modificar huésped

```DESCRIPCION```

----
### Caso de uso 11: Dar de baja huésped

```DESCRIPCION```

---

## Patrones implementados
| Patrón       | Nro de caso de uso | Nombre de caso de uso |
|--------------|--------------------|-----------------------|
| **DAO**      | `1, 2, 9, 10, 11`  | Todos                 |
| **Singleton**     | `1`                | Login                 |
| **Factory**  | `9`                | Dar de alta huésped   |

