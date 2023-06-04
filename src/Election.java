/* Classe reutilizada
 * Classe utilizada para guardar as informacoes referentes a todas aos
 * votos de todas as categorias de candidatos, por exemplo, adicionar e
 * remover candidatos, computar quantos votos cada um obtem, alem dos
 * votos brancos e nulos
 * A classe foi extendida para suportar todas as funçoes ja existentes
 * tambem para governador, prefeito e senador
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class Election {
  public static void print(String output) {
    System.out.println(output);
  }

  private final String password;

  public Urna urna;

  private boolean status;

  private int nullPresidentVotes;

  private int nullFederalDeputyVotes;

  private int nullGovernorVotes;

  private int nullMayorVotes;

  private int nullSenateVotes;

  private int presidentProtestVotes;

  private int federalDeputyProtestVotes;

  private int governorProtestVotes;

  private int mayorProtestVotes;

  private int senateProtestVotes;

  private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersGovernor = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersMayor = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersSenate = new HashMap<Voter, Integer>();

  private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();

  private Map<Voter, Senate> tempSVote = new HashMap<Voter, Senate>();

  public static class Builder {
    protected String password;
    protected Urna urna;

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder urna(Urna urna) {
      this.urna = urna;
      return this;
    }

    public Election build() {
      if (password == null)
        throw new IllegalArgumentException("password mustn't be null");

      if (password.isEmpty())
        throw new IllegalArgumentException("password mustn't be empty");

      return new Election(this.password);
    }

  }

  protected Election(
      String password) {
    this.password = password;
    this.status = false;
    this.nullFederalDeputyVotes = 0;
    this.nullPresidentVotes = 0;
    this.nullGovernorVotes = 0;
    this.nullMayorVotes = 0;
    this.nullSenateVotes = 0;
    this.presidentProtestVotes = 0;
    this.federalDeputyProtestVotes = 0;
    this.senateProtestVotes = 0;
    this.governorProtestVotes = 0;
    this.mayorProtestVotes = 0;
  }

  private Boolean isValid(String password) {
    return this.password.equals(password);
  }

  public void computeVote(Candidate candidate, Voter voter) {
    if (candidate instanceof President) {
      if (votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      candidate.numVotes++;
      votersPresident.put(voter, 1);
    } else if (candidate instanceof FederalDeputy) {
      if (votersFederalDeputy.get(voter) != null && votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      if (tempFDVote.get(voter) != null && tempFDVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersFederalDeputy.get(voter) == null) {
        votersFederalDeputy.put(voter, 1);
        tempFDVote.put(voter, (FederalDeputy) candidate);
      } else {
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
        tempFDVote.remove(voter);
      }
    } else if (candidate instanceof Senate) {
      if (votersSenate.get(voter) != null && votersSenate.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para senador");

      if (tempSVote.get(voter) != null && tempSVote.get(voter).equals(candidate))
        throw new Warning("Você não pode votar mais de uma vez em um mesmo candidato");

      candidate.numVotes++;
      if (votersSenate.get(voter) == null) {
        votersSenate.put(voter, 1);
        tempSVote.put(voter, (Senate) candidate);
      } else {
        votersSenate.put(voter, this.votersSenate.get(voter) + 1);
        tempSVote.remove(voter);
      }
    } else if (candidate instanceof Governor) {
      if (votersGovernor.get(voter) != null && votersGovernor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para governador");

      candidate.numVotes++;
      votersGovernor.put(voter, 1);
    } else if (candidate instanceof Mayor) {
      if (votersMayor.get(voter) != null && votersMayor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para prefeito");

      candidate.numVotes++;
      votersMayor.put(voter, 1);
    }
  };

  public void computeNullVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.nullPresidentVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.nullFederalDeputyVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    } else if (type.equals("Governor")) {
      if (this.votersGovernor.get(voter) != null && votersGovernor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para governador");

      this.nullGovernorVotes++;
      votersGovernor.put(voter, 1);
    } else if (type.equals("Mayor")) {
      if (this.votersMayor.get(voter) != null && votersMayor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para prefeito");

      this.nullMayorVotes++;
      votersMayor.put(voter, 1);
    } else if (type.equals("Senate")) {
      if (this.votersSenate.get(voter) != null && this.votersSenate.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para senador");

      this.nullSenateVotes++;
      if (this.votersSenate.get(voter) == null)
        votersSenate.put(voter, 1);
      else
        votersSenate.put(voter, this.votersSenate.get(voter) + 1);
    }

  }

  public void computeProtestVote(String type, Voter voter) {
    if (type.equals("President")) {
      if (this.votersPresident.get(voter) != null && votersPresident.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para presidente");

      this.presidentProtestVotes++;
      votersPresident.put(voter, 1);
    } else if (type.equals("FederalDeputy")) {
      if (this.votersFederalDeputy.get(voter) != null && this.votersFederalDeputy.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para deputado federal");

      this.federalDeputyProtestVotes++;
      if (this.votersFederalDeputy.get(voter) == null)
        votersFederalDeputy.put(voter, 1);
      else
        votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
    } else if (type.equals("Governor")) {
      if (this.votersGovernor.get(voter) != null && votersGovernor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para governador");

      this.governorProtestVotes++;
      votersGovernor.put(voter, 1);
    } else if (type.equals("Mayor")) {
      if (this.votersMayor.get(voter) != null && votersMayor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para prefeito");

      this.mayorProtestVotes++;
      votersMayor.put(voter, 1);
    } else if (type.equals("Senate")) {
      if (this.votersSenate.get(voter) != null && this.votersSenate.get(voter) >= 2)
        throw new StopTrap("Você não pode votar mais de uma vez para senado");

      this.senateProtestVotes++;
      if (this.votersSenate.get(voter) == null)
        votersSenate.put(voter, 1);
      else
        votersSenate.put(voter, this.votersSenate.get(voter) + 1);
    }
  }

  public boolean getStatus() {
    return this.status;
  }

  public void start(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = true;
  }

  public void finish(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.status = false;
  }

  public President getPresidentByNumber(int number) {
    return urna.presidentCandidates.get(number);
  }

  public void addPresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (urna.presidentCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    urna.presidentCandidates.put(candidate.number, candidate);

  }

  public void removePresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    urna.presidentCandidates.remove(candidate.number);
  }

  public Senate getSenateByNumber(String state, int number) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(state);
    return urnaEstado.senateCandidates.get(number);
  }

  public void addSenateCandidate(Senate candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (urnaEstado.senateCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    urnaEstado.senateCandidates.put(candidate.number, candidate);
  }

  public void removeSenateCandidate(Senate candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    urnaEstado.senateCandidates.remove(candidate.number);
  }

  public FederalDeputy getFederalDeputyByNumber(String state, Integer number) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(state);
    return urnaEstado.federalDeputyCandidates.get(number);
  }

  public void addFederalDeputyCandidate(FederalDeputy candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (urnaEstado.federalDeputyCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    urnaEstado.federalDeputyCandidates.put(candidate.number, candidate);
  }

  public void removeFederalDeputyCandidate(FederalDeputy candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    urnaEstado.federalDeputyCandidates.remove(candidate.number);
  }

  public Mayor getMayorByNumber(String state, String city, int number) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(state);
    return urnaEstado.mayorCandidates.get(number);
  }

  public void addMayorCandidate(Mayor candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (urnaEstado.mayorCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");
    urnaEstado.mayorCandidates.put(candidate.number, candidate);
  }

  public void removeMayorCandidate(Mayor candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    urnaEstado.mayorCandidates.remove(candidate.city + candidate.number);
  }

  public Governor getGovernorByNumber(String state, int number) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(state);
    return urnaEstado.governorCandidates.get(number);
  }

  public void addGovernorCandidate(Governor candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (urnaEstado.governorCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");
    urnaEstado.governorCandidates.put(candidate.number, candidate);
  }

  public void removeGovernorCandidate(Governor candidate, String password) {
    UrnaEstadual urnaEstado = urna.UrnasMap.get(candidate.state);
    if (!isValid(password))
      throw new Warning("Senha inválida");

    urnaEstado.governorCandidates.remove(candidate.number);
  }

  public String getResults(String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.status)
      throw new StopTrap("Eleição ainda não finalizou, não é possível gerar o resultado");

    var decimalFormater = new DecimalFormat("0.00");
    var presidentRank = new ArrayList<President>();
    var federalDeputyRank = new ArrayList<FederalDeputy>();
    var governorRank = new ArrayList<Governor>();
    var senateRank = new ArrayList<Senate>();
    var mayorRank = new ArrayList<Mayor>();

    var builder = new StringBuilder();

    builder.append("Resultado da eleicao:\n");

    int totalVotesP = presidentProtestVotes + nullPresidentVotes;
    for (Map.Entry<Integer, President> candidateEntry : urna.presidentCandidates.entrySet()) {
      President candidate = candidateEntry.getValue();
      totalVotesP += candidate.numVotes;
      presidentRank.add(candidate);
    }

    /*
     * Parte modificada:
     * Calcula votos separados para cada urna: urna nacional e urnas
     * estaduais implementadas pelo Broker
     */
    int totalVotesFD = federalDeputyProtestVotes + nullFederalDeputyVotes;
    int totalVotesM = mayorProtestVotes + nullMayorVotes;
    int totalVotesS = senateProtestVotes + nullSenateVotes;
    int totalVotesG = governorProtestVotes + nullGovernorVotes;
    for (Map.Entry<String, UrnaEstadual> stateUrnas : urna.UrnasMap.entrySet()) {
      UrnaEstadual UrnaEstado = stateUrnas.getValue();
      for (Map.Entry<Integer, FederalDeputy> candidateEntry : UrnaEstado.federalDeputyCandidates.entrySet()) {
        FederalDeputy candidate = candidateEntry.getValue();
        totalVotesFD += candidate.numVotes;
        federalDeputyRank.add(candidate);
      }

      for (Map.Entry<Integer, Mayor> candidateEntry : UrnaEstado.mayorCandidates.entrySet()) {
        Mayor candidate = candidateEntry.getValue();
        totalVotesM += candidate.numVotes;
        mayorRank.add(candidate);
      }

      for (Map.Entry<Integer, Senate> candidateEntry : UrnaEstado.senateCandidates.entrySet()) {
        Senate candidate = candidateEntry.getValue();
        totalVotesS += candidate.numVotes;
        senateRank.add(candidate);
      }

      for (Map.Entry<Integer, Governor> candidateEntry : UrnaEstado.governorCandidates.entrySet()) {
        Governor candidate = candidateEntry.getValue();
        totalVotesG += candidate.numVotes;
        governorRank.add(candidate);
      }
    }

    var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedPresidentRank = presidentRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedGovernorRank = governorRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedMayorRank = mayorRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    var sortedSenateRank = senateRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());

    builder.append("  Votos presidente:\n");
    builder.append("  Total: " + totalVotesP + "\n");
    builder.append("  Votos nulos: " + nullPresidentVotes + " ("
        + decimalFormater.format((double) nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + presidentProtestVotes + " ("
        + decimalFormater.format((double) presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (President candidate : sortedPresidentRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format((double) candidate.numVotes / (double) totalVotesP * 100)
          + "%\n");
    }

    President electPresident = sortedPresidentRank.get(0);
    builder.append("\n\n  Presidente eleito:\n");
    builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
        + decimalFormater.format((double) electPresident.numVotes / (double) totalVotesP * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("\n\n  Votos deputado federal:\n");
    builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
        + decimalFormater.format((double) nullFederalDeputyVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
        + decimalFormater.format((double) federalDeputyProtestVotes / (double) totalVotesFD * 100) + "%)\n");
    builder.append("  Total: " + totalVotesFD + "\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (FederalDeputy candidate : sortedFederalDeputyRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesFD * 100)
              + "%\n");
    }

    FederalDeputy firstDeputy = sortedFederalDeputyRank.get(0);
    FederalDeputy secondDeputy = sortedFederalDeputyRank.get(1);
    builder.append("\n\n  Deputados eleitos:\n");
    builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
        + decimalFormater.format((double) firstDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
    builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
        + decimalFormater.format((double) secondDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("\n\n  Votos senado:\n");
    builder.append("  Votos nulos: " + nullSenateVotes + " ("
        + decimalFormater.format((double) nullSenateVotes / (double) totalVotesS * 100) + "%)\n");
    builder.append("  Votos brancos: " + senateProtestVotes + " ("
        + decimalFormater.format((double) senateProtestVotes / (double) totalVotesS * 100) + "%)\n");
    builder.append("  Total: " + totalVotesS + "\n");
    builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
    for (Senate candidate : sortedSenateRank) {
      builder.append(
          "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesS * 100)
              + "%\n");
    }

    Senate firstSenate = sortedSenateRank.get(0);
    Senate secondSenate = sortedSenateRank.get(1);
    builder.append("\n\n  Senadores eleitos:\n");
    builder.append("  1º " + firstSenate.name + " do " + firstSenate.party + " com "
        + decimalFormater.format((double) firstSenate.numVotes / (double) totalVotesS * 100) + "% dos votos\n");
    builder.append("  2º " + secondSenate.name + " do " + secondSenate.party + " com "
        + decimalFormater.format((double) secondSenate.numVotes / (double) totalVotesS * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("  Votos governador:\n");
    builder.append("  Total: " + totalVotesG + "\n");
    builder.append("  Votos nulos: " + nullGovernorVotes + " ("
        + decimalFormater.format((double) nullGovernorVotes / (double) totalVotesG * 100) + "%)\n");
    builder.append("  Votos brancos: " + governorProtestVotes + " ("
        + decimalFormater.format((double) governorProtestVotes / (double) totalVotesG * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Governor candidate : sortedGovernorRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format((double) candidate.numVotes / (double) totalVotesG * 100)
          + "%\n");
    }

    Governor electGovernor = sortedGovernorRank.get(0);
    builder.append("\n\n  Governador eleito:\n");
    builder.append("  " + electGovernor.name + " do " + electGovernor.party + " com "
        + decimalFormater.format((double) electGovernor.numVotes / (double) totalVotesG * 100) + "% dos votos\n");
    builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");

    builder.append("  Votos prefeito:\n");
    builder.append("  Total: " + totalVotesM + "\n");
    builder.append("  Votos nulos: " + nullMayorVotes + " ("
        + decimalFormater.format((double) nullMayorVotes / (double) totalVotesM * 100) + "%)\n");
    builder.append("  Votos brancos: " + mayorProtestVotes + " ("
        + decimalFormater.format((double) mayorProtestVotes / (double) totalVotesM * 100) + "%)\n");
    builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
    for (Mayor candidate : sortedMayorRank) {
      builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
          + candidate.numVotes + " - "
          + decimalFormater.format((double) candidate.numVotes / (double) totalVotesM * 100)
          + "%\n");
    }

    Mayor electMayor = sortedMayorRank.get(0);
    builder.append("\n\n  Prefeito eleito:\n");
    builder.append("  " + electMayor.name + " do " + electMayor.party + " com "
        + decimalFormater.format((double) electMayor.numVotes / (double) totalVotesM * 100) + "% dos votos\n");

    return builder.toString();
  }
}
