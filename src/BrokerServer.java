import java.util.Map;

public class BrokerServer{
	UrnaEstadual urna;

	public BrokerServer(String estado, Map<String, UrnaEstadual> UrnasMap) {
		urna = UrnasMap.get(estado);
	}

	public UrnaEstadual returnResult() {
		return urna;
	}

}