package com.pablisco.result;

public abstract class Result<S, F> {

    private static final Function FORWARD_FUNCTION = new ForwardFunctions();

    public static <S, F> Result<S, F> success(S success) {
        return new Success<S, F>(success);
    }

    public static <S, F> Result<S, F> failure(F failure) {
        return new Failure<S, F>(failure);
    }

    public final boolean isSuccessful() {
        return fold(
            Result.<S, Boolean>entity(true),
            Result.<F, Boolean>entity(false)
        );
    }

    public final S getSuccess() {
        return fold(
            Result.<S>forward(),
            Result.<F, S>unsupported("Can't get success from failure Result. Did you check isSuccessful()?")
        );
    }

    public final F getFailure() {
        return fold(
            Result.<S, F>unsupported("Can't get failure from successful Result. Did you check isSuccessful()?"),
            Result.<F>forward()
        );
    }

    /**
     * Maps success to a new required success type, keeping the same failure type.
     */
    public final <S2> Result<S2, F> map(final Function<S, S2> f) {
        return fold(
            new Function<S, Result<S2, F>>() {
                @Override
                public Result<S2, F> apply(S s) {
                    return success(f.apply(s));
                }
            },
            new Function<F, Result<S2, F>>() {
                @Override
                public Result<S2, F> apply(F f) {
                    return failure(f);
                }
            }
        );
    }

    public final <S2> Result<S2, F> flatMap(final Function<S, Result<S2, F>> f) {
        return fold(
            new Function<S, Result<S2, F>>() {
                @Override
                public Result<S2, F> apply(S s) {
                    return f.apply(s);
                }
            },
            new Function<F, Result<S2, F>>() {
                @Override
                public Result<S2, F> apply(F f) {
                    return failure(f);
                }
            }
        );
    }

    public final <S2, F2> Result<S2, F2> bimap(
        final Function<S, Result<S2, F2>> onSuccess,
        final Function<F, Result<S2, F2>> onFailure
    ) {
        return fold(
            new Function<S, Result<S2, F2>>() {
                @Override
                public Result<S2, F2> apply(S s) {
                    return onSuccess.apply(s);
                }
            },
            new Function<F, Result<S2, F2>>() {
                @Override
                public Result<S2, F2> apply(F f) {
                    return onFailure.apply(f);
                }
            }
        );
    }

    public final Result<F, S> swap() {
        return fold(
            new Function<S, Result<F, S>>() {
                @Override
                public Result<F, S> apply(S s) {
                    return failure(s);
                }
            },
            new Function<F, Result<F, S>>() {
                @Override
                public Result<F, S> apply(F f) {
                    return success(f);
                }
            }
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
     * Direct copy of the one use in Java 8
     */
    public interface Function<T, R> {
        R apply(T t);
    }

    private static <T, R> Function<T, R> entity(R value) {
        return new EntityFunction<T, R>(value);
    }

    private static <T, R> Function<T, R> unsupported(String message) {
        return new UnsupportedFunctions<T, R>(message);
    }

    @SuppressWarnings("unchecked")
    private static <T> Function<T, T> forward() {
        return (Function<T, T>) FORWARD_FUNCTION;
    }

    private static class EntityFunction<T, R> implements Function<T, R> {

        private final R entity;

        private EntityFunction(R entity) {
            this.entity = entity;
        }

        @Override
        public R apply(T o) {
            return entity;
        }
    }

    private static class UnsupportedFunctions<T, R> implements Function<T, R> {

        private final String message;

        private UnsupportedFunctions(String message) {
            this.message = message;
        }

        @Override
        public R apply(T t) {
            throw new UnsupportedOperationException(message);
        }

    }

    private static class ForwardFunctions<T> implements Function<T, T> {

        @Override
        public T apply(T t) {
            return t;
        }

    }

}