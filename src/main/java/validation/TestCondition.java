package validation;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public interface TestCondition extends Supplier<Boolean> {

    static TestCondition isUuidNull(UUID uuid) {
        return () -> Objects.isNull(uuid);
    }

    static TestCondition hasText(String s) {
        return () -> s != null && !s.trim().isEmpty();
    }

    default TestCondition and(TestCondition other) {
        return () -> {
            final boolean isValid = this.get();
            return isValid ? other.get() : false;
        };
    }

    default boolean isTrue() {
        return this.get();
    }
}
