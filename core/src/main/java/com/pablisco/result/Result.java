package com.pablisco.result;

public abstract class Result<S, F> {

    public static <S, F> Result<S, F> success(S success) {
        return new Success<>(success);
    }

    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<S, F>(failure);
    }

    public final boolean isSuccessful() {
        return fold(
            o -> true,
            o -> false
        );
    }

    public final S getSuccess() {
        return fold(
            t -> t,
            unsupported("Can't get success from failure Result. Did you check isSuccessful()?")
        );
    }

    public final F getFailure() {
        return fold(
            unsupported("Can't get failure from successful Result. Did you check isSuccessful()?"),
            t -> t
        );
    }

    /**
     * Maps success to a new required success type, keeping the same failure type.
     */
    public final <S2> Result<S2, F> map(Function<S, S2> f) {
        return fold(
            s -> success(f.apply(s)),
            Result::failure
        );
    }

    public final <S2> Result<S2, F> flatMap(Function<S, Result<S2, F>> onSuccess) {
        return fold(
            onSuccess,
            Result::failure
        );
    }

    public final <S2, F2> Result<S2, F2> bimap(Function<S, Result<S2, F2>> onSuccess, Function<F, Result<S2, F2>> onFailure) {
        return fold(
            onSuccess,
            onFailure
        );
    }

    public final Result<F, S> swap() {
        return fold(
            Result::failure,
            Result::success
        );
    }

    public abstract <T> T fold(Function<S, T> onSuccess, Function<F, T> onFailure);

    private static class Failure<S, F> extends Result<S, F> {

        private F failure;

        private Failure(F failure) {
            this.failure = failure;
        }

        @Override
        public <T> T fold(Function<S, T> onSuccess, Function<F, T> onFailure) {
            return onFailure.apply(failure);
        }

    }

    private static class Success<S, F> extends Result<S, F> {

        private S success;

        private Success(S success) {
            this.success = success;
        }

        @Override
        public <T> T fold(Function<S, T> onSuccess, Function<F, T> onFailure) {
            return onSuccess.apply(success);
        }

    }

    /**
     * Direct copy of the one use in Java 8 for Java 6 support
     */
    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }

    private static <T, R> Function<T, R> unsupported(String message) {
        return t -> { throw new UnsupportedOperationException(message);};
    }

}