package com.pablisco.result;

import org.assertj.core.api.Assertions;

public class ResultAssertions extends Assertions {

    public static <S, F> ResultAssert<S, F> assertThat(Result<S, F> result) {
        return new ResultAssert<>(result);
    }

}
