package sia.tacocloud.tacos;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
public class Ingredient {

    //Ingredient 테이블은 따로 커스텀하게 id값을 생성해서 넣기 때문에 '키 자동생성 전략'이 필요없다.
    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}