package beer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import beer.entity.User;
import beer.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findByUserName(String username){
        return userRepository.findByUsername(username);
    }

}
