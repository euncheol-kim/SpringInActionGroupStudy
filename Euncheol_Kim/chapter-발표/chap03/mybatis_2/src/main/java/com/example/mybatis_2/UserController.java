package com.example.mybatis_2;

import com.example.mybatis_2.mapper.UserMapper;
import mybatis.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    private UserMapper mapper;

    public UserController(UserMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/user/{id}") // 특정 데이터 조회
    public User getUser(@PathVariable("id") String id) {
        return mapper.getUser(id);
    }

    @GetMapping("/user/all") // 모든 데이터 조회
    public List<User> getUserList() {
        return mapper.getUserList();
    }

    @PutMapping("/user/{id}") //데이터 생성
    public void putUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        mapper.insertUser(id, name, phone, address);
    }

    @PostMapping("/user/{id}") //데이터 수정
    public void postUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        mapper.updateUser(id, name, phone, address);
    }

    @DeleteMapping("/user/{id}") //데이터 삭제
    public void deleteUser(@PathVariable("id") String id){
        mapper.deleteUser(id);
    }

}
