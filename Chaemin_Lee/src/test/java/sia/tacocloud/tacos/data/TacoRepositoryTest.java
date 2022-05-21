package sia.tacocloud.tacos.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sia.tacocloud.tacos.Taco;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TacoRepositoryTest {

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void sequenceTest(){
        Taco taco1 = new Taco();
        Taco taco2 = new Taco();
        Taco taco3 = new Taco();

        System.out.println("========================");
        // DB DEQ = 1   |  1
        // DB DEQ = 51  |  2
        // DB DEQ = 51  |  3
        em.persist(taco1);  //DB 1, 51
        em.persist(taco2);  //MEM
        em.persist(taco3);  //MEM

        System.out.println("taco1 = " + taco1.getClass());
        System.out.println("taco2 = " + taco2.getClass());
        System.out.println("taco3 = " + taco3.getClass());
    }
}