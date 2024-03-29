package springinaction.tacos.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springinaction.tacos.domain.entity.Ingredient;
import springinaction.tacos.domain.repository.IngredientRepository;

import java.net.URI;
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

