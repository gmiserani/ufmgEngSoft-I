/* Classe adicionada
 * Usada para a implementacao do padrao de projeto Proxy
 */
public class Server implements OperationsInterface {

    public void print(String output) {
        System.out.println(output);
    }

    public String readString() {
        try {
            return scanner.readLine();
        } catch (Exception e) {
            print("\nErro na leitura de entrada, digite novamente");
            return readString();
        }
    }

    public int readInt() {
        try {
            return Integer.parseInt(readString());
        } catch (Exception e) {
            print("\nErro na leitura de entrada, digite novamente");
            return readInt();
        }
    }
}