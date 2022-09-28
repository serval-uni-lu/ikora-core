package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.runner.Resolved;

public class VariableType extends BaseType{
    public VariableType(String name) {
        super(name, null);
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public boolean isValid(Resolved resolved) {
        if(resolved.isResolved()){
            return false;
        }

        if(resolved.getOrigin() == null){
            return false;
        }

        return resolved.getOrigin().isVariable();
    }
}
