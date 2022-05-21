package springinaction.tacos.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue
    Long id;
    String name;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL) // team
    List<User> users = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
