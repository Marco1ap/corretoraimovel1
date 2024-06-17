package br.com.corretoraImovel.model;

public class Endereco {
	  
	private Long id;

	private String logradouro;

	private String bairro;
	  
	private String complemento;

	public Endereco() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Endereco(Long id,  String logradouro, String bairro, String complemento) {
		super();
		this.id = id;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.complemento = complemento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Override
	public String toString() {
		return "Endereco [id=" + id + ", logradouro=" + logradouro + ", bairro=" + bairro + ", complemento="
				+ complemento + "]";
	}
	
	

}
