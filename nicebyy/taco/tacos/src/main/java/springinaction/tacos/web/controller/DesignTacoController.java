package springinaction.tacos.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;


import javax.validation.Valid;

import org.springframework.validation.Errors;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import springinaction.tacos.domain.entity.Ingredient;
import springinaction.tacos.domain.entity.Order;
import springinaction.tacos.domain.entity.Taco;
import springinaction.tacos.domain.entity.User;
import springinaction.tacos.domain.repository.IngredientRepository;
import springinaction.tacos.domain.repository.TacoRepository;
import springinaction.tacos.domain.repository.UserRepository;

import static springinaction.tacos.domain.entity.Ingredient.*;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@RequiredArgsConstructor
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private final TacoRepository tacoRepository;    
    private final UserRepository userRepository;


    @GetMapping
    public String showDesignForm(Model model, @AuthenticationPrincipal User user) {
        List<Ingredient> ingredients = new ArrayList<>(ingredientRepository.findAll());

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

//        model.addAttribute("taco", new Taco());
        model.addAttribute("user",user);
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors,Order order) {
        if (errors.hasErrors()) {
            return "design";
        }

        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }
}