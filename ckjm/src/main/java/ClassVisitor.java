import java.util.Set;
import java.util.Stack;


public class ClassVisitor extends ASTVisitor {
    private String sourceFilePath;
    private int anonymousNumber;

    class MethodInTheStack {
        methodResult result;
        List<MethodLevelMetric> methodLevelMetrics;
    }

    class ClassInTheStack {
        CKClassResult result;
        List<ClassLevelMetric> classLevelMetrics;
        Stack<MethodInTheStack> methods;


        ClassInTheStack() {
            methods = new Stack<>();
        }
    }
    private Stack<ClassInTheStack> classes;

    private Set<classResult> collectedClasses;

    private CompilationUnit cu;
    private Callable<List<ClassLevelMetric>> classLevelMetrics;
    private Callable<List<MethodLevelMetric>> methodLevelMetrics;

    public ClassVisitor(String sourceFilePath, CompilationUnit cu, Callable<List<ClassLevelMetric>> classLevelMetrics, Callable<List<MethodLevelMetric>> methodLevelMetrics) {
        this.sourceFilePath = sourceFilePath;
        this.cu = cu;
        this.classLevelMetrics = classLevelMetrics;
        this.methodLevelMetrics = methodLevelMetrics;
        this.classes = new Stack<>();
        this.collectedClasses = new HashSet<>();
    }
}
