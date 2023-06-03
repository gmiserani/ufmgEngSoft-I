import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class UrnaEstadual {

    public Map<Integer, FederalDeputy> federalDeputyCandidates = new HashMap<Integer, FederalDeputy>();
  
    public Map<Integer, Governor> governorCandidates = new HashMap<Integer, Governor>();

    public Map<String, Mayor> mayorCandidates = new HashMap<String, Mayor>();

    public Map<Integer, Senate> senateCandidates = new HashMap<Integer, Senate>();

    public UrnaEstadual(){

    }

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

  public boolean voteGovernor(Voter voter) {
    print("(ext) Desistir");
    print("Digite o número do candidato escolhido por você para governador:");
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    else if (vote.equals("br")) {
      print("Você está votando branco\n");
      print("(1) Confirmar\n(2) Mudar voto");
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, currentElection, "Governor", true);
        return true;
      } else
        voteGovernor(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          print("Você está votando nulo\n");
          print("(1) Confirmar\n(2) Mudar voto");
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, currentElection, "Governor", false);
            return true;
          } else
            voteGovernor(voter);
        }

        // Normal
        Governor candidate = currentElection.getGovernorByNumber(voter.state, voteNumber);
        if (candidate == null) { 
          print("Nenhum candidato encontrado com este número, tente novamente");
          print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
          return voteGovernor(voter);
        }
        print(candidate.name + " do " + candidate.party + "\n");
        print("(1) Confirmar\n(2) Mudar voto");
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, currentElection, "Governor", false);
          return true;
        } else if (confirm == 2)
          return voteGovernor(voter);
      } catch (Warning e) {
        print(e.getMessage());
        return voteGovernor(voter);
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

  public boolean voteMayor(Voter voter) {
    print("(ext) Desistir");
    print("Digite o número do candidato escolhido por você para prefeito:");
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    else if (vote.equals("br")) {
      print("Você está votando branco\n");
      print("(1) Confirmar\n(2) Mudar voto");
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, currentElection, "Mayor", true);
        return true;
      } else
        voteMayor(voter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          print("Você está votando nulo\n");
          print("(1) Confirmar\n(2) Mudar voto");
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, currentElection, "Mayor", false);
            return true;
          } else
            voteMayor(voter);
        }

        // Normal
        Mayor candidate = currentElection.getMayorByNumber(voter.city, voteNumber);
        if (candidate == null) { 
          print("Nenhum candidato encontrado com este número, tente novamente");
          print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
          return voteMayor(voter);
        }
        print(candidate.name + " do " + candidate.party + "\n");
        print("(1) Confirmar\n(2) Mudar voto");
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, currentElection, "Mayor", false);
          return true;
        } else if (confirm == 2)
          return voteMayor(voter);
      } catch (Warning e) {
        print(e.getMessage());
        return voteMayor(voter);
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

  public boolean voteFederalDeputy(Voter voter, int counter) {
    print("(ext) Desistir");
    print("Digite o número do " + counter + "º candidato escolhido por você para deputado federal:\n");
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    if (vote.equals("br")) {
      print("Você está votando branco\n");
      print("(1) Confirmar\n(2) Mudar voto");
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, currentElection, "FederalDeputy", true);
        return true;
      } else
        return voteFederalDeputy(voter, counter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          print("Você está votando nulo\n");
          print("(1) Confirmar\n(2) Mudar voto\n");
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, currentElection, "FederalDeputy", false);
            return true;
          } else
            return voteFederalDeputy(voter, counter);
        }

        // Normal
        FederalDeputy candidate = currentElection.getFederalDeputyByNumber(voter.state, voteNumber);
        if (candidate == null) {
          print("Nenhum candidato encontrado com este número, tente novamente");
          print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
          return voteFederalDeputy(voter, counter);
        }
        print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        print("(1) Confirmar\n(2) Mudar voto");
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, currentElection, "FederalDeputy", false);
          return true;
        } else if (confirm == 2)
          return voteFederalDeputy(voter, counter);
      } catch (Warning e) {
        print(e.getMessage());
        return voteFederalDeputy(voter, counter);
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

  public boolean voteSenate(Voter voter, int counter) {
    print("(ext) Desistir");
    print("Digite o número do " + counter + "º candidato escolhido por você para senador:\n");
    String vote = readString();
    if (vote.equals("ext"))
      throw new StopTrap("Saindo da votação");
    // Branco
    if (vote.equals("br")) {
      print("Você está votando branco\n");
      print("(1) Confirmar\n(2) Mudar voto");
      int confirm = readInt();
      if (confirm == 1) {
        voter.vote(0, currentElection, "Senate", true);
        return true;
      } else
        return voteSenate(voter, counter);
    } else {
      try {
        int voteNumber = Integer.parseInt(vote);
        // Nulo
        if (voteNumber == 0) {
          print("Você está votando nulo\n");
          print("(1) Confirmar\n(2) Mudar voto\n");
          int confirm = readInt();
          if (confirm == 1) {
            voter.vote(0, currentElection, "Senate", false);
            return true;
          } else
            return voteSenate(voter, counter);
        }

        // Normal
        Senate candidate = currentElection.getSenateByNumber(voter.state, voteNumber);
        if (candidate == null) {
          print("Nenhum candidato encontrado com este número, tente novamente");
          print("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
          return voteSenate(voter, counter);
        }
        print(candidate.name + " do " + candidate.party + "(" + candidate.state + ")\n");
        print("(1) Confirmar\n(2) Mudar voto");
        int confirm = readInt();
        if (confirm == 1) {
          voter.vote(voteNumber, currentElection, "Senate", false);
          return true;
        } else if (confirm == 2)
          return voteSenate(voter, counter);
      } catch (Warning e) {
        print(e.getMessage());
        return voteSenate(voter, counter);
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
}
