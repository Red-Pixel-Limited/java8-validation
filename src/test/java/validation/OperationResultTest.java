package validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OperationResultTest {

    @Mock
    private Validator<LocalDateTime> timeValidator;

    @Test
    public void testChainFunctions() {
        when(timeValidator.validate(any(LocalDateTime.class))).thenReturn(Optional.empty());

        OperationResult<String> chain =
                new OperationResult<String>()
                        .flatMap(this::verifyTime)
                        .flatMap(this::verifyName)
                        .onSuccess(() -> System.out.println("success"));


        assertFalse(chain.hasError());
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowException() {
        when(timeValidator.validate(any(LocalDateTime.class)))
                .thenReturn(Optional.of(new ValidationError() {
                    @Override
                    public String getErrorMessage() {
                        return "Some time validation error";
                    }

                    @Override
                    public String getCode() {
                        return "0x111";
                    }
                }));

        OperationResult.<LocalDateTime, ValidationError>of(timeValidator.validate(LocalDateTime.now()))
                .interruptOnFailure(error -> new IllegalStateException(error.getErrorMessage()));
    }

    private OperationResult<String> verifyTime() {
        return OperationResult.of(timeValidator.validate(LocalDateTime.now()));
    }

    private OperationResult<String> verifyName() {
        return OperationResult.success("James");
    }
}