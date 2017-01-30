package beer.util;

import beer.dto.Error;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

public class ControllerUtils {

    public static Map buildGenericError(Exception e) {
        Map result = new HashMap();
        Error error = new Error();
        Map errors = new HashMap();
        errors.put("domain", "global");

        List<String> messages = new ArrayList<String>();
        messages.add(e.getMessage());

        errors.put("message", messages);
        error.setErrors(errors);
        error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        result.put(Error.ERROR, error);

        return result;
    }

    public static Map buildConstraintError(ConstraintViolationException cve) {
        Map result = new HashMap();
        Error error = new Error();
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

        return result;
    }
}
