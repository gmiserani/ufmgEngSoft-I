package br.ufmg.engsoft.reprova.services.input;

public class GetQuestionByIdInput {
	
	private String id;

	public GetQuestionByIdInput(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
