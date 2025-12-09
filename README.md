# Desarrollo de Software: Diseño de gestión hotelera para Hotel Premier con Java
## Autores


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

**IDE:** VSCode (Lautaro), IntelliJ IDEA (Gael, Mateo)

**Build tool:** Maven

**Documentación:** JavaDoc

**Frameworks:** Next.js (front-end), Springboot (Back-end)

**Librerías:** GSON (Manejo de JSON), JLine (Contraseña oculta).

----

## Clonar el repositorio
Para clonar el repositorio, dirijase al directorio donde quiera clonarlo mediante `cd <nombre-directorio>` y luego, ejecute los siguientes comandos:

```
git clone https://github.com/mateopozzo/DeSo.git
cd DeSo
git checkout entrega-final-diseño
```

----

## Flujo de trabajo propuesto

Existen tres formas de correr el sistema
1. Abrir el proyecto en su IDE de preferencia y compile el proyecto desde src/main/java/ddb/deso/DeSo.java, el front-end se podrá acceder desde localhost:8080/.
2. Abrir el proyecto en su IDE de preferencia y compile el proyecto desde src/main/java/ddb/deso/DeSo.java, luego dirijirse a DeSo/hotel-premier y ejecutar `npm run build`, accediendo al sistema desde localhost:3000/

----

## Documentación
En /Diagramas se encuentra la documentación relacionada a lo desarrollado en UML para Diseño: Diagrama de clases, diagramas de secuencia, DER.

----

## Casos de uso implementados

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




