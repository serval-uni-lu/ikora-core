package tech.ikora.builder;

import tech.ikora.libraries.builtin.keywords.ImportLibrary;
import tech.ikora.libraries.builtin.keywords.ImportResource;
import tech.ikora.libraries.builtin.keywords.ImportVariables;
import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.model.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DynamicImports {
    private Map<KeywordDefinition, ResourcesTable> resources;
    private Map<KeywordDefinition, Set<Library>> libraries;
    private Map<KeywordDefinition, Map<Resources, Set<Token>>> variables;

    public DynamicImports(){
        resources = new HashMap<>();
        libraries = new HashMap<>();
        variables = new HashMap<>();
    }

    public SourceNode findNode(String value){
        throw new NotImplementedException("Need to get some more work on the imports");
    }


    public void add(KeywordDefinition parent, Step step){
        step.getKeywordCall().ifPresent(call -> {
            if(!call.getClass().isAssignableFrom(LibraryKeyword.class)){
                return;
            }

            if(call.getClass().isAssignableFrom(ImportLibrary.class)){
                addLibrary(parent, call.getArgumentList());
            }
            else if(call.getClass().isAssignableFrom(ImportResource.class)){
                addResources(parent, call.getArgumentList());
            }
            else if(call.getClass().isAssignableFrom(ImportVariables.class)){
                addVariables(parent, call.getArgumentList());
            }
        });
    }

    private void addLibrary(KeywordDefinition parent, List<Argument> argumentList){
        if(!argumentList.isEmpty()){
            libraries.putIfAbsent(parent, new HashSet<>());
            Set<Library> current = libraries.get(parent);

            Library library = new Library(argumentList.get(0).getNameToken(), getParams(argumentList), Token.empty());
            current.add(library);
        }
    }

    private void addResources(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            resources.putIfAbsent(parent, new ResourcesTable());
            ResourcesTable current = resources.get(parent);

            Token name = values.get(0).getNameToken();
            File file = new File(name.getText());

            Resources resource = new Resources(name, file, Collections.emptyList(), Token.empty());
            current.add(resource);
        }
    }

    private void addVariables(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            variables.putIfAbsent(parent, new HashMap<>());
            Map<Resources, Set<Token>> current = variables.get(parent);

            Token name = values.get(0).getNameToken();
            File file = new File(name.getText());

            Resources resource = new Resources(name, file, Collections.emptyList(), Token.empty());

            current.putIfAbsent(resource, new HashSet<>());
            Set<Token> args = current.get(resource);
            List<Token> params = getParams(values);

            if(params.isEmpty()){
                args.add(Token.empty());
            }
            else {
                args.addAll(params);
            }
        }
    }

    private static List<Token> getParams(List<Argument> argumentList){
        if(argumentList.size() < 2){
            return Collections.emptyList();
        }

        return argumentList.subList(1, argumentList.size()).stream()
                .map(Argument::getNameToken)
                .collect(Collectors.toList());
    }
}
