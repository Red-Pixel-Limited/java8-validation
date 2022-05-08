# Java 8 FP inspired validation

Project has demonstrable example how Java 8 functional interfaces can be used to implemenent business validation and test conditions.

### How to?

##### Validation

Given an example validation

```
private OperationResult<String> verifyTime() {
    return OperationResult.of(timeValidator.validate(LocalDateTime.now()));
}

private OperationResult<String> verifyName() {
    return OperationResult.success("James");
}
```
It's possible to chain opeations using `flatMap` as follows
```
OperationResult<String> chain =
    new OperationResult<String>()
        .flatMap(this::verifyTime)
        .flatMap(this::verifyName)
        .onSuccess(() -> System.out.println("success"));


assertFalse(chain.hasError());
```
Please refer [tests](/src/test/java/validation/OperationResultTest.java) for more details.

##### Test conditions 

Java 8 functional interfaces can be extended to build your owns. An example can be find below:
```
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
```
Given this it's possible to compose functions as follows:
```
isUuidNull(uuid).and(hasText(s)).isTrue()
```
Please refer [tests](/src/test/java/validation/TestConditionTest.java) for more details.
