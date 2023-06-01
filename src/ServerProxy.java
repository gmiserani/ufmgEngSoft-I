import java.util.HashMap;
import java.util.Map;

public class ServerProxy {
    Voter resultVoterServer;

    private static final Map<String, Voter> VoterMap = new HashMap<>();

    public static Election currentElection;
	
	public ServerProxy(CallMessage msgServer) {
		
		Server s = new Server();
        int achou = 0;
        while(achou == 0){
            s.print("Insira seu título de eleitor:");
            String electoralCard = s.readString();
            Voter voter = VoterMap.get(electoralCard);
            if (voter == null) {
                s.print("Eleitor não encontrado, por favor confirme se a entrada está correta e tente novamente");
            } else {
                s.print("Olá, você é " + voter.name + " de " + voter.state + "?\n");
                s.print("(1) Sim\n(2) Não");
                int command = s.readInt();
                if (command == 1){
                    resultVoterServer = voter;
                    achou = 1;
                }
                else if (command == 2)
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