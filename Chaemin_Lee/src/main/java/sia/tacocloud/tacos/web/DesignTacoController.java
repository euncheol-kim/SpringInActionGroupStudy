package sia.tacocloud.tacos.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import sia.tacocloud.tacos.Ingredient;
import sia.tacocloud.tacos.Order;
import sia.tacocloud.tacos.Taco;
import sia.tacocloud.tacos.User;
import sia.tacocloud.tacos.data.IngredientRepository;
import sia.tacocloud.tacos.data.TacoRepository;
import sia.tacocloud.tacos.data.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sia.tacocloud.tacos.Ingredient.*;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;
    private final UserRepository userRepository;

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model, Principal principal){
        // TODO : 식자재 데이터를 모두 조회해서 리스트에 담기
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);

        // TODO : 같은 타입끼리 모델에 담기 -> (type, List<Ingredient>)
        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        model.addAttribute("user", user);

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid @ModelAttribute Taco design, @ModelAttribute Order order, Errors errors){
        if(errors.hasErrors()){
            return "design";
        }

        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
    }
}
