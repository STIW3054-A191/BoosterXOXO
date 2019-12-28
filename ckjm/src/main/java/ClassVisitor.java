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
        classResult result;
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
    @Override
    public void endVisit(TypeDeclaration node) {

        // let's first visit any metrics that might make use of this endVisit
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.endVisit(node));

        ClassInTheStack completedClass = classes.pop();

        // persist the results of the class level metrics in the result
        completedClass.classLevelMetrics.forEach(m -> m.setResult(completedClass.result));

        // we are done processing this class, so now let's
        // store it in the collected classes set
        collectedClasses.add(completedClass.result);
    }

    public boolean visit(MethodDeclaration node) {

        IMethodBinding binding = node.resolveBinding();

        String currentMethodName = binding!=null ? JDTUtils.getMethodFullName(binding) : JDTUtils.getMethodFullName(node);
        boolean isConstructor = node.isConstructor();

        CKMethodResult currentMethod = new CKMethodResult(currentMethodName, isConstructor, node.getModifiers());
        currentMethod.setLoc(calculate(IOUtils.toInputStream(node.toString())));
        currentMethod.setStartLine(JDTUtils.getStartLine(cu, node));

        // let's instantiate method level visitors for this current method
        List<MethodLevelMetric> methodLevelMetrics = instantiateMethodLevelMetricVisitors();

        // we add it to the current class we are visiting
        MethodInTheStack methodInTheStack = new MethodInTheStack();
        methodInTheStack.result = currentMethod;
        methodInTheStack.methodLevelMetrics = methodLevelMetrics;
        classes.peek().methods.push(methodInTheStack);

        // and there might be metrics that also use the methoddeclaration node.
        // so, let's call them
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));
        if(!classes.peek().methods.isEmpty())
            classes.peek().methods.peek().methodLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));

        return true;
    }

    @Override
    public void endVisit(MethodDeclaration node) {

        // let's first invoke the metrics, because they might use this node
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.endVisit(node));
        classes.peek().methods.peek().methodLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.endVisit(node));

        // remove the method from the stack
        MethodInTheStack completedMethod = classes.peek().methods.pop();

        // persist the data of the visitors in the CKMethodResult
        completedMethod.methodLevelMetrics.forEach(m -> m.setResult(completedMethod.result));

        // store its final version in the current class
        classes.peek().result.addMethod(completedMethod.result);
    }


    public boolean visit(AnonymousClassDeclaration node) {
        // there might be metrics that use it
        // (even before an anonymous class is created)
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));
        if(!classes.peek().methods.isEmpty())
            classes.peek().methods.peek().methodLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));

        // we give the anonymous class a 'class$AnonymousN' name
        String anonClassName = classes.peek().result.getClassName() + "$Anonymous" + ++anonymousNumber;
        classResult currentClass = new classResult(sourceFilePath, anonClassName, "anonymous", -1);
        currentClass.setLoc((int) JDTUtils.countLoc(node));

        // create a set of visitors, just for the current class
        List<ClassLevelMetric> classLevelMetrics = instantiateClassLevelMetricVisitors();

        // store everything in a 'class in the stack' data structure
        ClassInTheStack classInTheStack = new ClassInTheStack();
        classInTheStack.result = currentClass;
        classInTheStack.classLevelMetrics = classLevelMetrics;

        // push it to the stack, so we know the current class we are visiting
        classes.push(classInTheStack);

        // and there might be metrics that also use the methoddeclaration node.
        // so, let's call them
        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));
        if(!classes.peek().methods.isEmpty())
            classes.peek().methods.peek().methodLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.visit(node));

        return true;
    }

    public void endVisit(AnonymousClassDeclaration node) {

        classes.peek().classLevelMetrics.stream().map(metric -> (ASTVisitor) metric).forEach(ast -> ast.endVisit(node));

        ClassInTheStack completedClass = classes.pop();

        // persist the results of the class level metrics in the result
        completedClass.classLevelMetrics.forEach(m -> m.setResult(completedClass.result));

        // we are done processing this class, so now let's
        // store it in the collected classes set
        collectedClasses.add(completedClass.result);
    }

}
