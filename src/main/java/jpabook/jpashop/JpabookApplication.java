package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpabookApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpabookApplication.class, args);
    }
    @Bean
    Hibernate5JakartaModule Hibernate5JakartaModule(){
        //강제 지연 로딩 설정
        Hibernate5JakartaModule Hibernate5JakartaModule = new Hibernate5JakartaModule();
        Hibernate5JakartaModule.configure(com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING,
                true);
        return Hibernate5JakartaModule;
    }
}
