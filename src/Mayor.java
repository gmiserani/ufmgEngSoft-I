/* Classe adicionada
 * Um prefeito eh um candidato valido com
 * nome, partido, numero, estado e cidade
 */

import java.util.Set;

public class Mayor extends Candidate {
  protected final String city;
  protected final String state;

  public static class Builder {
    protected String name;
    protected String party;
    protected int number;
    protected String city;
    protected String state;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder party(String party) {
      this.party = party;
      return this;
    }

    public Builder number(int number) {
      this.number = number;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Mayor build() {
      if (number <= 0)
        throw new IllegalArgumentException("number mustn't be less than or equal to 0");

      if (name == null)
        throw new IllegalArgumentException("name mustn't be null");

      if (name.isEmpty())
        throw new IllegalArgumentException("name mustn't be empty");

      if (party == null)
        throw new IllegalArgumentException("party mustn't be null");

      if (party.isEmpty())
        throw new IllegalArgumentException("party mustn't be empty");

      if (city == null)
        throw new IllegalArgumentException("city mustn't be null");

      if (city.isEmpty())
        throw new IllegalArgumentException("city mustn't be empty");

      return new Mayor(
          this.name,
          this.party,
          this.number,
          this.city,
          this.state);
    }
  }

  protected Mayor(
      String name,
      String party,
      int number,
      String city,
      String state) {
    super(name, party, number);
    this.city = city;
    this.state = state;
  }

  @Override
  public String toString() {
    return super.name + super.party + super.number + this.city + this.state;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Mayor))
      return false;

    var fd = (Mayor) obj;

    return this.toString().equals(fd.toString());
  }

  public String getCity() {
    return this.city;
  }

  public String getState() {
    return this.state;
  }
}
