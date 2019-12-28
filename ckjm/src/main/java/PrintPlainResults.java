import java.io.PrintStream;

public class PrintPlainResults implements CkjmOutputHandler {
    private PrintStream p;

    public PrintPlainResults (PrintStream p) {
        this.p = p;
    }

    public void handleClass(String name, ClassMetrics c) {
        p.println(name + " " + c.toString());
    }
}
