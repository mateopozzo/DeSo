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
- Caso de uso 01: Login

```DESCRIPCION```

- Caso de uso 02: Buscar huésped

```DESCRIPCION```
- Caso de uso 09: Dar de alta huésped

```DESCRIPCION```

- Caso de uso 10: Modificar huésped

```DESCRIPCION```

- Caso de uso 11: Dar de baja huésped

```DESCRIPCION```

---

## Patrones implementados
| Patrón       | Nro de caso de uso | Nombre de caso de uso |
|--------------|--------------------|-----------------------|
| **DAO**      | `1, 2, 9, 10, 11`  | Todos                 |
| **Singleton**     | `1`                | Login                 |
| **Factory**  | `9`                | Dar de alta huésped   |

