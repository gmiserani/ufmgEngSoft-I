import java.util.Map;

public class BrokerServer{
	UrnaEstadual urna = new UrnaEstadual();

	public BrokerServer(String estado, Map<String, UrnaEstadual> UrnasMap) {
		urna = UrnasMap.get(estado);
	}

	public UrnaEstadual returnResult() {
		return urna;
	}

}