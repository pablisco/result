package org.example;

import com.pablisco.result.Result;
import org.junit.Test;

import static com.pablisco.result.ResultAssertions.assertThat;

public class ExternalUseTest {

    private static class Success {}

    private static class Failure {}

    @Test
    public void shouldAccessSuccess_whenOutsideOfPackage() throws Exception {
        Success expectedSuccess = new Success();
        Result<Success, Failure> result = Result.success(expectedSuccess);

        assertThat(result).hasSuccess(expectedSuccess);
    }

    @Test
    public void shouldAccessFailure_whenOutsideOfPackage() throws Exception {
        Failure expectedFailure = new Failure();
        Result<Success, Failure> result = Result.failure(expectedFailure);

        assertThat(result).hasFailure(expectedFailure);
    }

}
