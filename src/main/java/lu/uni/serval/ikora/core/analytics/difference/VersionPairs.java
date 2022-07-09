package lu.uni.serval.ikora.core.analytics.difference;

import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.model.SourceNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class VersionPairs {
    private final List<Pair<SourceNode, SourceNode>> pairs;
    private final Set<Edit> edits;
    Projects version1;
    Projects version2;

    VersionPairs(List<Pair<SourceNode, SourceNode>> pairs, Set<Edit> edits, Projects version1, Projects version2) {
        this.pairs = pairs;
        this.edits = edits;
        this.version1 = version1;
        this.version2 = version2;
    }

    public Optional<SourceNode> findPrevious(SourceNode node){
        if(node == null){
            throw new NullPointerException();
        }

        if(!version2.contains(node.getProject())){
            throw new IllegalArgumentException("Illegal version");
        }

        return pairs.stream()
                .filter(p -> p.getRight() == node)
                .map(Pair::getLeft)
                .filter(Objects::nonNull)
                .findAny();
    }

    public Set<Edit> getEdits(){
        return this.edits;
    }

    public Projects getLeftVersion() {
        return version1;
    }

    public Projects getRightVersion() {
        return version2;
    }
}
