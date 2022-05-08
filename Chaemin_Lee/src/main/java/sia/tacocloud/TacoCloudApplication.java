package sia.tacocloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class TacoCloudApplication {
    //JAR파일이 실행될 때, 호출되어 실행되는 메서드
    public static void main(String[] args) {
        //애플리케이션 시작 및 스프링 컨테이너 생성
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
