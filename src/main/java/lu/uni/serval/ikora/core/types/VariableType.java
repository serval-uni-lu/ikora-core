package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Variable;
import lu.uni.serval.ikora.core.parser.VariableParser;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class VariableType extends BaseType{
    public VariableType(String name) {
        super(name, false);
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        return Optional.of(getName());
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        if(from.size() != 1){
            final String input = String.join(",", from.stream()
                    .map(BaseType::asString)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList()
            );
            throw new InvalidTypeException(String.format("Cannot convert a List to a Variable name: [%s]", input));
        }

        Optional<String> name = from.get(0).asString();

        if(name.isEmpty()){
            throw new InvalidTypeException("Should be a variable but got an empty value instead");
        }

        if(to == Variable.class){
            final Optional<Variable> parse = VariableParser.parse(Token.fromString(name.get()));

            if(parse.isEmpty()){
                throw new InvalidTypeException("Should be a variable but got " + name.get() + " instead");
            }

            return (T)parse.get();
        }

        if(to == String.class){
            return (T)name.get();
        }

        throw new InvalidTypeException(
                String.format("Variable type is used to generate variable names but got: %s", to.getCanonicalName())
        );
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }
}
