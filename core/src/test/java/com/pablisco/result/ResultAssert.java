package com.pablisco.result;

import org.assertj.core.api.AbstractObjectAssert;

public class ResultAssert<S, F> extends AbstractObjectAssert<ResultAssert<S, F>, Result<S, F>> {

    public ResultAssert(Result<S, F> actual) {
        super(actual, ResultAssert.class);
    }

    public ResultAssert<S, F> isSuccessful() {
        isNotNull();
        if (!actual.isSuccessful()) {
            failWithMessage(actual + " not successful but expected successful");
        }
        return myself;
    }

    public ResultAssert<S, F> isNotSuccessful() {
        if (actual.isSuccessful()) {
            failWithMessage(actual + " successful but expected not successful");
        }
        return myself;
    }

    public ResultAssert<S, F> hasSuccess(S success) {
        isSuccessful();
        if (!actual.map(s -> s.equals(success)).isSuccessful()) {
            failWithMessage(actual + " expected to have success: " + success);
        }
        return myself;
    }

    public ResultAssert<S, F> hasFailure(F failure) {
        isNotSuccessful();
        if (!actual.swap().map(f -> f.equals(failure)).isSuccessful()) {
            failWithMessage(actual + " expected to have failure: " + failure);
        }
        return myself;
    }

}
