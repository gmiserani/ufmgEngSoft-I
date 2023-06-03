public class BrokerClient {
	String methodName;
	String params;
	String message;
	CallMessage resultMessage;
	
	public BrokerClient(CallMessage msg) {
		
		
	}

	public CallMessage getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(CallMessage resultMessage) {
		this.resultMessage = resultMessage;
	}

}