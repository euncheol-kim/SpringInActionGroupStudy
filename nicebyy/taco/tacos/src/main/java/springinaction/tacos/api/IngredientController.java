package springinaction.tacos.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springinaction.tacos.domain.entity.Ingredient;
import springinaction.tacos.domain.repository.IngredientRepository;

import java.util.Optional;

//@RestController
//@RequestMapping(path="/api/ingredients", produces="application/json")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping
    public Iterable<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findIngredientById(@PathVariable String id){
        Optional<Ingredient> findIngredient = ingredientRepository.findById(id);

        return findIngredient.map(ingredient -> new ResponseEntity<>(ingredient, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }
}

@Service
@Slf4j
@RequiredArgsConstructor
public class TacoCloudClient {

    private final RestTemplate rest;
    private final Traverson traverson;

    //
    // POST examples
    //
    public Ingredient createIngredient(Ingredient ingredient) {
        return rest.postForObject("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }

    // 생성된 URI 반환
    public URI createIngredient(Ingredient ingredient) {
        return rest.postForLocation("http://localhost:8080/ingredients",
                ingredient, Ingredient.class);
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity =
                rest.postForEntity("http://localhost:8080/ingredients",
                        ingredient,
                        Ingredient.class);
        log.info("New resource created at " + responseEntity.getHeaders().getLocation());
        return responseEntity.getBody();
    }
}