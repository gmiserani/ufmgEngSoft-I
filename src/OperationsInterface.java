import java.io.BufferedReader;
import java.io.InputStreamReader;

public interface OperationsInterface {
    void print(String output);
    String readString();
    int readInt();
    public final BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
}