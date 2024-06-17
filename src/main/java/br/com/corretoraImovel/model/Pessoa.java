package br.com.corretoraImovel.model;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;

public class Pessoa {
	
	 private Long id;

	 private String nome;

	 private String documento;

	 private String telefone;

	 private String email;

	 public Pessoa() {
		
	}

	public Pessoa (String nome,String documento, String telefone,String email) {
		this.nome = nome;
		this.documento = documento;
		this.telefone = telefone;
		this.email = email;

	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	    public String getDocumento() {return documento;
	    }

	    public void setDocumento(String documento) {
	        this.documento = documento;
	    }

	    public String getTelefone() {
	        return telefone;
	    }

	    public void setTelefone(String telefone) {
	        this.telefone = telefone;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

		@Override
		public int hashCode() {
			return Objects.hash(documento, email, nome, telefone);
		}


		@Override
		public String toString() {
			return "Pessoa [nome=" + nome + ", cpf=" + documento + ", telefone=" + telefone + ", email=" + email + "]";
		}

	
}

