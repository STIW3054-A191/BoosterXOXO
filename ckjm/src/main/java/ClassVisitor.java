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
    @Override
    public boolean visit(TypeDeclaration node) {
        ITypeBinding binding = node.resolveBinding();

        // there might be metrics that use it
        // (even before a class is declared)
        if(!classes.isEmpty()) {
            classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));
            if (!classes.peek().methods.isEmpty())
                classes.peek().methods.peek().methodLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));
        }

        // build a classResult based on the current type
        // declaration we are visiting
        String className = binding != null ? binding.getBinaryName() : node.getName().getFullyQualifiedName();
        String type = getTypeOfTheUnit(node);
        int modifiers = node.getModifiers();
        classResult currentClass = new classResult(sourceFilePath, className, type, modifiers);
        currentClass.setLoc((int) JDTUtils.countLoc(node));

        // create a set of visitors, just for the current class
        List<ClassLevelMetric> classLevelMetrics = instantiateClassLevelMetricVisitors();

        // store everything in a 'class in the stack' data structure
        ClassInTheStack classInTheStack = new ClassInTheStack();
        classInTheStack.result = currentClass;
        classInTheStack.classLevelMetrics = classLevelMetrics;

        // push it to the stack, so we know the current class we are visiting
        classes.push(classInTheStack);

        // there might be class level metrics that use the TypeDeclaration
        // so, let's run them
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));

        return true;
    }

}
