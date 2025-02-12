package loadclassbytecode;

import java.io.IOException;

public class calc {
    static {
        try {
            Runtime.getRuntime().exec("calc.exe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
