// Classe nao modificada: usada para guardar as informacoes do usuario
public class Voter {
  protected final String electoralCard;

  protected final String name;

  protected final String state;

  public String city;

  public static class Builder {
    private String electoralCard;
    private String name;
    private String state;

    public Builder electoralCard(String electoralCard) {
      this.electoralCard = electoralCard;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Voter build() {
      if (electoralCard == null)
        throw new IllegalArgumentException("electoralCard mustn't be null");

      if (electoralCard.isEmpty())
        throw new IllegalArgumentException("electoralCard mustn't be empty");

      if (name == null)
        throw new IllegalArgumentException("name mustn't be null");

      if (name.isEmpty())
        throw new IllegalArgumentException("name mustn't be empty");

      if (state == null)
        throw new IllegalArgumentException("state mustn't be null");

      if (state.isEmpty())
        throw new IllegalArgumentException("state mustn't be empty");

      return new Voter(electoralCard, name, state);
    }
  }

  protected Voter(String electoralCard, String name, String state) {
    this.electoralCard = electoralCard;
    this.name = name;
    this.state = state;
  }

  public void vote(int number, Election election, String type, Boolean isProtestVote) {
    if (type.equals("President")) {
      if (isProtestVote)
        election.computeProtestVote("President", this);
      else if (number == 0)
        election.computeNullVote("President", this);
      else {
        President candidate = election.getPresidentByNumber(number);
        if (candidate == null)
          throw new Warning("Número de candidato inválido");
        election.computeVote(candidate, this);
      }
    } else if (type.equals("FederalDeputy"))
      if (number == 0)
        election.computeNullVote("FederalDeputy", this);
      else if (isProtestVote)
        election.computeProtestVote("FederalDeputy", this);
      else {
        FederalDeputy candidate = election.getFederalDeputyByNumber(this.state, number);
        if (candidate == null)
          throw new Warning("Número de candidato inválido");
        election.computeVote(candidate, this);
      }
    else if (type.equals("Governor"))
      if (number == 0)
        election.computeNullVote("Governor", this);
      else if (isProtestVote)
        election.computeProtestVote("Governor", this);
      else {
        Governor candidate = election.getGovernorByNumber(this.state, number);
        if (candidate == null)
          throw new Warning("Número de candidato inválido");
        election.computeVote(candidate, this);
      }
    else if (type.equals("Senate"))
      if (number == 0)
        election.computeNullVote("Senate", this);
      else if (isProtestVote)
        election.computeProtestVote("Senate", this);
      else {
        Senate candidate = election.getSenateByNumber(this.state, number);
        if (candidate == null)
          throw new Warning("Número de candidato inválido");
        election.computeVote(candidate, this);
      }
    else if (type.equals("Mayor"))
      if (number == 0)
        election.computeNullVote("Mayor", this);
      else if (isProtestVote)
        election.computeProtestVote("Mayor", this);
      else {
        Mayor candidate = election.getMayorByNumber(this.state, this.city, number);
        if (candidate == null)
          throw new Warning("Número de candidato inválido");
        election.computeVote(candidate, this);
      }
  }
}
