package lu.uni.serval.utils;

import lu.uni.serval.utils.exception.DuplicateNodeException;
import org.apache.commons.cli.Options;

public interface CommandRunner {
    void run() throws DuplicateNodeException;
    void setCmdOptions(Options options);
}
