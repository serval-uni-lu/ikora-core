package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.builder.ValueResolver;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.types.StringType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ArgumentUtils {
    private ArgumentUtils() {}

    public static int findFirst(NodeList<Argument> arguments, Class<?> type){
        final Optional<Argument> first = arguments.stream()
                .filter(a -> a.isType(type))
                .findFirst();

        return first.map(arguments::indexOf).orElse(-1);
    }

    public static boolean isExpendedUntilPosition(NodeList<Argument> arguments, int position){
        int listIndex = findFirst(arguments, ListVariable.class);
        int dictIndex = findFirst(arguments, DictionaryVariable.class);

        int varIndex = arguments.size();
        varIndex = listIndex != -1 ? Math.min(varIndex, listIndex) : varIndex;
        varIndex = dictIndex != -1 ? Math.min(varIndex, dictIndex) : varIndex;

        return position < varIndex;
    }

    public static boolean contains(NodeList<Argument> arguments, Value value){
        return arguments.stream().map(Argument::getDefinition).anyMatch(v -> v == value);
    }

    public static Class<? extends BaseType> getArgumentType(Argument argument){
        SourceNode parent = argument.getAstParent(true);

        if(parent == null || parent.getClass() != KeywordCall.class){
            return StringType.class;
        }

        final Optional<Keyword> keyword = ((KeywordCall)parent).getKeyword();

        if(!keyword.isPresent()){
            return StringType.class;
        }

        final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();
        int index = ((KeywordCall) parent).getArgumentList().indexOf(argument);

        if(index < 0 || index >= argumentTypes.size()){
            return StringType.class;
        }

        return argumentTypes.get(index).getClass();
    }

    public static List<Pair<String, SourceNode>> getArgumentValues(Argument argument){
        return getArgumentValues(argument, new HashSet<>());
    }

    private static List<Pair<String, SourceNode>> getArgumentValues(Argument argument, Set<Variable> memory){
        List<Pair<String, SourceNode>> values = new ArrayList<>();

        for(Node node: ValueResolver.getValueNodes(argument)){
            if(node instanceof Literal){
                values.add(Pair.of(node.getName(), (SourceNode) node));
            }
            else if(node instanceof Argument){
                values.add(Pair.of(node.getName(), ((Argument)node).getDefinition()));
            }
            else if (node instanceof LibraryVariable){
                values.add(Pair.of(node.getName(), argument));
            }
            else if(node instanceof VariableAssignment){
                values.addAll(getAssignmentValue((VariableAssignment)node));
            }
            else if (node instanceof Variable){
                values.addAll(getParameterValueFromCalls((Variable)node, memory));
            }
        }

        return values;
    }

    private static List<Pair<String, SourceNode>> getAssignmentValue(final VariableAssignment assignment){
        final List<List<String>> values = new ArrayList<>();

        for(Argument argument: assignment.getValues()){
            values.add(getArgumentValues(argument).stream().map(Pair::getLeft).collect(Collectors.toList()));
        }

        return Permutations.permutations(values).stream()
                .map(v -> Pair.of(String.join("\t", v), (SourceNode)assignment))
                .collect(Collectors.toList());
    }

    private static List<Pair<String, SourceNode>> getParameterValueFromCalls(final Variable variable, Set<Variable> memory){
        if(!memory.add(variable)){
            return Collections.emptyList();
        }

        final Optional<UserKeyword> userKeyword = ValueResolver.getUserKeywordFromArgument(variable);

        if(!userKeyword.isPresent()){
            return Collections.emptyList();
        }

        final List<Pair<String, SourceNode>> values = new ArrayList<>();
        final int position = userKeyword.get().getParameters().indexOf(variable);

        for(SourceNode node: userKeyword.get().getDependencies()){
            if(node instanceof KeywordCall){
                final NodeList<Argument> argumentList = ((KeywordCall)node).getArgumentList();

                if(ArgumentUtils.isExpendedUntilPosition(argumentList, position)){
                    final Argument argument = argumentList.get(position);
                    values.addAll(getArgumentValues(argument, memory));
                }
            }
        }

        return values;
    }
}
