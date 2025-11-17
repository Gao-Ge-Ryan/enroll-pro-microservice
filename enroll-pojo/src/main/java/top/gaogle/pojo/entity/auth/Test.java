package top.gaogle.pojo.entity.auth;

import top.gaogle.framework.commons.pojo.SuperEntity;

import java.io.Serializable;

public class Test extends SuperEntity implements Serializable {

    private String id;

    private String name;

    private Integer age;

    private Long price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", price=" + price +
                '}';
    }
}
