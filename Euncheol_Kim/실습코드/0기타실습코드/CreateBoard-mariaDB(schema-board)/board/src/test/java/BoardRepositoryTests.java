import com.study.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    BoardRepository boardRepository;

    @Test
    public void testClass() {
        System.out.println(boardRepository);
    }
}
