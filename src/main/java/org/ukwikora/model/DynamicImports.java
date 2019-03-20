package org.ukwikora.model;

import org.ukwikora.libraries.builtin.ImportLibrary;
import org.ukwikora.libraries.builtin.ImportResource;
import org.ukwikora.libraries.builtin.ImportVariables;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DynamicImports {
    private ResourcesTable resources;
    private Set<Library> libraries;
    private Map<Resources, Set<String>> variables;

    public DynamicImports(){
        resources = new ResourcesTable();
        libraries = new HashSet<>();
        variables = new HashMap<>();
    }

    public Statement findStatement(String value){
        throw new NotImplementedException();
    }


    public void add(Step step){
        step.getKeywordCall().ifPresent(call -> {
            if(!call.getClass().isAssignableFrom(LibraryKeyword.class)){
                return;
            }

            if(call.getClass().isAssignableFrom(ImportLibrary.class)){
                addLibrary(call.getParameters());
            }
            else if(call.getClass().isAssignableFrom(ImportResource.class)){
                addResources(call.getParameters());
            }
            else if(call.getClass().isAssignableFrom(ImportVariables.class)){
                addVariables(call.getParameters());
            }
        });
    }

    private void addLibrary(List<Value> values){
        if(!values.isEmpty()){
            Library library = new Library(values.get(0).getName(), getParams(values), "");
            libraries.add(library);
        }
    }

    private void addResources(List<Value> values){
        if(!values.isEmpty()){
            String name = values.get(0).getName();
            File file = new File(name);

            Resources resource = new Resources(name, file, Collections.emptyList(), "");
            resources.add(resource);
        }
    }

    private void addVariables(List<Value> values){
        if(!values.isEmpty()){
            String name = values.get(0).getName();
            File file = new File(name);

            Resources resource = new Resources(name, file, Collections.emptyList(), "");

            variables.putIfAbsent(resource, new HashSet<>());
            Set<String> args = variables.get(resource);
            List<String> params = getParams(values);

            if(params.isEmpty()){
                args.add("");
            }
            else {
                args.addAll(params);
            }
        }
    }

    private static List<String> getParams(List<Value> values){
        if(values.size() < 2){
            return Collections.emptyList();
        }

        return values.subList(1, values.size()).stream().map(Value::getName).collect(Collectors.toList());
    }
}
