package validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static validation.TestCondition.hasText;
import static validation.TestCondition.isUuidNull;

@RunWith(Parameterized.class)
public class TestConditionTest {

    private final TestCondition isUuidNull;
    private final TestCondition hasText;
    private final boolean expectation;

    public TestConditionTest(TestCondition isUuidNull,
                             TestCondition hasText,
                             boolean expectation) {
        this.isUuidNull = isUuidNull;
        this.hasText = hasText;
        this.expectation = expectation;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                {
                        isUuidNull(UUID.randomUUID()),
                        hasText(null),
                        false
                },
                {
                        isUuidNull(UUID.randomUUID()),
                        hasText(" "),
                        false
                },
                {
                        isUuidNull(null),
                        hasText("Yes"),
                        true
                }
        });
    }

    @Test
    public void testConditions() {
        assertEquals(isUuidNull.and(hasText).isTrue(), expectation);
    }
}