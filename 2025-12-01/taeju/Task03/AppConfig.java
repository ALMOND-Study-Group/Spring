package org.mycompany.test.interfaceDi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    public Calculator add(){
        return new AddCalculator();
    }

    @Bean
    public Calculator sub(){
        return new Subtract();
    }
}
