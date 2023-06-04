/* Classe modificada:
 * A classe Urna eh encarregada de gerenciar o flow da votacao,
 * ela guarda as informacoes das votacoes nacionais e redireciona
 * o usuario para as votacoes estaduais atraves do Broker
 */
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import static java.lang.System.exit;

public class Urna {
  public static Election eleicao;

  public static class Builder {
    protected Election eleicao;

    public Builder election(Election election) {
      this.eleicao = election;
      return this;
    }
  }

  private static final BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

  private static boolean exit = false;

  private static final Map<String, TSEProfessional> TSEMap = new HashMap<>();

  private static final Map<String, Voter> VoterMap = new HashMap<>();

  // Hash com urnas estaduais, uma para cada estado
  public static final Map<String, UrnaEstadual> UrnasMap = new HashMap<>();

  public static Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();

  public static void print(String output) {
    System.out.println(output);
  }

  public static String readString() {
    try {
      return scanner.readLine();
    } catch (Exception e) {
      print("\nErro na leitura de entrada, digite novamente");
      return readString();
      // return "";
    }
  }

  public static int readInt() {
    try {
      return Integer.parseInt(readString());
    } catch (Exception e) {
      print("\nErro na leitura de entrada, digite novamente");
      return readInt();
      // return -1;
    }
  }

  public void startMenu() {
    try {
      while (!exit) {
        print("Escolha uma opção:\n");
        print("(1) Entrar (Eleitor)");
        print("(2) Entrar (TSE)");
        print("(0) Fechar aplicação");
        int command = readInt();
        switch (command) {
          case 1 -> voterMenu();
          case 2 -> tseMenu();
          case 0 -> exit = true;
          default -> print("Comando inválido\n");
        }
        print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
      }
    } catch (Exception e) {
      print("Erro inesperado\n");
    }
  }

  public static Voter getVoter() {
    ServerProxy proxy = new ServerProxy(VoterMap);
    Voter v = proxy.returnResult();
    return v;
  }

  private static boolean votePresident(Voter voter) {
    print("(ext) Desistir");
    print("Digite o número do candidato escolhido por você para presidente:");
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    else if (vote.equals("br")) {
      print("Você está votando branco\n");
      print("(1) Confirmar\n(2) Mudar voto");
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, eleicao, "President", true);
        return true;
      } else
        votePresident(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        if (voteNumber == 0) {
          print("Você está votando nulo\n");
          print("(1) Confirmar\n(2) Mudar voto");
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, eleicao, "President", false);
            return true;
          } else
            votePresident(voter);
        }

        President candidate = eleicao.getPresidentByNumber(voteNumber);
        if (candidate == null) {
          print("Nenhum candidato encontrado com este número, tente novamente");
          print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
          return votePresident(voter);
        }
        print(candidate.name + " do " + candidate.party + "\n");
        print("(1) Confirmar\n(2) Mudar voto");
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, eleicao, "President", false);
          return true;
        } else if (confirm == 2)
          return votePresident(voter);
      } catch (Warning e) {
        print(e.getMessage());
        return votePresident(voter);
      } catch (Error e) {
        print(e.getMessage());
        throw e;
      } catch (Exception e) {
        print("Ocorreu um erro inesperado");
        return false;
      }
    }
    return true;

  }

  // Broker redireciona usuario para a votacao estadual adequada
  public static UrnaEstadual estateElections(Voter voter) {
    BrokerServer broker = new BrokerServer(voter.state, UrnasMap);
    UrnaEstadual urnaEstadual = broker.returnResult();
    return urnaEstadual;
  }

  private static void voterMenu() {
    try {
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
      if (!eleicao.getStatus()) {
        print("A eleição ainda não foi inicializada, verifique com um funcionário do TSE");
        return;
      }

      Voter voter = getVoter();
      if (voter == null)
        return;
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      print("Vamos começar!\n");
      print(
          "OBS:\n- A partir de agora caso você queira votar nulo você deve usar um numero composto de 0 (00 e 0000)\n- A partir de agora caso você queira votar branco você deve escrever br\n");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (votePresident(voter))
        print("Voto para presidente registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      UrnaEstadual voterStateUrna = estateElections(voter);

      if (voterStateUrna.voteFederalDeputy(voter, 1))
        print("Primeiro voto para deputado federal registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (voterStateUrna.voteFederalDeputy(voter, 2))
        print("Segundo voto para deputado federal registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (voterStateUrna.voteGovernor(voter))
        print("Voto para Governador registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (voterStateUrna.voteMayor(voter))
        print("Voto para Prefeito registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (voterStateUrna.voteSenate(voter, 1))
        print("Primeiro voto para senador registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

      if (voterStateUrna.voteSenate(voter, 2))
        print("Segundo voto para senador registrado com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    } catch (Warning e) {
      print(e.getMessage());
    } catch (StopTrap e) {
      print(e.getMessage());
    } catch (Exception e) {
      print("Erro inesperado");
    }
  }

  private static TSEProfessional getTSEProfessional() {
    print("Insira seu usuário:");
    String user = readString();
    TSEProfessional tseProfessional = TSEMap.get(user);
    if (tseProfessional == null) {
      print("Funcionário do TSE não encontrado, por favor confirme se a entrada está correta e tente novamente");
    } else {
      print("Insira sua senha:");
      String password = readString();
      // Deveria ser um hash na pratica
      if (tseProfessional.password.equals(password))
        return tseProfessional;
      print("Senha inválida, tente novamente");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    }
    return null;
  }

  private static void addCandidate(TSEEmployee tseProfessional) {
    print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    print("Qual a categoria de seu candidato?\n");
    print("(1) Presidente");
    print("(2) Deputado Federal");
    print("(3) Governador");
    print("(4) Senador");
    print("(5) Prefeito");
    int candidateType = readInt();

    if (candidateType > 5 || candidateType < 1) {
      print("Comando inválido");
      addCandidate(tseProfessional);
    }

    /*Feature adicionada: ha um limite superior para o numero de candidatos
     * que podem ser adicionados a cada categoria
    */
    for (Map.Entry<String, UrnaEstadual> stateUrnas : UrnasMap.entrySet()) {
      UrnaEstadual UrnaEstado = stateUrnas.getValue();
      if (candidateType == 1 && presidentCandidates.size() >= 10) {
        print("Número de candidatos maximo para presidencia atingido");
        addCandidate(tseProfessional);
      } else if (candidateType == 2 && UrnaEstado.federalDeputyCandidates.size() >= 10) {
        print("Número de candidatos maximo para deputado federal atingido");
        addCandidate(tseProfessional);
      } else if (candidateType == 3 && UrnaEstado.governorCandidates.size() >= 10) {
        print("Número de candidatos maximo para governador atingido");
        addCandidate(tseProfessional);
      } else if (candidateType == 4 && UrnaEstado.senateCandidates.size() >= 10) {
        print("Número de candidatos maximo para senador atingido");
        addCandidate(tseProfessional);
      } else if (candidateType == 5 && UrnaEstado.mayorCandidates.size() >= 10) {
        print("Número de candidatos maximo para senador atingido");
        addCandidate(tseProfessional);
      }
    }

    print("Qual o nome do candidato?");
    String name = readString();

    print("Qual o numero do candidato?");
    int number = readInt();

    print("Qual o partido do candidato?");
    String party = readString();

    print("Qual a cidade do candidato?");
    String city = readString();

    Candidate candidate = null;
    if (candidateType == 2) {
      print("Qual o estado do candidato?");
      String state = readString();

      print("\nCadastrar o candidato deputado federal " + name + " Nº " + number + " do " + party + "(" + state + ")?");
      candidate = new FederalDeputy.Builder()
          .name(name)
          .number(123)
          .party(party)
          .state(state)
          .build();
    } else if (candidateType == 3) {
      print("Qual o estado do candidato?");
      String state = readString();

      print("\nCadastrar o candidato governador " + name + " Nº " + number + " do " + party + "(" + state + ")?");
      candidate = new Governor.Builder()
          .name(name)
          .number(123)
          .party(party)
          .state(state)
          .build();
    } else if (candidateType == 5) {
      print("Qual a cidade do candidato?");
      String cidade = readString();

      print("\nCadastrar o candidato prefeito " + name + " Nº " + number + " do " + party + "(" + city + ")?");
      candidate = new Mayor.Builder()
          .name(name)
          .number(123)
          .party(party)
          .city(cidade)
          .build();
    } else if (candidateType == 4) {
      print("Qual o estado do candidato?");
      String state = readString();

      print("\nCadastrar o candidato senador " + name + " Nº " + number + " do " + party + "(" + state + ")?");
      candidate = new Senate.Builder()
          .name(name)
          .number(123)
          .party(party)
          .state(state)
          .build();
    } else if (candidateType == 1) {
      print("\nCadastrar o candidato a presidente " + name + " Nº " + number + " do " + party + "?");
      candidate = new President.Builder()
          .name(name)
          .number(123)
          .party(party)
          .build();
    }

    print("(1) Sim\n(2) Não");
    int save = readInt();
    if (save == 1 && candidate != null) {
      print("Insira a senha da urna");
      String pwd = readString();
      tseProfessional.addCandidate(candidate, eleicao, pwd);
      print("Candidato cadastrado com sucesso");
    }
  }

  private static void removeCandidate(TSEEmployee tseProfessional) {
    print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    print("Qual a categoria de seu candidato?");
    print("(1) Presidente");
    print("(2) Deputado Federal");
    print("(3) Governador");
    print("(4) Senador");
    print("(5) Prefeito");
    int candidateType = readInt();

    if (candidateType > 4 || candidateType < 1) {
      print("Comando inválido");
      removeCandidate(tseProfessional);
    }

    Candidate candidate = null;
    print("Qual o numero do candidato?");
    int number = readInt();
    if (candidateType == 2) {
      print("Qual o estado do candidato?");
      String state = readString();
      UrnaEstadual urnaEstado = UrnasMap.get(state);
      candidate = urnaEstado.federalDeputyCandidates.get(number);
      if (candidate == null) {
        print("Candidato não encontrado");
        return;
      }
      print("/Remover o candidato a deputado federal " + candidate.name + " Nº " + candidate.number + " do "
          + candidate.party + "("
          + ((FederalDeputy) candidate).state + ")?");
    } else if (candidateType == 4) {
      print("Qual o estado do candidato?");
      String state = readString();
      UrnaEstadual urnaEstado = UrnasMap.get(state);
      candidate = urnaEstado.federalDeputyCandidates.get(number);
      if (candidate == null) {
        print("Candidato não encontrado");
        return;
      }
      print("/Remover o candidato a senador " + candidate.name + " Nº " + candidate.number + " do "
          + candidate.party + "("
          + ((Senate) candidate).state + ")?");
    } else if (candidateType == 3) {
      print("Qual o estado do candidato?");
      String state = readString();
      UrnaEstadual urnaEstado = UrnasMap.get(state);
      candidate = urnaEstado.governorCandidates.get(number);
      if (candidate == null) {
        print("Candidato não encontrado");
        return;
      }
      print("/Remover o candidato a governador " + candidate.name + " Nº " + candidate.number + " do "
          + candidate.party + "("
          + ((Governor) candidate).state + ")?");

    } else if (candidateType == 5) {
      print("Qual o estado do candidato?");
      String state = readString();
      print("Qual a cidade do candidato?");
      String city = readString();
      UrnaEstadual urnaEstado = UrnasMap.get(state);
      candidate = urnaEstado.mayorCandidates.get(city + number);
      if (candidate == null) {
        print("Candidato não encontrado");
        return;
      }
      print("/Remover o candidato a prefeito " + candidate.name + " Nº " + candidate.number + " do "
          + candidate.party + "("
          + ((Mayor) candidate).city + ")?");

    } else if (candidateType == 1) {
      candidate = presidentCandidates.get(number);
      if (candidate == null) {
        print("Candidato não encontrado");
        return;
      }
      print("/Remover o candidato a presidente " + candidate.name + " Nº " + candidate.number + " do " + candidate.party
          + "?");
    }

    print("(1) Sim\n(2) Não");
    int remove = readInt();
    if (remove == 1) {
      print("Insira a senha da urna:");
      String pwd = readString();
      tseProfessional.removeCandidate(candidate, eleicao, pwd);
      print("Candidato removido com sucesso");
    }
  }

  private static void startSession(CertifiedProfessional tseProfessional) {
    try {
      print("Insira a senha da urna");
      String pwd = readString();
      tseProfessional.startSession(eleicao, pwd);
      print("Sessão inicializada");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    } catch (Warning e) {
      print(e.getMessage());
    }
  }

  private static void endSession(CertifiedProfessional tseProfessional) {
    try {
      print("Insira a senha da urna:");
      String pwd = readString();
      tseProfessional.endSession(eleicao, pwd);
      print("Sessão finalizada com sucesso");
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    } catch (Warning e) {
      print(e.getMessage());
    }
  }

  private static void showResults(CertifiedProfessional tseProfessional) {
    try {
      print("Insira a senha da urna");
      String pwd = readString();
      print(tseProfessional.getFinalResult(eleicao, pwd));
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    } catch (Warning e) {
      print(e.getMessage());
    }
  }

  private static void tseMenu() {
    try {
      TSEProfessional tseProfessional = getTSEProfessional();
      if (tseProfessional == null)
        return;
      print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
      boolean back = false;
      while (!back) {
        print("Escolha uma opção:");
        if (tseProfessional instanceof TSEEmployee) {
          print("(1) Cadastrar candidato");
          print("(2) Remover candidato");
          print("(0) Sair");
          int command = readInt();
          switch (command) {
            case 1 -> addCandidate((TSEEmployee) tseProfessional);
            case 2 -> removeCandidate((TSEEmployee) tseProfessional);
            case 0 -> back = true;
            default -> print("Comando inválido\n");
          }
        } else if (tseProfessional instanceof CertifiedProfessional) {
          print("(1) Iniciar sessão");
          print("(2) Finalizar sessão");
          print("(3) Mostrar resultados");
          print("(0) Sair");
          int command = readInt();
          switch (command) {
            case 1 -> startSession((CertifiedProfessional) tseProfessional);
            case 2 -> endSession((CertifiedProfessional) tseProfessional);
            case 3 -> showResults((CertifiedProfessional) tseProfessional);
            case 0 -> back = true;
            default -> print("Comando inválido\n");
          }
        }
      }
    } catch (Warning e) {
      print(e.getMessage());
    } catch (Exception e) {
      print("Ocorreu um erro inesperado");
    }
  }

  public void loadVoters() {
    try {
      File myObj = new File("voterLoad.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        var voterData = data.split(",");
        VoterMap.put(voterData[0],
            new Voter.Builder().electoralCard(voterData[0]).name(voterData[1]).state(voterData[2]).build());
      }
      myReader.close();
    } catch (Exception e) {
      print("Erro na inicialização dos dados");
      exit(1);
    }
  }

  public void loadTSEProfessionals() {
    TSEMap.put("cert", new CertifiedProfessional.Builder()
        .user("cert")
        .password("54321")
        .build());
    TSEMap.put("emp", new TSEEmployee.Builder()
        .user("emp")
        .password("12345")
        .build());
  }
}
