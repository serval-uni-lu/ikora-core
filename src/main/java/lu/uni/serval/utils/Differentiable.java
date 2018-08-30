package lu.uni.serval.utils;

public interface Differentiable<T> {
    double difference(Differentiable<T> other);
}
