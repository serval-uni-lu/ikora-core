package tech.ikora.model;

import tech.ikora.exception.InvalidArgumentException;

import java.io.File;
import java.io.Reader;

public class Source {
    final String inMemory;
    final File file;

    public Source(File file) {
        if(file == null){
            throw new NullPointerException("file cannot be null");
        }

        this.file = file;
        this.inMemory = null;
    }

    public Source(String inMemory) {
        if(inMemory == null){
            throw new NullPointerException("inMemory cannot be null");
        }

        this.inMemory = inMemory;
        this.file = null;
    }

    public Source(Source parentFolder, String name) throws InvalidArgumentException {
        if(parentFolder.isInMemory()){
            throw new InvalidArgumentException("Expected source of type file and not in memory");
        }

        this.file = new File(parentFolder.asFile(), name);
        this.inMemory = null;
    }

    public String getAbsolutePath(){
        if(this.file != null){
            return this.file.getAbsolutePath();
        }

        return "<IN_MEMORY>";
    }

    public String getPath(){
        if(this.file != null){
            return this.file.getPath();
        }

        return "<IN_MEMORY>";
    }

    public String getName(){
        if(this.file != null){
            return this.file.getName();
        }

        return "<IN_MEMORY>";
    }

    public String getDirectory(){
        if(this.file != null){
            return this.file.getParent();
        }

        return "<IN_MEMORY>";
    }

    public boolean isInMemory() {
        return this.file == null;
    }

    public File asFile() {
        return this.file;
    }
}
