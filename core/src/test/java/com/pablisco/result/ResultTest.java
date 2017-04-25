package com.pablisco.result;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ResultTest {

    private static final Character CHAR_VALUE = '9';
    private static final String SUCCESS_VALUE = "success";
    private static final String FAILURE_VALUE = "failure";
    public static final int INTEGER_VALUE = 123;

    @Test
    public void shouldBeSuccessful_withSuccess() throws Exception {
        Result<String, String> result = Result.success(SUCCESS_VALUE);

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getSuccess()).isEqualTo(SUCCESS_VALUE);
    }

    @Test
    public void shouldNotBeSuccessFul_withFailure() throws Exception {
        Result<String, String> result = Result.failure(FAILURE_VALUE);

        assertThat(result.isSuccessful()).isFalse();
        assertThat(result.getFailure()).isEqualTo(FAILURE_VALUE);
    }

    @Test
    public void shouldThrowException_whenRequestingFailureOnSuccess() throws Exception {
        Result<String, String> result = Result.success(SUCCESS_VALUE);

        try {
            result.getFailure();
            failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
        } catch (UnsupportedOperationException e) {
            assertThat(e).hasMessage("Can't get failure from successful Result. Did you check isSuccessful()?");
        }
    }

    @Test
    public void shouldThrowException_whenRequestingSuccessOnFailure() throws Exception {
        Result<String, String> result = Result.failure(FAILURE_VALUE);

        try {
            result.getSuccess();
            failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
        } catch (UnsupportedOperationException e) {
            assertThat(e).hasMessage("Can't get success from failure Result. Did you check isSuccessful()?");
        }

    }

    @Test
    public void shouldTransformSuccess() throws Exception {
        Result<Character, String> result = Result.<String, String>success(SUCCESS_VALUE)
            .onSuccess(new Result.OnSuccess<String, Integer, String>() {
                @Override
                public Result<Integer, String> transform(String success) {
                    return Result.success(INTEGER_VALUE);
                }
            })
            .onSuccess(new Result.OnSuccess<Integer, Character, String>() {
                @Override
                public Result<Character, String> transform(Integer success) {
                    return Result.success(CHAR_VALUE);
                }
            });

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getSuccess()).isEqualTo(CHAR_VALUE);
    }

    @Test
    public void shouldTransformSuccess_withFailure() throws Exception {
        Result<Character, String> failure = Result.<String, String>failure(FAILURE_VALUE)
            .onSuccess(new Result.OnSuccess<String, Character, String>() {
                @Override
                public Result<Character, String> transform(String success) {
                    return Result.success(CHAR_VALUE);
                }
            });

        assertThat(failure.isSuccessful()).isFalse();
        assertThat(failure.getFailure()).isEqualTo(FAILURE_VALUE);
    }

    @Test
    public void shouldTransformFailure_intoSuccess() throws Exception {
        Result<String, Character> result = Result.<String, String>failure(FAILURE_VALUE)
            .onFailure(new Result.OnFailure<String, String, Character>() {
                @Override
                public Result<String, Character> transform(String failure) {
                    return Result.success(SUCCESS_VALUE);
                }
            });

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getSuccess()).isEqualTo(SUCCESS_VALUE);
    }

    @Test
    public void shouldTransformToSuccess_fromSuccess() throws Exception {
        Result<String, Character> result = Result.<String, String>success(SUCCESS_VALUE)
            .onFailure(new Result.OnFailure<String, String, Character>() {
                @Override
                public Result<String, Character> transform(String failure) {
                    return Result.failure(CHAR_VALUE);
                }
            });

        assertThat(result.isSuccessful()).isTrue();
        assertThat(result.getSuccess()).isEqualTo(SUCCESS_VALUE);
    }

}
