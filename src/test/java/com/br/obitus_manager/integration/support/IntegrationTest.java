package com.br.obitus_manager.integration.support;

import com.br.obitus_manager.ObitusManagerApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Testes de integração embarcados: profile {@code integtest}, H2 em memória, sem infraestrutura externa.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integtest")
@ContextConfiguration(classes = ObitusManagerApplication.class)
public @interface IntegrationTest {
}
