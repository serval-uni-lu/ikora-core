package org.ikora.builder;

import org.ikora.libraries.builtin.ImportLibrary;
import org.ikora.libraries.builtin.ImportResource;
import org.ikora.libraries.builtin.ImportVariables;
import org.ikora.model.*;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DynamicImports {
    private Map<KeywordDefinition, ResourcesTable> resources;
    private Map<KeywordDefinition, Set<Library>> libraries;
    private Map<KeywordDefinition, Map<Resources, Set<String>>> variables;

    public DynamicImports(){
        resources = new HashMap<>();
        libraries = new HashMap<>();
        variables = new HashMap<>();
    }

    public Node findNode(String value){
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

            Library library = new Library(argumentList.get(0).getName(), getParams(argumentList), "");
            current.add(library);
        }
    }

    private void addResources(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            resources.putIfAbsent(parent, new ResourcesTable());
            ResourcesTable current = resources.get(parent);

            String name = values.get(0).getName();
            File file = new File(name);

            Resources resource = new Resources(name, file, Collections.emptyList(), "");
            current.add(resource);
        }
    }

    private void addVariables(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            variables.putIfAbsent(parent, new HashMap<>());
            Map<Resources, Set<String>> current = variables.get(parent);

            String name = values.get(0).getName();
            File file = new File(name);

            Resources resource = new Resources(name, file, Collections.emptyList(), "");

            current.putIfAbsent(resource, new HashSet<>());
            Set<String> args = current.get(resource);
            List<String> params = getParams(values);

            if(params.isEmpty()){
                args.add("");
            }
            else {
                args.addAll(params);
            }
        }
    }

    private static List<String> getParams(List<Argument> argumentList){
        if(argumentList.size() < 2){
            return Collections.emptyList();
        }

        return argumentList.subList(1, argumentList.size()).stream()
                .map(Argument::getName)
                .collect(Collectors.toList());
    }
}
