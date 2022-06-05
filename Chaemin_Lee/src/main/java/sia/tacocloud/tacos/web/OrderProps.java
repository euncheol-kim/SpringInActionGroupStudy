package sia.tacocloud.tacos.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "taco.orders")
@Data
@Validated
public class OrderProps {

    @Min(value = 5, message = "5부터 25사이의 값이어야 합니다.")
    @Max(value = 25, message = "5부터 25사이의 값이어야 합니다.")
    private int pageSize;
}
