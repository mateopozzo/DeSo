# Diseño de gestión hotelera para Hotel Premier con NEXT.js, Springboot y PostgreSQL

## Autores

- ANTUÑA, Felipe

  feliantuna@gmail.com

  [_@felipeantuna_](https://github.com/felipeantuna)

- GRASSI, Julieta

  jjulietagrassi@gmail.com

  [_@jjuligrassi_](https://github.com/jjuligrassi)

- POZZO GALDÓN, Mateo

  mateopozzogaldon@gmail.com

  [_@mateopozzo_](https://github.com/mateopozzo)

- SUÁREZ BELLETTI, Gael

  gaelbelletti@gmail.com

  [_@gbelletti_](https://github.com/gbelletti)

- ZEBALLOS, Lautaro

  lgzeballos1@gmail.com

  [_@lau2214_](https://github.com/lau2214)

## Tecnologías involucradas:

**Lenguaje:** Java 17+, JDK 21

**IDE:** VSCode (Felipe, Julieta), IntelliJ IDEA (Gael, Lautaro, Mateo)

**Build tool:** Maven

**Documentación:** JavaDoc

**Tech stack:**

- NEXT.js, Typescript, Tailwind CSS
- Springboot, PostgreSQL
- Mockito, JUnit, Postman

**Librerías:** GSON, Jline, Springboot: Starter, Data JPA, Postgresql (Hosteado por servidor Aiven), Lombok, Commons-lang3, Annotations (JetBrains), Starter-test, Junit-Jupiter.

## Clonar el repositorio

Para clonar el repositorio, dirijase al directorio donde quiera clonarlo mediante la terminal y luego, ejecute los siguientes comandos por terminal:

```
git clone https://github.com/mateopozzo/DeSo.git
cd DeSo
```

El directorio _BaseDeDatos_ contiene el script de población de la base de datos PostgreSQL. El directorio Diagramas contiene los diagramas hechos para Diseño.
El directorio DeSo contiene:

- Directorio _hotel-premier_: Proyecto NEXT
- Directorio _src/main/java_: Back-end

## Flujo de trabajo propuesto

Abra el proyecto en su IDE de preferencia. Compile el proyecto desde src/main/java/ddb/deso/DeSo.java. Esto compila el back. Luego, dirijase a DeSo/hotel-premier y ejecute por terminal:

```
npm run build
```

Esto compila el front. Es necesario tener npm instalado; puede hacerse con `npm install`. Podrá acceder a la página en [localhost:3000](http://localhost:3000/).

Las credenciales de acceso son:

```
usuario: conserje
contraseña: conserje123
```

El login es únicamente una validación estática con el back, ya que el sistema no almacena cookies.

## Documentación

En la carpeta _Diagramas_ se encuentra la documentación relacionada a lo desarrollado en UML para Diseño: Diagrama de clases, diagramas de secuencia, DER.

## Seed de base de datos

Utilizamos una base PostgreSQL gratuita de la página console.aiven.io/ cuyo script se encuentra dentro de la carpeta _BaseDeDatos_. El archivo llamado `poblacion.bdd.sql` se debe ejecutar de la siguiente forma:

### pgadmin4

Un software open source para manipular bases de datos PostgreSQL. Debe generar una nueva conexión en _Servers_ con los siguientes datos:

```
nombre: <a elección>
host: discipulos-discipulos.k.aivencloud.com
port: 21073
```

Las credenciales de acceso para la base de datos se encuentran en DeSo/main/resources/application.properties bajo spring.datasource.username y spring.datasource.password

Luego, abra la query tool. Puede pegar el contenido del script o importarlo. Corra cada bloque seleccionando de inicio a fin y apretando F5, o bien, el botón "Run".

_Cabe mencionar que al momento de entrega de este trabajo, la base de datos ya se encuentra poblada con este script, respetando la disposición del DER._

## Patrones implementados

| Patrón        | Nro de caso de uso | Nombre de caso de uso      |
| ------------- | ------------------ | -------------------------- |
| **DAO**       | `2, 4, 5, 6, 7, 10, 11`  | Todos                      |
| **Singleton** | `1`                | Login                      |
| **Factory**   | `9`                | Dar de alta huésped        |
| **Generics**  | `1, 2, 9, 10, 11`  | Todos (ManejadorJSON.java) |
| **Strategy**  | `7`                | Facturar                   |

_DAO_ para abstraer el acceso a la capa de datos de la capa de negocio

_Singleton_ (login) donde nos aseguramos de que la instancia de autenticación sea única

_Factory_ (Alojado, Invitado, Huesped) donde el patrón se utiliza para crear un invitado (alojado que no puede ser responsable de habitación) o un huésped (alojado que es responsable de habitación y tiene CUIT)

_Strategy_ (Factura) donde el patrón nos permite separar la lógica de guardar una factura de la lógica de _cómo_ hacerlo. Cumple con el principio open/closed de SOLID. Permite cambiar el tipo de exportación en tiempo de ejecución.
