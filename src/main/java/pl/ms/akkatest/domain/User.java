package pl.ms.akkatest.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * Created by Marcin on 2017-10-31 12:56
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = -5247931912876437458L;

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    User() {
        // JPA
    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
