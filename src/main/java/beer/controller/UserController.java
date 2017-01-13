package beer.controller;

import beer.dto.Error;
import beer.entity.User;
import beer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * We are creating a resource User.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    public static final String USER = "user";

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> get(@RequestParam(value = "username", defaultValue = "xile") String username) {
        return userService.findByUserName(username);
    }

    @PostMapping(name = "signup", value = "signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    Map signup(@RequestBody User user) {
        User savedUser;
        Map result = new HashMap();
        try {
            savedUser = userService.save(user);
            result.put(USER, savedUser);
        } catch (ConstraintViolationException cve) {
            beer.dto.Error error = new beer.dto.Error();
            Map errors = new HashMap();
            errors.put("domain", "global");

            Iterator it = cve.getConstraintViolations().iterator();
            List<String> validationErrors = new ArrayList<String>();

            while (it.hasNext()) {
                ConstraintViolation cv = (ConstraintViolation) it.next();
                validationErrors.add(cv.getPropertyPath().toString() + " " + cv.getMessage());
            }

            errors.put("message", validationErrors);
            error.setErrors(errors);
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            result.put(Error.ERROR, error);
        } finally {
            return result;
        }
    }
}
