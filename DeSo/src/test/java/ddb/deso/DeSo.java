package ddb.deso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.annotation.Rollback;

@SpringBootApplication
@Rollback(true)
public class DeSo {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DeSo.class, args);
    }
}
