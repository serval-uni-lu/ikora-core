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
package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.model.LogLevel;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class LogLevelType extends BaseType {

    private LogLevel value = null;
    private LogLevel defaultValue = null;

    public LogLevelType(String name) {
        super(name, false);
    }

    public LogLevelType(String name, LogLevel defaultValue){
        super(name, true);
        this.defaultValue = defaultValue;
    }

    public LogLevelType(String name, String defaultValue) throws InvalidTypeException {
        super(name, true);
        this.defaultValue = toLogLevel(defaultValue);
    }

    public Optional<LogLevel> getValue() {
        if(value == null){
            return Optional.of(defaultValue);
        }

        return Optional.of(value);
    }

    public void setValue(LogLevel value) {
        this.value = value;
    }

    public void setValue(String value) throws InvalidTypeException {
        this.value = toLogLevel(value);
    }

    private static LogLevel toLogLevel(String value) throws InvalidTypeException {
        try{
            return LogLevel.valueOf(value.strip().toUpperCase());
        }
        catch (IllegalArgumentException e){
            throw new InvalidTypeException(String.format("%s cannot be converted to a Log Level", value));
        }
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        return getValue().map(LogLevel::name);
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
            throw new InvalidTypeException(String.format("Cannot convert a List to a LogLevel: [%s]", input));
        }

        if(to != LogLevel.class && to != String.class){
            throw new InvalidTypeException(String.format("LogLevel type is used to generate boolean values but got: %s", to.getCanonicalName()));
        }

        if(from.get(0) instanceof LogLevelType logLevelType){
            final Optional<LogLevel> logLevel = logLevelType.getValue();
            if(logLevel.isEmpty()){
                throw new InvalidTypeException("Missing value");
            }
            return (T)logLevel.get();
        }

        final Optional<String> stringValue = from.get(0).asString();
        if(stringValue.isEmpty()){
            throw new InvalidTypeException("Missing value");
        }
        return (T)toLogLevel(stringValue.get());
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }
}
