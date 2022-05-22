package sia.tacocloud.tacos;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@SequenceGenerator(name = "taco_seq_generator", sequenceName = "taco_seq")
public class Taco {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taco_seq_generator")
    private Long id;
    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    @PrePersist
    void createdAt(){
        this.createdAt = new Date();
    }
}
