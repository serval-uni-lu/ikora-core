package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.TestCaseFile;

import java.io.*;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;
    private TestCaseFile testCaseFile;
    private int loc;

    public LineReader(Reader reader) {
        this.file = new File("/");
        loc = 0;

        this.reader = new LineNumberReader(reader);
        this.testCaseFile = null;
    }

    public LineReader(TestCaseFile testCaseFile) throws FileNotFoundException {
        this.file = testCaseFile.getFile();
        loc = 0;

        FileReader input = new FileReader(this.file);
        this.reader = new LineNumberReader(input);

        this.testCaseFile = testCaseFile;
    }

    public Line readLine() throws IOException {
        current = new Line(reader.readLine(), reader.getLineNumber());

        if(testCaseFile != null){
            testCaseFile.addLine(current);
        }

        if(current.isValid() && !current.isEmpty()){
            ++loc;
        }

        if(testCaseFile != null && !current.isValid()){
            testCaseFile.setLoc(loc);
        }

        return current;
    }

    Line getCurrent(){
        return this.current;
    }

    public File getFile() {
        return file;
    }

    void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
