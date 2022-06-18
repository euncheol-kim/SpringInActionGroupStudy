package mybatis.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // lombok annotation : 변수에 대한 getter & setter생성
public class User {
    private String id;
    private String name;
    private String phone;
    private String address;

    public User(String id, String name, String phone, String address) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}
