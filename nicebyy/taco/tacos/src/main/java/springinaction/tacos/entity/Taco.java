package springinaction.tacos.entity;

import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {

    @NotBlank
    @Size(min=5,message = "최소 5글자 필요")
    private String name;

    @Size(min=1, message = "최소 하나이상의 재료필요")
    private List<String> ingredients;
}
