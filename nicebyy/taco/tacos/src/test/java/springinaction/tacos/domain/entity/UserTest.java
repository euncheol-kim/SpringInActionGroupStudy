package springinaction.tacos.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@Rollback(value = false)
@Transactional
class UserTest {

    @Autowired
    EntityManager entityManager;

    Long userId;

    @BeforeEach
    void save(){

        User userA = new User("userA");
        Team team = new Team("team");

        team.users.add(userA);

        userA.setTeam(team);

        entityManager.persist(team);
        userId = userA.getId();

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("team is not null")
    void test(){

        User user = entityManager.find(User.class, userId);
        String teamName = user.getTeam().getName();

        Assertions.assertNotNull(teamName);
    }
}