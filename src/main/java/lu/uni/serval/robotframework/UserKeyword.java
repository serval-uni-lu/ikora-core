package lu.uni.serval.robotframework;

import java.util.List;

public class UserKeyword extends TestCase {
    private List<String> arguments;

    public UserKeyword(String name, List<String> arguments, String documentation, List<Step> steps) {
        super(name, documentation, steps);

        this.arguments = arguments;
    }
}
