import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import static java.lang.System.exit;

public class Server implements OperationsInterface{

    public void print(String output) {
        System.out.println(output);
    }
    public String readString() {
        try {
        return scanner.readLine();
        } catch (Exception e) {
        print("\nErro na leitura de entrada, digite novamente");
        return readString();
        // return "";
        }
    }

    public int readInt() {
        try {
        return Integer.parseInt(readString());
        } catch (Exception e) {
        print("\nErro na leitura de entrada, digite novamente");
        return readInt();
        // return -1;
        }
    }
}