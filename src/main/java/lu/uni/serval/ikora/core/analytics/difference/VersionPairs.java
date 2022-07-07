package lu.uni.serval.ikora.core.analytics.difference;

import lu.uni.serval.ikora.core.model.Projects;
import lu.uni.serval.ikora.core.model.SourceNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VersionPairs {
    private List<Pair<SourceNode, SourceNode>> pairs;
    Projects version1;
    Projects version2;

    VersionPairs(List<Pair<SourceNode, SourceNode>> pairs, Projects version1, Projects version2) {
        this.pairs = pairs;
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
}
