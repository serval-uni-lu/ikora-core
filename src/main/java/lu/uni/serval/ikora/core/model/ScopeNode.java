package lu.uni.serval.ikora.core.model;

import java.util.List;

public interface ScopeNode {
    List<Dependable> findDefinition(Variable variable);
}
