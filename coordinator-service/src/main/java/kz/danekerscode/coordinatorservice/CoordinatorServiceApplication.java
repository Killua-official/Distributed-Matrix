package kz.danekerscode.coordinatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@SpringBootApplication
public class CoordinatorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoordinatorServiceApplication.class, args);
    }

//    @Bean
//    public JsonMessageConverter jsonMessageConverter(){
//        return new ByteArrayJsonMessageConverter();
//    }

}
