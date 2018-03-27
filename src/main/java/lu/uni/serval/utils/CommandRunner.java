package lu.uni.serval.utils;

import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;

public interface CommandRunner {
    void run(CommandLine cmd) throws MissingArgumentException, DuplicateNodeException;
    void setCmdOptions(Options options);
}
