package lu.uni.serval.utils;

import lu.uni.serval.utils.exception.DuplicateNodeException;

public interface CommandRunner {
    void run() throws DuplicateNodeException;
}
