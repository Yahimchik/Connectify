package org.example.socialnetwork.validation.password;

import jakarta.validation.Constraint;
import org.example.socialnetwork.validation.email.EmailValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "Password invalid";
    Class[] groups() default {};
    Class[] payload() default {};
}
