package beer.controller;

import beer.dto.Error;
import beer.entity.User;
import beer.resource.UserResource;
import beer.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * We are creating a resource UserResource.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    public static final String USER = "user";

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    HttpEntity<UserResource> get(@RequestParam(value = "username", defaultValue = "xile") String username) {
        Optional<User> user = userService.getByUsername(username);
        UserResource resourceUser = new UserResource(user.get());
        return new ResponseEntity<UserResource>(resourceUser, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(name = "signup", value = "signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public
    @ResponseBody
    HttpEntity<Object> signup(@RequestBody User user) {
        Map result = new HashMap();

        if(!StringUtils.isBlank(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        try {
            User savedUser = userService.save(user);
            result.put(USER, savedUser);
            UserResource resourceUser = new UserResource(savedUser);
            resourceUser.add(linkTo(methodOn(UserController.class).get(user.getUsername())).withSelfRel());
            return new ResponseEntity<Object>(resourceUser, HttpStatus.OK);
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
            return new ResponseEntity<Object>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
