package lu.uni.serval.utils;

public interface Differentiable<T> {
    double indexTo(Differentiable<T> other);
}
