public class Main extends Urna{
    public static void main(String[] args) {
        // Startup the current election instance
        String electionPassword = "password";

        currentElection = new Election.Builder()
            .password(electionPassword)
            .build();

        President presidentCandidate1 = new President.Builder().name("João").number(123).party("PDS1").build();
        currentElection.addPresidentCandidate(presidentCandidate1, electionPassword);
        President presidentCandidate2 = new President.Builder().name("Maria").number(124).party("ED").build();
        currentElection.addPresidentCandidate(presidentCandidate2, electionPassword);

        FederalDeputy federalDeputyCandidate1 = new FederalDeputy.Builder().name("Carlos").number(12345).party("PDS1")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate1, electionPassword);
        FederalDeputy federalDeputyCandidate2 = new FederalDeputy.Builder().name("Cleber").number(54321).party("PDS2")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate2, electionPassword);
        FederalDeputy federalDeputyCandidate3 = new FederalDeputy.Builder().name("Sofia").number(11211).party("IHC")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate3, electionPassword);

        Governor governorCadidate1 = new Governor.Builder().name("Marcos").number(123456).party("PDS1")
            .state("MG").build();
        currentElection.addGovernorCandidate(governorCadidate1, electionPassword);
        Governor governorCadidate2 = new Governor.Builder().name("Marcelo").number(654321).party("PDS2")
            .state("MG").build();
        currentElection.addGovernorCandidate(governorCadidate2, electionPassword);
        Governor governorCadidate3 = new Governor.Builder().name("Matheus").number(123546).party("PDS3")
            .state("MG").build();
        currentElection.addGovernorCandidate(governorCadidate3, electionPassword);

        Senate senateCandidate1 = new Senate.Builder().name("Sarah").number(1234567).party("PDS1")
            .state("MG").build();
        currentElection.addSenateCandidate(senateCandidate1, electionPassword);
        Senate senateCandidate2 = new Senate.Builder().name("Maria").number(7654321).party("PDS2")
            .state("MG").build();
        currentElection.addSenateCandidate(senateCandidate2, electionPassword);
        Senate senateCandidate3 = new Senate.Builder().name("Samantha").number(1121177).party("IHC")
            .state("MG").build();
        currentElection.addSenateCandidate(senateCandidate3, electionPassword);

        // Startar todo os eleitores e profissionais do TSE
        loadVoters();
        loadTSEProfessionals();

        startMenu();
    }
}