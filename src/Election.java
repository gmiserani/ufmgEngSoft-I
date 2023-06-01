import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

public class Election {
  private final String password;

  private boolean status;

  private int nullPresidentVotes;

  private int nullFederalDeputyVotes;

  private int nullGovernorVotes;

  private int nullSenateVotes;

  private int presidentProtestVotes;

  private int federalDeputyProtestVotes;

  private int governorProtestVotes;

  private int senateProtestVotes;

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();

  // Na prática guardaria uma hash do eleitor
  private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersGovernor = new HashMap<Voter, Integer>();

  private Map<Voter, Integer> votersSenate = new HashMap<Voter, Integer>();

  public Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();

  public Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();
  
  public Map<String, Governor> governorCandidates = new HashMap<String, Governor>();

  public Map<String, Senate> senateCandidates = new HashMap<String, Senate>();

  private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();

  private Map<Voter, Senate> tempSVote = new HashMap<Voter, Senate>();

  public static class Builder {
    protected String password;

    public Builder password(String password) {
      this.password = password;
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
    this.nullSenateVotes = 0;
    this.presidentProtestVotes = 0;
    this.federalDeputyProtestVotes = 0;
    this.senateProtestVotes = 0;
    this.governorProtestVotes = 0;
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
    }else if (candidate instanceof Senate) {
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
    }
    else if (candidate instanceof Governor) {
      if (votersGovernor.get(voter) != null && votersGovernor.get(voter) >= 1)
        throw new StopTrap("Você não pode votar mais de uma vez para governador");

      candidate.numVotes++;
      votersGovernor.put(voter, 1);
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
    }else if (type.equals("Senate")) {
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
    }else if (type.equals("Senate")) {
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
    return this.presidentCandidates.get(number);
  }

  public void addPresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.presidentCandidates.get(candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.presidentCandidates.put(candidate.number, candidate);

  }

  public void removePresidentCandidate(President candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.presidentCandidates.remove(candidate.number);
  }

  public Senate getSenateByNumber(String state, int number) {
    return this.senateCandidates.get(state + number);
  }

  public void addSenateCandidate(Senate candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.senateCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.senateCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeSenateCandidate(Senate candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.senateCandidates.remove(candidate.state + candidate.number);
  }

  public FederalDeputy getFederalDeputyByNumber(String state, int number) {
    return this.federalDeputyCandidates.get(state + number);
  }

  public void addFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.federalDeputyCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");

    this.federalDeputyCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeFederalDeputyCandidate(FederalDeputy candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.federalDeputyCandidates.remove(candidate.state + candidate.number);
  }

  public Governor getGovernorByNumber(String state, int number) {
    return this.governorCandidates.get(state + number);
  }

  public void addGovernorCandidate(Governor candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    if (this.governorCandidates.get(candidate.state + candidate.number) != null)
      throw new Warning("Numero de candidato indisponível");
    this.governorCandidates.put(candidate.state + candidate.number, candidate);
  }

  public void removeGovernorCandidate(Governor candidate, String password) {
    if (!isValid(password))
      throw new Warning("Senha inválida");

    this.governorCandidates.remove(candidate.state + candidate.number);
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

    var builder = new StringBuilder();

    builder.append("Resultado da eleicao:\n");

    int totalVotesP = presidentProtestVotes + nullPresidentVotes;
    for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
      President candidate = candidateEntry.getValue();
      totalVotesP += candidate.numVotes;
      presidentRank.add(candidate);
    }

    int totalVotesFD = federalDeputyProtestVotes + nullFederalDeputyVotes;
    for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
      FederalDeputy candidate = candidateEntry.getValue();
      totalVotesFD += candidate.numVotes;
      federalDeputyRank.add(candidate);
    }

    int totalVotesG = governorProtestVotes + nullGovernorVotes;
    for (Map.Entry<String, Governor> candidateEntry : governorCandidates.entrySet()) {
      Governor candidate = candidateEntry.getValue();
      totalVotesG += candidate.numVotes;
      governorRank.add(candidate);
    }

    int totalVotesS = senateProtestVotes + nullSenateVotes;
    for (Map.Entry<String, Senate> candidateEntry : senateCandidates.entrySet()) {
      Senate candidate = candidateEntry.getValue();
      totalVotesS += candidate.numVotes;
      senateRank.add(candidate);
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

    return builder.toString();
  }
}
