package br.com.corretoraImovel.model;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
public class Proprietario extends Pessoa{

//
	public Proprietario (){
		super();
	}

	private Long id;

	public Proprietario(String nome, String documento, String telefone, String email) {
		super(nome, documento, telefone, email);
	}

	public Proprietario(Long id ) {
		super();
		this.id = id;

	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Proprietario [id=" + id + ", imovel=" +  "]";
	}

	
}

