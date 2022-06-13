# Part1 추가내용

# HTTP 통신과 관련된 Annotation들

## @RequestMapping (요청을 받을 때)

> 특정 URL에 요청을 보내면 Controller에서 이를 처리하는데, URL과 HTTP Method에 따라 맞는 요청을 받아 처리한다.
> 

```java
@Controller
@Slf4j
@SessionAttributes("order")
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current") // @RequestMapping(value = "/current",method = RequestMethod.GET)
		public String orderForm(@AuthenticationPrincipal User user,Order order){
		...
		
		}
}
```

Class 레벨에 @RequestMapping을 걸어두고 Method레벨에 @GetMapping 같이 특정 HTTP Method를 알 수 있는 애노테이션을 붙이는 방식이 선호되는 방식

## HTTP Method 종류와 애노테이션

### @GetMapping

- HTTP Get 방식의 Method를 처리
- Get Method 요청을 처리하는데 추가로 전달할 요청 관련 정보는 쿼리파라미터로 같이 전달
- HTTP Form 에 입력한 값은 쿼리 파라미터로 전달해서 view를 리턴하거나 정보를 리턴함
- @RequestParam 이나 @ModelAttribute를 통해 전달 받은 요청 파라미터를 처리

```java
// RequestParam : String, Integer 과 같은 primitive 타입
		@GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user,Model model
    , @RequestParam int limit,@RequestParam int offset){

			PageRequest pageable = PageRequest.of(offset,limit);
      model.addAttribute("orders",orderRepository.findByUserOrderByPlacedAtDesc(user,pageable));
      
			return "orderList";
    }
```

```java
// ModelAttribute : object 타입

@GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal User user,@ModelAttribute Order order){
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
```

### @PostMapping

- HTTP Post 방식의 Method를 처리
- 요청을 처리하는데 추가로 전달할 요청 관련 정보는 HTTP body에도 담아 전달
- HTTP Form 에 입력한 값은 Get방식과 마찬가지로 쿼리스트링 방식으로에 전달해서 로직을 수행
- @RequestBody 나 @ModelAttribute를 통해 전달 받은 요청 파라미터를 처리
- 전달 받은 데이터는 MessageConverter와 자바 reflection을 통해 객체 바인딩이 가능

```java
@PostMapping // ModelAttribute 사용 (파라미터 형태로 보내질 때)
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user){

        if(errors.hasErrors()){
            return "orderForm";
        }

        log.info("order submitted : {}",order);
        order.setUser(user);
        orderRepository.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
```

```java
@PostMapping // RequestBody 사용 (Body에 데이터를 담아 보내질 때)
    public String processOrder(@Valid @RequestBody Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user){

        if(errors.hasErrors()){
            return "orderForm";
        }
        order.setUser(user);
        orderRepository.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
```

### @PutMapping, @PatchMapping, @DeleteMapping

- Html Form은 Get과 Post만 동작하지만, PostMapping 처럼 동작하는 HTTP Method들이 있음
- PutMapping 과 PatchMapping은 수정관련 요청 Method인데 차이점은 부분수정vs전체수정
- DeleteMapping은 말 그대로 데이터를 삭제할 때 요청하는 Method

## @RequestParam

- 요청 파라미터를 받는 방식 중 하나로 primitive 타입만 받는다.
- 보낼때 파마미터 변수와 메소드의 파라미터 변수와 같으면 생략이 가능함

## @ResponseBody (데이터를 보낼 때)

ViewResolver를 통해 서버에서 view 를 렌더링해서 보여주는 응답을 할 수 있지만

API서버로서 데이터를 보낼 때는 String 형식이나 Json 형식으로 body데이터를 보낼 수 있음

 

```java
		@GetMapping("/list")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<List<Taco>> getTacos(@AuthenticationPrincipal User user){

        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
        List<Order> orders = orderRepository.findByUserOrderByPlacedAtDesc(user, pageable);

        return orders.stream()
                .map(Order::getTacos)
                .collect(Collectors.toCollection(ArrayList::new));
    }
```

헤더 설정이 필요한 경우 HttpEnity 형태로 return 도 가능함

## @PathVariable

> 경로 변수라고도 하며 URL에 쿼리스트링 형식으로 데이터를 보내는 것과 다르게 URL 경로중 일부로 작용함.
ex) [www.tacocloud.com/](http://www.taco.com/)taco/1
> 

```java
		private final TacoRepository tacoRepository;
    @GetMapping("/{tacoNum}")
		@ResponseBody
    public Taco getTaco(@PathVariable Long tacoNum){

        return tacoRepository.findById(tacoNum).get();
    }
```

위와 같이 쿼리 스트링 형식이 아닌 경로에 포함된 변수로서 사용이 가능

## @RedirectAttributes

> 요청한 데이터와 관련되거나 포함된 URL을 다시 redirect해서 보여줄때 사용한다. 예를들어 PRG (post redirect get ) 방식으로 다시 Get을 요청 하게 만들 때 사용함
> 

```java
		private final TacoRepository tacoRepository;

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors,RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "design";
        }

        Taco savedTaco = tacoRepository.save(design);
        redirectAttributes.addAttribute("tacoNum",savedTaco.getId());

        return "redirect:/taco/{tacoNum}";
    }
```

## @ModelAttribute

> ModeAttribute는 object 와 String 을 서로 변환, 역변환을 도와주는 타입 컨버터가 적용되어 있다. RequestBody와 차이점은 ModelAttribute는 요청 파라미터에만 적용이 된다. 그래서 Order를 쓸 때도 애노테이션을 생략해도 동작이 가능했음. (RequestBody는 불가능)
> 

Jackson2HttpMessageConverter 로 인해 컨버전이 동작하는데 reflection 기반이기 때문에 객체에 getter만 선언해도 동작이 됨

## 메소드레벨에서 @ModelAttribute 사용

```java
@ModelAttribute("orders")
public List<Order> orders(@AuthenticationPrincipal User user){

    Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
    List<Order> orders = orderRepository.findByUserOrderByPlacedAtDesc(user, pageable);
    return orders;

}
```

@ModelAttribute를 만약 메소드레벨에서 사용하면 지정한 변수명은 모든 요청에대해 자동으로 model.setAttribute() 가 동작해서 model에 담기게 됨