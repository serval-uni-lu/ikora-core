package lu.uni.serval.robotframework.compiler;

import java.io.*;

public class LineReader {
    private LineNumberReader reader;
    private Line current;
    private File file;
    private int loc;

    public LineReader(File file) throws FileNotFoundException {
        this.file = file;
        loc = 0;

        FileReader input = new FileReader(this.file);
        this.reader = new LineNumberReader(input);
    }

    public LineReader(Reader reader) {
        this.file = new File("/");
        loc = 0;

        this.reader = new LineNumberReader(reader);
    }

    public Line readLine() throws IOException {
        current = new Line(reader.readLine(), reader.getLineNumber());

        if(current.isValid() && !current.isEmpty()){
            ++loc;
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

    int getLoc() {
        return loc;
    }
}
