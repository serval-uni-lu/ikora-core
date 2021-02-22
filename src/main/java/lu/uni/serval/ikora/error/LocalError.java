package lu.uni.serval.ikora.error;

import lu.uni.serval.ikora.model.Range;

public abstract class LocalError extends Error {
    private final Range range;

    public LocalError(String message, Range range){
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
