package br.com.corretoraImovel.services;

import br.com.corretoraImovel.model.Proprietario;
import br.com.corretoraImovel.model.Proprietario;
import br.com.corretoraImovel.repository.ProprietarioRepository;

public class ProprietarioService {
	
	static ProprietarioRepository proprietarioRepository = new ProprietarioRepository();
	
	public static boolean existe(Long id) {
		return ProprietarioRepository.findById(id) != null ? true : false;
	}

	public static Proprietario findById(Long id) {
		return ProprietarioRepository.findById(id);
	}

	public static Proprietario save(Proprietario proprietario) {
	
		Proprietario proprietarioRepository = null;
		try {
			if(validacaoProprietario(proprietario) == false) {
				return null;
			}
			else {
				proprietarioRepository = ProprietarioRepository.save(proprietario);
				return proprietarioRepository;
				}
		}
		catch (RuntimeException ex) {
	           System.out.println("Exceção de runtime ao salvar proprietario: "+ ex);
	    } catch (Exception ex) {
	    	System.out.println("Exceção ao salvar proprietario: "+ex);
	    }	
		
		return proprietarioRepository;
		
	}

	public static Proprietario update(Long id, Proprietario proprietario) {

		Proprietario velho = ProprietarioRepository.findById(id);
		Proprietario novo = null;
		System.out.println("oi"+velho.getId());
		if (velho == null || velho.getId() != proprietario.getId()) {
			return ProprietarioRepository.save(proprietario);
		}
		novo = ProprietarioRepository.update(proprietario);
		return novo;
	}

	public static boolean delete(Long proprietarioId) {
		if (existe(proprietarioId)) {
			return ProprietarioRepository.delete(proprietarioId);
		} else {
			return false;
		}

	}
	
	 public static boolean validacaoProprietario(Proprietario proprietario) {
	        boolean retorno = false;

	        if(proprietario.getDocumento().equals("") || proprietario.getNome().equals("") || proprietario.getTelefone().equals("")) {
				System.out.println("validaçao de campos.");
	            return retorno = false;
	        }
	        else if (!validarCPF(proprietario.getDocumento())){
	        	 System.out.println("CPF inválido.");
	        	retorno = false;
	        }
	        else 
	        	retorno = true;
	        
	        return retorno;
	    }
	 
	 public static boolean validarCPF(String cpf) {
	        // Remove caracteres não numéricos do CPF
	        cpf = cpf.replaceAll("[^0-9]", "");

	        // Verifica se o CPF tem 11 dígitos
	        if (cpf.length() != 11) 	        	
	            return false;
	   
	        // Verifica se todos os dígitos são iguais
	        boolean todosDigitosIguais = true;
	        for (int i = 1; i < 11; i++) {
	            if (cpf.charAt(i) != cpf.charAt(0)) {
	                todosDigitosIguais = false;
	                break;
	            }
	        }
	        if (todosDigitosIguais) 	        	
	            return false;
	        
	        // Calcula e compara os dígitos verificadores
	        int soma = 0;
	        int peso = 10;
	        for (int i = 0; i < 9; i++) {
	            soma += (cpf.charAt(i) - '0') * peso;
	            peso--;
	        }
	        int digito1 = 11 - (soma % 11);
	        if (digito1 > 9)
	            digito1 = 0;

	        soma = 0;
	        peso = 11;
	        for (int i = 0; i < 10; i++) {
	            soma += (cpf.charAt(i) - '0') * peso;
	            peso--;
	        }
	        int digito2 = 11 - (soma % 11);
	        if (digito2 > 9)
	            digito2 = 0;

	        // Verifica se os dígitos verificadores calculados são iguais aos fornecidos no CPF
	        return (digito1 == cpf.charAt(9) - '0' && digito2 == cpf.charAt(10) - '0');
	    }

}
