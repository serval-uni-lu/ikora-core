package lu.uni.serval.robotframework.compiler;

import java.io.*;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;

    public LineReader(File file) throws FileNotFoundException {
        this.file = file;

        FileReader input = new FileReader(this.file);
        this.reader = new LineNumberReader(input);
    }

    public Line readLine() throws IOException {
        current = new Line(reader.readLine(), reader.getLineNumber());
        return current;
    }

    public Line getCurrent(){
        return this.current;
    }

    public File getFile() {
        return file;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
