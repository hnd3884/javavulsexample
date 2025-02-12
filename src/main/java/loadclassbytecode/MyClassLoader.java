package loadclassbytecode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class MyClassLoader extends ClassLoader {
    private static String myClassName = "loadclassbytecode.calc";

    // Use javac to compile loadclassbytecode.calc class
    // Path to your compiled class file
    String classFilePath = "D:\\learn\\java\\loadclassfrombycode\\src\\loadclassbytecode.calc.class";

    // Read the class file as bytes
    byte[] bs = Files.readAllBytes(Paths.get(classFilePath));

    public MyClassLoader() throws IOException {
    }

    public static void main(String[] args) {
        try {
            MyClassLoader loader = new MyClassLoader();
            Class helloClass = loader.loadClass(myClassName);
            Object obj = helloClass.newInstance();
            Method method = obj.getClass().getMethod("test");
            method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (Objects.equals(name, myClassName)) {
            return defineClass(myClassName, bs, 0, bs.length);
        }
        return super.findClass(name);
    }

}
