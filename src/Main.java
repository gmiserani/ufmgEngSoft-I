/* Classe adicionada
 * Classe Main criada separadamente apenas com metodo main
 * e outros metodos de inicializacao
 */

// Padrao de projeto utilizado para permitir apenas a criacao de uma instancia de eleicao
public class Main {
    private static Election uniqueInstance;

    private Main() {
    }

    public synchronized static Election getInstance(Election e) {
        if (uniqueInstance == null) {
            uniqueInstance = e;
        }
        return uniqueInstance;
    }

    /*
     * Metodo main inicializa a eleicao junto com sua urna, inicializa candidatos
     * automaticamente na urna para fins de exemplificacao e apresenta o menu para
     * o usuario, de onde parte as proximas interacoes com a urna
     */
    public static void main(String[] args) {

        Urna urna = new Urna();

        // Startup the current election instance
        String electionPassword = "password";

        Election currentElection = new Election.Builder()
                .urna(urna)
                .password(electionPassword)
                .build();

        Election uniqueElectionInstance = Main.getInstance(currentElection);

        urna.eleicao = uniqueElectionInstance;
        uniqueElectionInstance.urna = new Urna();

        President presidentCandidate1 = new President.Builder().name("João").number(123).party("PDS1").build();
        uniqueElectionInstance.addPresidentCandidate(presidentCandidate1, electionPassword);
        President presidentCandidate2 = new President.Builder().name("Maria").number(124).party("ED").build();
        uniqueElectionInstance.addPresidentCandidate(presidentCandidate2, electionPassword);

        UrnaEstadual urnaMG = new UrnaEstadual();
        UrnaEstadual urnaSP = new UrnaEstadual();
        UrnaEstadual urnaRJ = new UrnaEstadual();
        UrnaEstadual urnaES = new UrnaEstadual();

        urnaMG.estado = "MG";
        urnaMG.eleicao = uniqueElectionInstance;
        urnaSP.eleicao = uniqueElectionInstance;
        urnaRJ.eleicao = uniqueElectionInstance;
        urnaES.eleicao = uniqueElectionInstance;

        FederalDeputy federalDeputyCandidate1 = new FederalDeputy.Builder().name("Carlos").number(12345).party("PDS1")
                .state("MG").build();
        urnaMG.federalDeputyCandidates.put(12345, federalDeputyCandidate1);
        FederalDeputy federalDeputyCandidate2 = new FederalDeputy.Builder().name("Cleber").number(54321).party("PDS2")
                .state("MG").build();
        urnaMG.federalDeputyCandidates.put(54321, federalDeputyCandidate2);
        FederalDeputy federalDeputyCandidate3 = new FederalDeputy.Builder().name("Sofia").number(11211).party("IHC")
                .state("SP").build();
        urnaSP.federalDeputyCandidates.put(11211, federalDeputyCandidate3);

        Governor governorCadidate1 = new Governor.Builder().name("Marcos").number(123456).party("PDS1")
                .state("MG").build();
        urnaMG.governorCandidates.put(123456, governorCadidate1);
        Governor governorCadidate2 = new Governor.Builder().name("Marcelo").number(654321).party("PDS2")
                .state("MG").build();
        urnaMG.governorCandidates.put(654321, governorCadidate2);
        Governor governorCadidate3 = new Governor.Builder().name("Matheus").number(123546).party("PDS3")
                .state("ES").build();
        urnaES.governorCandidates.put(123546, governorCadidate3);

        Senate senateCandidate1 = new Senate.Builder().name("Sarah").number(1234567).party("PDS1")
                .state("MG").build();
        urnaES.senateCandidates.put(1234567, senateCandidate1);
        Senate senateCandidate2 = new Senate.Builder().name("Maria").number(7654321).party("PDS2")
                .state("MG").build();
        urnaMG.senateCandidates.put(7654321, senateCandidate2);
        Senate senateCandidate3 = new Senate.Builder().name("Samantha").number(1121177).party("IHC")
                .state("BA").build();
        urnaES.senateCandidates.put(1121177, senateCandidate3);

        Mayor mayorCadidate1 = new Mayor.Builder().name("Marcos").number(123354456).party("PDS1")
                .city("bh").build();
        urnaMG.mayorCandidates.put(123354456, mayorCadidate1);
        Mayor mayorCadidate2 = new Mayor.Builder().name("Marcelo").number(653454321).party("PDS2")
                .city("bh").build();
        urnaMG.mayorCandidates.put(653454321, mayorCadidate2);
        Mayor mayorCadidate3 = new Mayor.Builder().name("Matheus").number(123345546).party("PDS3")
                .city("uberlandia").build();
        urnaMG.mayorCandidates.put(123345546, mayorCadidate3);

        uniqueElectionInstance.urna.UrnasMap.put("MG", urnaMG);
        uniqueElectionInstance.urna.UrnasMap.put("SP", urnaSP);
        uniqueElectionInstance.urna.UrnasMap.put("RJ", urnaRJ);
        uniqueElectionInstance.urna.UrnasMap.put("ES", urnaES);

        urna.UrnasMap.put("MG", urnaMG);
        urna.UrnasMap.put("SP", urnaSP);
        urna.UrnasMap.put("RJ", urnaRJ);
        urna.UrnasMap.put("ES", urnaES);
        urna.eleicao = uniqueElectionInstance;
        urnaMG.eleicao = uniqueElectionInstance;
        urnaSP.eleicao = uniqueElectionInstance;
        urnaRJ.eleicao = uniqueElectionInstance;
        urnaES.eleicao = uniqueElectionInstance;

        uniqueElectionInstance.urna.loadVoters();
        uniqueElectionInstance.urna.loadTSEProfessionals();
        uniqueElectionInstance.urna.startMenu();
    }
}