package springinaction.tacos.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import springinaction.tacos.domain.entity.Order;
import springinaction.tacos.domain.repository.OrderRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@SessionAttributes("order")
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @GetMapping("/current")
    public String orderForm(){
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus){

        if(errors.hasErrors()){
//
//            List<FieldError> list = errors.getFieldErrors();
//            list.forEach(e -> log.error(e.getDefaultMessage()));
            return "orderForm";
        }

//        log.info("order submitted : {}",order);
        orderRepository.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
