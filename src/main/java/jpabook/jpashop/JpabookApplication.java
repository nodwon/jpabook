package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.querydsl.jpa.Hibernate5Templates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class JpabookApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpabookApplication.class, args);
    }
    @Bean
    Hibernate5Module hibernate5Module(){
        //강제 지연 로딩 설정
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,
                true);
        return hibernate5Module;
    }
}
