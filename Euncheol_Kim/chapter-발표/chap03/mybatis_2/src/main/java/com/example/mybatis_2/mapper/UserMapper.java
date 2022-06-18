package com.example.mybatis_2.mapper;

import mybatis.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

// DAO :: 데이터베이스에 접근하는 객체
@Mapper //Mapper 인터페이스
public interface UserMapper {

    @Select("SELECT * FROM User WHERE id=#{id}")
    User getUser(@Param("id") String id); //전달된 id를 가지고 database에서 조회를해서 User 객체로 반환하는 api
    // @Param어노테이션 덕분에 매개변수 id가 #{id}에 매핑이 된다.

    @Select("SELECT * FROM User")
    List<User> getUserList();

    @Insert("INSERT INTO User VALUES(#{id}, #{name}, #{phone}, #{address})")
    int insertUser(@Param("id") String id, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Update("UPDATE User SET name=#{name}, phone=#{phone}, address=#{address} WHERE id=#{id}")
    int updateUser(@Param("id") String id, @Param("name") String name, @Param("phone") String phone, @Param("address") String address);

    @Delete("DELETE FROM User WHERE id=#{id}")
    int deleteUser(@Param("id") String id);
}
