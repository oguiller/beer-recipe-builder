package beer.resource;

import beer.entity.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.ResourceSupport;

public class UserResource extends ResourceSupport {

    private User user;

    @JsonCreator
    public UserResource(User user) {
        this.user = user;
    }

    public UserResource() {
    }

    public User getUser() {
        return user;
    }

}
