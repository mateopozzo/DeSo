# Diseño de gestión hotelera para Hotel Premier con NEXT.js, Springboot y PostgreSQL

## Autores

- POZZO GALDÓN, Mateo

  mateopozzogaldon@gmail.com

  [_@mateopozzo_](https://github.com/mateopozzo)

- SUÁREZ BELLETTI, Gael

  gaelbelletti@gmail.com

  [_@gbelletti_](https://github.com/gbelletti)

- ZEBALLOS, Lautaro

  lgzeballos1@gmail.com

  [_@lau2214_](https://github.com/lau2214)

---

## Tecnologías involucradas:

**Lenguaje:** Java 17+, JDK 21

**IDE:** VSCode (Lautaro), IntelliJ IDEA (Gael, Mateo)

**Build tool:** Maven

**Documentación:** JavaDoc

**Tech stack:**

- NEXT.js, Typescript, Tailwind CSS
- Springboot, PostgreSQL
- Mockito, JUnit

**Librerías:** GSON, Jline, Springboot: Starter, Data JPA, Postgresql, Lombok, Commons-lang3, Annotations (JetBrains), Starter-test, Junit-Jupiter.

---

## Clonar el repositorio

Para clonar el repositorio, dirijase al directorio donde quiera clonarlo mediante y luego, ejecute los siguientes comandos por terminal:

```
git clone https://github.com/mateopozzo/DeSo.git
cd DeSo
git checkout entrega-final-diseño
```

---

## Flujo de trabajo propuesto

Existen dos formas de correr el sistema:

### Archivo .jar

Abra el proyecto en su IDE de preferencia. En la carpeta raíz DeSo ejecute por terminal:

```
java -jar deso-0.0.1-SNAPSHOT.jar
```

Esto compila el proyecto. Podrá acceder a la página en [localhost:8080](http://localhost:8080/)

### Front y back por separado

Abra el proyecto en su IDE de preferencia. Compile el proyecto desde src/main/java/ddb/deso/DeSo.java. Esto compila el back. Luego, dirijase a DeSo/hotel-premier y ejecute por terminal:

```
npm run build
```

Esto compila el front. Es necesario tener npm instalado; puede hacerse con `npm install`. Podrá acceder a la página en [localhost:3000](http://localhost:3000/).

---

## Documentación

En la carpeta _Diagramas_ se encuentra la documentación relacionada a lo desarrollado en UML para Diseño: Diagrama de clases, diagramas de secuencia, DER.

---

## Cambios y mejoras implementadas

- Se movieron las entidades de negocio a su propio paquete `service`.
- Conexión entre estadía y reserva: Ahora están relacionadas mediante un atributo en estadía.
- La grilla ahora permite que se puedan ocupar reservas en caso de uso `Ocupar habitación` pero prohibirlo en caso de uso `Reservar habitación`.
- Control de fechas en consulta a la grilla: La fecha inicial de consulta no puede ser menor al día actual.
- Al querer ocupar una reserva en estadía, esta informa a quién pertenece y relaciona la reserva con la estadía.
- A la hora de buscar un huésped, si no lo encuentra ofrece la opción de crearlo en una nueva pestaña.
- Upgrade de versión NEXT.js debido a vulnerabilidades de versión anterior.
- Creación de archivo `CorsConfig` para evitar errores _forbidden_ al querer conectarse a obtener-reservas y este enviar método `OPTIONS`. Por lo tanto, sacamos toda anotación `@CrossOrigins` de los controllers.
- Correcciones estéticas para que los casos de uso ocupar y reservar sigan el mismo estilo.
