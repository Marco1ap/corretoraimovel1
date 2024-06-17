package br.com.corretoraImovel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class Imovel {

	private Long id;

	private Endereco endereco;

    private int dormitorios;

    private double valorAluguel;

    private double valorCondominio;

    private double valorIptu;

    private double valorTaxaIncendio;

	private Proprietario proprietario;

    
	public Imovel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Imovel(Long id, Endereco endereco,  int dormitorios, double valorAluguel, double valorCondominio,
				  double valorIptu, double valorTaxaIncendio, Proprietario proprietario) {
		super();
		this.id = id;
		this.endereco = endereco;
		this.dormitorios = dormitorios;
		this.valorAluguel = valorAluguel;
		this.valorCondominio = valorCondominio;
		this.valorIptu = valorIptu;
		this.valorTaxaIncendio = valorTaxaIncendio;
		this.proprietario = proprietario;
	
	}


	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	public int getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(int dormitorios) {
		this.dormitorios = dormitorios;
	}
	public double getValorAluguel() {
		return valorAluguel;
	}
	public void setValorAluguel(double valorAluguel) {
		this.valorAluguel = valorAluguel;
	}
	public double getValorCondominio() {
		return valorCondominio;
	}
	public void setValorCondominio(double valorCondominio) {
		this.valorCondominio = valorCondominio;
	}
	public double getValorIptu() {
		return valorIptu;
	}
	public void setValorIptu(double valorIptu) {
		this.valorIptu = valorIptu;
	}
	public double getValorTaxaIncendio() {
		return valorTaxaIncendio;
	}
	public void setValorTaxaIncendio(double valorTaxaIncendio) {
		this.valorTaxaIncendio = valorTaxaIncendio;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	@Override
	public String toString() {
		return "Imovel [id=" + id + ", dormitorios=" + dormitorios + ", valorAluguel=" + valorAluguel
				+ ", valorCondominio=" + valorCondominio + ", valorIptu=" + valorIptu + ", valorTaxaIncendio="
				+ valorTaxaIncendio + "]";
	}
    
    
}
