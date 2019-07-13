class Line {
    private final String line;
    private final int lineNumber;

    Line(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    String getLine() {
        return line;
    }

    int getLineNumber() {
        return lineNumber;
    }
}
