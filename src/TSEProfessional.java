/* Classe nao modificada: usada para validar o profissional
*  encarregado de gerenciar a urna */
public class TSEProfessional {
  protected final String user;

  protected final String password;

  public TSEProfessional(
      String user,
      String password) {
    this.user = user;
    this.password = password;
  }
}
