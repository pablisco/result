package com.pablisco.result;

import org.junit.Test;

import static com.pablisco.result.ResultAssertions.assertThat;

public class ResultTest {

    private static final Character CHAR_VALUE = '9';
    private static final String SUCCESS_VALUE = "success";
    private static final String FAILURE_VALUE = "failure";
    private static final int INTEGER_VALUE = 123;

    @Test
    public void shouldBeSuccessful_withSuccess() throws Exception {
        Result<String, String> result = Result.success(SUCCESS_VALUE);

        assertThat(result.getSuccessOrElse("invalid success")).isEqualTo(SUCCESS_VALUE);
        assertThat(result).hasSuccess(SUCCESS_VALUE);
    }

    @Test
    public void shouldNotBeSuccessFul_withFailure() throws Exception {
        Result<String, String> result = Result.failure(FAILURE_VALUE);

        assertThat(result.getFailureOrElse("invalid failure")).isEqualTo(FAILURE_VALUE);
        assertThat(result).hasFailure(FAILURE_VALUE);
    }

    @Test
    public void shouldReturnAlternative_whenRequestingFailureOnSuccess() throws Exception {
        Result<String, String> result = Result.success(SUCCESS_VALUE);

        String alternative = result.getFailureOrElse("alternative failure");

        assertThat(alternative).isEqualTo("alternative failure");
    }

    @Test
    public void shouldReturnAlternative_whenRequestingSuccessOnFailure() throws Exception {
        Result<String, String> result = Result.failure(FAILURE_VALUE);

        String alternative = result.getSuccessOrElse("alternative success");

        assertThat(alternative).isEqualTo("alternative success");
    }

    @Test
    public void shouldTransformSuccess() throws Exception {
        Result<Character, String> result = Result.<String, String>success(SUCCESS_VALUE)
            .map(s -> INTEGER_VALUE)
            .map(character -> CHAR_VALUE);

        assertThat(result).hasSuccess(CHAR_VALUE);
    }

    @Test
    public void shouldRemainFailure_whenMaping() throws Exception {
        Result<Character, String> result = Result.<String, String>failure(FAILURE_VALUE)
            .map(s -> CHAR_VALUE);

        assertThat(result).hasFailure(FAILURE_VALUE);
    }

    @Test
    public void shouldTransformSuccess_withFailure() throws Exception {
        Result<Character, String> failure = Result.<String, String>failure(FAILURE_VALUE)
            .flatMap(s -> Result.success(CHAR_VALUE));

        assertThat(failure).hasFailure(FAILURE_VALUE);
    }

    @Test
    public void shouldNotTransformSuccess_intoSuccess() throws Exception {
        Result<String, String> result = Result.<String, String>success("first success")
            .flatMap(s -> Result.success(SUCCESS_VALUE));

        assertThat(result).hasSuccess(SUCCESS_VALUE);
    }

    @Test
    public void shouldTransformToFailure_fromSuccess() throws Exception {
        Result<String, Character> result = Result.<String, String>success(SUCCESS_VALUE)
            .bimap(
                s -> Result.failure(CHAR_VALUE),
                f -> Result.failure(Character.MIN_VALUE)
            );

        assertThat(result).hasFailure(CHAR_VALUE);
    }

    @Test
    public void shouldTransformToSuccess_fromFailure() throws Exception {
        Result<String, Character> result = Result.<String, String>failure(FAILURE_VALUE)
            .bimap(
                s -> Result.success(SUCCESS_VALUE),
                f -> Result.success(SUCCESS_VALUE)
            );

        assertThat(result).hasSuccess(SUCCESS_VALUE);
    }

    @Test
    public void shouldSwapSuccessToFailure() throws Exception {
        Result<String, Integer> result = Result.success(SUCCESS_VALUE);

        Result<Integer, String> swapped = result.swap();

        assertThat(swapped).hasFailure(SUCCESS_VALUE);
    }

    @Test
    public void shouldSwapFailureToSuccess() throws Exception {
        Result<String, Integer> result = Result.failure(INTEGER_VALUE);

        Result<Integer, String> swapped = result.swap();

        assertThat(swapped).hasSuccess(INTEGER_VALUE);
    }

}
