import java.util.List;

class FileContent {
    private List<Line> lines;

    List<Line> getLines() {
        return lines;
    }

    FileContent(List<Line> lines) {
        this.lines = lines;
    }

    void appendLine(Line line) {
        lines.add(line);
    }
}
