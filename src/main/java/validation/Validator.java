package validation;

import java.util.Optional;

public interface Validator<T> {

    Optional<ValidationError> validate(T source);
}
