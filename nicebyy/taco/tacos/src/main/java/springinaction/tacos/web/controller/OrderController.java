package springinaction.tacos.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springinaction.tacos.entity.Order;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model){
        model.addAttribute("order",new Order());
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors){

        if(errors.hasErrors()){

            List<FieldError> list = errors.getFieldErrors();
            list.forEach(e -> log.error(e.getDefaultMessage()));
            return "orderForm";
        }

        log.info("order submitted : {}",order);
        return "redirect:/";
    }
}
