package lu.uni.serval.ikora.libraries.builtin.keywords;

import lu.uni.serval.ikora.model.LibraryKeyword;
import lu.uni.serval.ikora.runner.Runtime;
import lu.uni.serval.ikora.types.StringType;

public class GetTime extends LibraryKeyword {
    public GetTime(){
        super(Type.GET,
                new StringType("format", "timestamp"),
                new StringType("time_", "NOW")
        );
    }

    @Override
    public void run(Runtime runtime) {
        throw new UnsupportedOperationException();
    }
}
