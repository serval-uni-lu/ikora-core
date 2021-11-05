package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;

public class FileMemory extends PathMemory {
    private final SourceFile file;

    public FileMemory(SourceFile file){
        this.file = file;
    }

    @Override
    public boolean isAcceptable(Node node) {
        return super.isAcceptable(node) && ((SourceNode)node).getSourceFile() == this.file;
    }
}
