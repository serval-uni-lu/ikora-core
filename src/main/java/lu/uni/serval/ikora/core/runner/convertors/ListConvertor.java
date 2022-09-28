package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.parser.ValueParser;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.ListType;

public class ListConvertor extends Convertor{
    public ListConvertor(){
        super(ListType.class);
    }

    @Override
    <T> T convert(Resolved value, Class<T> type) throws RunnerException {
        if(type.equals(Value.class)){
            return (T)ValueParser.parseValue(Token.fromString(value.getValue()));
        }

        throw new InternalException("Cannot convert " + type.getName() + " using type convertor.");
    }
}
