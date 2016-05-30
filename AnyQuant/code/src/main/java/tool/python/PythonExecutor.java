package tool.python;



import java.io.IOException;

/**
 * Created by kylin on 16/3/20.
 */
public class PythonExecutor {

    public void runPython(String path) throws InterruptedException, IOException {
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.execfile(path);
        Process proc = Runtime.getRuntime().exec(path);
        proc.waitFor();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        PythonExecutor pythonExecutor = new PythonExecutor();
        String pythonFilePath = "src/main/python/RefreshLatest.py";
        pythonExecutor.runPython(pythonFilePath);
    }
}
