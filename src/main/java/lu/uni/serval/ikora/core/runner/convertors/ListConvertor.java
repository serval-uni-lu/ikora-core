package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.parser.ValueParser;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.ListType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListConvertor extends Convertor{
    public ListConvertor(){
        super(ListType.class);
    }

    @Override
    <T> T convert(List<Resolved> resolvedList, Class<T> type) throws InternalException {
        throw new InternalException("Cannot convert " + type.getName() + " using list convertor.");
    }

    @Override
    <C extends Collection<T>, T> C convert(List<Resolved> resolvedList, Class<T> type, Class<C> container) throws RunnerException {
        if(List.class.isAssignableFrom(container)){
            final List<T> ts = new ArrayList<>();

            if(type == String.class){
                for(Resolved resolved: resolvedList){
                    ts.add((T)resolved.getValue());
                }
            }
            else if(type == Value.class){
                for(Resolved resolved: resolvedList){
                    ts.add((T) ValueParser.parseValue(Token.fromString(resolved.getValue())));
                }
            }

            return (C)ts;
        }

        throw new InternalException("Cannot convert " + container.getName() + " using list convertor.");
    }
}
