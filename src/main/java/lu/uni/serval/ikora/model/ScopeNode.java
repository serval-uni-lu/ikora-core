package lu.uni.serval.ikora.model;

import java.util.List;

public interface ScopeNode {
    List<Dependable> findDefinition(Variable variable);
}
