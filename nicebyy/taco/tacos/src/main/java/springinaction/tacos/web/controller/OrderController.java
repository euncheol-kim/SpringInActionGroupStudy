package springinaction.tacos.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import springinaction.tacos.domain.entity.User;
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
    private final OrderProps orderProps;

    @GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal User user,Order order){
        if (order.getDeliveryName() == null) {
            order.setDeliveryName(user.getFullname());
        }
        if (order.getDeliveryStreet() == null) {
            order.setDeliveryStreet(user.getStreet());
        }
        if (order.getDeliveryCity() == null) {
            order.setDeliveryCity(user.getCity());
        }
        if (order.getDeliveryState() == null) {
            order.setDeliveryState(user.getState());
        }
        if (order.getDeliveryZip() == null) {
            order.setDeliveryZip(user.getZip());
        }
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user){

        if(errors.hasErrors()){
//
//            List<FieldError> list = errors.getFieldErrors();
//            list.forEach(e -> log.error(e.getDefaultMessage()));
            return "orderForm";
        }

//        log.info("order submitted : {}",order);
        order.setUser(user);
        orderRepository.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user,Model model){

        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());

        model.addAttribute("orders",
                orderRepository.findByUserOrderByPlacedAtDesc(user,pageable));

        return "orderList";
    }
}
