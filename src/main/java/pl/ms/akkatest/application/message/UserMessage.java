package pl.ms.akkatest.application.message;

import pl.ms.akkatest.domain.User;

import java.io.Serializable;

/*
 * Created by Marcin on 2017-12-05 09:40
 */
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 545599307993923558L;

    private User user;

    public UserMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "user=" + user +
                '}';
    }
}
