package com.pablisco.result;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ResultTest {

    @Test
    public void shouldBeSuccessful_withSuccess() throws Exception {
        Result<String, String> result = Result.success("success");

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getSuccess()).isEqualTo("success");
    }

    @Test
    public void shouldNotBeSuccessFul_withFailure() throws Exception {
        Result<String, String> result = Result.failure("failure");

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.getFailure()).isEqualTo("failure");
    }

    @Test
    public void shouldThrowException_whenRequestingFailureOnSuccess() throws Exception {
        Result<String, String> result = Result.success("value");

        try {
            result.getFailure();
            failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
        } catch (UnsupportedOperationException e) {
            assertThat(e).hasMessage("Can't get failure from successful Result. Did you check isSuccessful()?");
        }
    }

    @Test
    public void shouldThrowException_whenRequestingSuccessOnFailure() throws Exception {
        Result<String, String> result = Result.failure("value");

        try {
            result.getSuccess();
            failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
        } catch (UnsupportedOperationException e) {
            assertThat(e).hasMessage("Can't get success from failure Result. Did you check isSuccessful()?");
        }

    }
}
