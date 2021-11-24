package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportResource;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportLibrary;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportVariables;
import lu.uni.serval.ikora.core.model.*;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class DynamicImports {
    private final Map<KeywordDefinition, ResourcesTable> resources = new HashMap<>();
    private final Map<KeywordDefinition, Set<Library>> libraries = new HashMap<>();
    private final Map<KeywordDefinition, Map<Resources, Set<Token>>> variables = new HashMap<>();


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

            Library library = new Library(Token.empty(), argumentList.get(0).getDefinitionToken(), getParams(argumentList));
            current.add(library);
        }
    }

    private void addResources(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            resources.putIfAbsent(parent, new ResourcesTable());
            ResourcesTable current = resources.get(parent);

            Token name = values.get(0).getDefinitionToken();

            Resources resource = new Resources(Token.empty(), name, Collections.emptyList());
            current.add(resource);
        }
    }

    private void addVariables(KeywordDefinition parent, List<Argument> values){
        if(!values.isEmpty()){
            variables.putIfAbsent(parent, new HashMap<>());
            Map<Resources, Set<Token>> current = variables.get(parent);
            Token name = values.get(0).getDefinitionToken();

            Resources resource = new Resources(Token.empty(), name, Collections.emptyList());

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
                .map(Argument::getDefinitionToken)
                .collect(Collectors.toList());
    }
}
