import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import static java.lang.System.exit;

public interface OperationsInterface {
    void print(String output);
    String readString();
    int readInt();
    public final BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
}