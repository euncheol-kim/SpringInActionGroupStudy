package springinaction.tacos.entity;

import lombok.*;


@Data
@RequiredArgsConstructor
public class Ingredient {

    private final String id;
    private final String name;
    private final Type type;

}
