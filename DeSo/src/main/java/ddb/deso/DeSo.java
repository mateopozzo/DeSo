package ddb.deso;

import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.login.Sesion;
import ddb.deso.presentacion.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class DeSo {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DeSo.class, args);
    }
}
