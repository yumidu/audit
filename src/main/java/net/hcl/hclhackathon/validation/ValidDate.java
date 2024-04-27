package net.hcl.hclhackathon.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target( { ElementType.FIELD,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidDateValidator.class)
public @interface ValidDate {
     //error message
     public String message() default "Invalid Date";
     //represents group of constraints
     public Class<?>[] groups() default {};
     //represents additional information about annotation
     public Class<? extends Payload>[] payload() default {};
}
