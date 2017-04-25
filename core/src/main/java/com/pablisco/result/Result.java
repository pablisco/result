package com.pablisco.result;

public abstract class Result<S, F> {

    public static <S, F> Result<S, F> success(S success) {
        return new Success<S, F>(success);
    }

    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<S, F>(failure);
    }

    public abstract boolean isSuccessful();

    public abstract S getSuccess();

    public abstract F getFailure();

    public abstract <S2> Result<S2, F> onSuccess(OnSuccess<S, S2, F> onSuccess);

    public abstract <F2> Result<S, F2> onFailure(OnFailure<S, F, F2> onFailure);

    private static class Failure<S, F> extends Result<S, F> {

        private F failure;

        private Failure(F failure) {

            this.failure = failure;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public S getSuccess() {
            throw new UnsupportedOperationException("Can't get success from failure Result. Did you check isSuccessful()?");
        }

        @Override
        public F getFailure() {
            return failure;
        }

        @Override
        public <S2> Result<S2, F> onSuccess(OnSuccess<S, S2, F> onSuccess) {
            return failure(failure);
        }

        @Override
        public <F2> Result<S, F2> onFailure(OnFailure<S, F, F2> onFailure) {
            return onFailure.transform(failure);
        }
    }

    private static class Success<S, F> extends Result<S, F> {

        private S success;

        private Success(S success) {
            this.success = success;
        }

        @Override
        public boolean isSuccessful() {
            return true;
        }

        @Override
        public S getSuccess() {
            return success;
        }

        @Override
        public F getFailure() {
            throw new UnsupportedOperationException("Can't get failure from successful Result. Did you check isSuccessful()?");
        }

        @Override
        public <S2> Result<S2, F> onSuccess(OnSuccess<S, S2, F> onSuccess) {
            return onSuccess.transform(success);
        }

        @Override
        public <F2> Result<S, F2> onFailure(OnFailure<S, F, F2> onFailure) {
            return success(success);
        }
    }

    public interface OnSuccess<S1, S2, F> {
        Result<S2, F> transform(S1 success);
    }

    public interface OnFailure<S, F1, F2> {
        Result<S, F2> transform(F1 failure);
    }

}