package lu.uni.serval.utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;

public interface CommandRunner {
    void run(CommandLine cmd) throws MissingArgumentException;
    void setCmdOptions(Options options);
}
