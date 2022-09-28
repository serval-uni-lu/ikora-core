package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseType;

import java.util.Collections;
import java.util.Set;

public abstract class Convertor {
    private final Set<Class<? extends BaseType>> types;

    protected Convertor(Set<Class<? extends BaseType>> types) {
        this.types = types;
    }

    protected Convertor(Class<? extends BaseType> type){
        this.types = Collections.singleton(type);
    }

    public boolean accept(BaseType type) {
        return types.contains(type.getClass());
    }

    abstract <T> T convert(Resolved value, Class<T> type) throws RunnerException;
}
