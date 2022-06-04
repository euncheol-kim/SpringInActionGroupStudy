package springinaction.tacos.web.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 주문 도메인 구성 속성 클래스
 */
@Component
@ConfigurationProperties(prefix = "taco-orders")
@Data
@Validated
public class OrderProps {

    @Range(min=5,max=25,message = "must be in 5~25")
    private int pageSize = 20;

}
