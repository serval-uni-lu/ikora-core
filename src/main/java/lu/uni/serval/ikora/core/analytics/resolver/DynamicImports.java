/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics.resolver;

import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportResource;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportLibrary;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.ImportVariables;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.KeywordType;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class DynamicImports {
    private final Map<KeywordDefinition, ResourcesTable> resources = new HashMap<>();
    private final Map<KeywordDefinition, Set<Library>> libraries = new HashMap<>();
    private final Map<KeywordDefinition, Set<VariableFile>> variables = new HashMap<>();


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

            Token name = argumentList.get(0).getDefinitionToken();

            Library library = new Library(Token.empty(), name, getImportArguments(argumentList));
            current.add(library);
        }
    }

    private void addResources(KeywordDefinition parent, List<Argument> argumentList){
        if(!argumentList.isEmpty()){
            resources.putIfAbsent(parent, new ResourcesTable());
            ResourcesTable current = resources.get(parent);

            Token name = argumentList.get(0).getDefinitionToken();

            Resources resource = new Resources(Token.empty(), name, getImportArguments(argumentList));
            current.add(resource);
        }
    }

    private void addVariables(KeywordDefinition parent, List<Argument> argumentList){
        if(!argumentList.isEmpty()){
            variables.putIfAbsent(parent, new HashSet<>());
            Set<VariableFile> current = variables.get(parent);

            Token name = argumentList.get(0).getDefinitionToken();

            VariableFile variableFile = new VariableFile(Token.empty(), name, getImportArguments(argumentList));
            current.add(variableFile);
        }
    }

    public static NodeList<Value> getImportArguments(List<Argument> argumentList){
        final NodeList<Value> values = new NodeList<>();

        for(Argument argument: argumentList.subList(1, argumentList.size())){
            if(!argument.isType(KeywordType.class)){
                values.add((Value) argument.getDefinition());
            }
        }

        return values;
    }
}
