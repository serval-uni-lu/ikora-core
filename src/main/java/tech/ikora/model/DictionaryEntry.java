package tech.ikora.model;

public class DictionaryEntry {
    private Node key;
    private Node value;

    public DictionaryEntry(Node key, Node value){
        this.key = key;
        this.value = value;
    }

    public Node getKey(){
        return this.key;
    }

    public Node getValue(){
        return this.value;
    }


}
