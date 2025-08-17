package com.br.obitus_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ObitusManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObitusManagerApplication.class, args);
    }

}
