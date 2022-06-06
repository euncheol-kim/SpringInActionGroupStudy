package com.example.mybatis;

import mybatis.model.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// RestAPI 생성 - class내부에서 데이터 생성/삭제/수정/제거

@RestController
public class UserController {
    private Map<String, User> userMap;

    @PostConstruct // 초기화 진행
    public void init() {
        userMap = new HashMap<String, User>();
        userMap.put("1", new User("1", "홍길동", "111-1111", "서울시 강남구 대치1동"));
        userMap.put("2", new User("2", "홍길자", "111-1112", "서울시 강남구 대치2동"));
        userMap.put("3", new User("3", "홍길순", "111-1113", "서울시 강남구 대치3동"));
    }

    @GetMapping("/user/{id}") // id값을 통한 데이터 반환
    public User getUser(@PathVariable("id") String id) {
        return userMap.get(id);
    }

    @GetMapping("/user/all") // 모든 데이터 반환
    public List<User> getUserList() {
        return new ArrayList<User>(userMap.values()); // https://docs.oracle.com/javase/8/docs/api/java/util/Map.html -> Map의 데이터들을 ArrayList로 바꾼다.
    }

    @PutMapping("/user/{id}") //데이터 생성
    public void putUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        User user = new User(id, name, phone, address);
        userMap.put(id, user);
    }

    @PostMapping("/user/{id}") //데이터 수정
    public void postUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        User user = userMap.get(id);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
    }

    @DeleteMapping("/user/{id}") //데이터 삭제
    public void deleteUser(@PathVariable("id") String id){
        userMap.remove(id);
    }

}
