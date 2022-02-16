package formpackage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLInputTypeAnnotation {
    HTMLInputType HTML_INPUT_TYPE();
    boolean required();
    String labelname() default "";
}

