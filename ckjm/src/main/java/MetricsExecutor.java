import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class MetricsExecutor extends FileASTRequestor {

    private Callable<List<ClassLevelMetric>> classLevelMetrics;
    private Callable<List<MethodLevelMetric>> methodLevelMetrics;
    private CKNotifier notifier;

    private static Logger log = Logger.getLogger(MetricsExecutor.class);

    public MetricsExecutor(Callable<List<ClassLevelMetric>> classLevelMetrics, Callable<List<MethodLevelMetric>> methodLevelMetrics, CKNotifier notifier) {
        this.classLevelMetrics = classLevelMetrics;
        this.methodLevelMetrics = methodLevelMetrics;
        this.notifier = notifier;
    }


    public void acceptAST(String sourceFilePath,
                          CompilationUnit cu) {

        try {
            CKVisitor visitor = new CKVisitor(sourceFilePath, cu, classLevelMetrics, methodLevelMetrics);

            cu.accept(visitor);
            Set<classResult> collectedClasses = visitor.getCollectedClasses();

            for (classResult collectedClass : collectedClasses) {
                log.info(collectedClass);
                notifier.notify(collectedClass);
            }
        } catch(Exception e) {
            log.error("error in " + sourceFilePath, e);
        }
    }

}