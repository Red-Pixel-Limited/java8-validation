package validation;

import static java.util.Objects.isNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OperationResult<T> {

    private T item;

    private ValidationError error;

    public boolean hasError() {
        return !isNull(error);
    }

    public OperationResult<T> flatMap(Supplier<OperationResult<T>> result) {
        return hasError() ? this : result.get();
    }

    public OperationResult<T> onSuccess(Runnable action) {
        if (!hasError()) {
            action.run();
        }
        return this;
    }

    public OperationResult<T> interruptOnFailure(Function<ValidationError, RuntimeException> exceptionPredicate) {
        if (hasError()) {
            throw exceptionPredicate.apply(error);
        }
        return this;
    }

    public static <T> OperationResult<T> failure(ValidationError error) {
        return new OperationResult<>(null, error);
    }

    public static <T> OperationResult<T> success(T item) {
        return new OperationResult<>(item, null);
    }

    public static <T, E extends ValidationError> OperationResult<T> of(Optional<E> optional) {
        return optional.map(OperationResult::<T>failure)
                .orElse(new OperationResult<>());
    }
}
