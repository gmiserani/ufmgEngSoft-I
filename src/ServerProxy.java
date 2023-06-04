/* Classe adicionada
 * Classe referente ao padrao de projeto Proxy que garante a
 * integridade da urna, verificando e validando o titulo de
 * eleitor do usuario antes de permitir a votacao
*/
import java.util.Map;

public class ServerProxy {

    Voter resultVoterServer;

    public ServerProxy(Map<String, Voter> VoterMap) {

        Server s = new Server();
        int isValidElector = 0;
        while (isValidElector == 0) {
            s.print("Insira seu título de eleitor:");
            String electoralCard = s.readString();
            Voter voter = VoterMap.get(electoralCard);
            if (voter == null) {
                s.print("Eleitor não encontrado, por favor confirme se a entrada está correta e tente novamente");
            } else {
                s.print("Insira sua cidade:");
                String city = s.readString();
                s.print("Olá, você é " + voter.name + " de " + voter.state + "," + city + "?\n");
                s.print("(1) Sim\n(2) Não");
                int command = s.readInt();
                if (command == 1) {
                    resultVoterServer = voter;
                    isValidElector = 1;

                } else if (command == 2)
                    s.print("Ok, você será redirecionado para o menu inicial");
                else {
                    s.print("Entrada inválida, tente novamente");
                }
            }
        }
    }

    public Voter returnResult() {
        return resultVoterServer;
    }

}