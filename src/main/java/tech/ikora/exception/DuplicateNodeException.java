package tech.ikora.exception;

public class DuplicateNodeException extends Exception {
    private String node;

    public DuplicateNodeException(String node){
        this.node = node;
    }

    @Override
    public String getMessage() {
        return "Duplicate node in tree hierarchy : " + node;
    }
}
