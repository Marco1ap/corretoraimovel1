package br.com.corretoraImovel.services;

import br.com.corretoraImovel.model.Pessoa;
import br.com.corretoraImovel.repository.VisitanteRepository;
import br.com.corretoraImovel.utils.Utils;

import java.sql.SQLException;

public class VisitanteService {
	
	static VisitanteRepository visitanteRepository = new VisitanteRepository();
	static Utils validarCPF = new Utils();

	public static boolean existe(Long id) {
		return VisitanteRepository.findById(id) != null ? true : false;
	}

	public static Pessoa findById(Long id) {
		return VisitanteRepository.findById(id);
	}

	public static Pessoa save(Pessoa visitante) {
	
		Pessoa visitanteRepository = null;
		try {
			if(validacaoVisitante(visitante) == false) {
				return null;
			}
			else {
				visitanteRepository = VisitanteRepository.save(visitante);
				return visitanteRepository;
				}
		}
		catch (RuntimeException ex) {
	           System.out.println("Exceção de runtime ao salvar visitante: "+ ex);
	    } catch (Exception ex) {
	    	System.out.println("Exceção ao salvar visitante: "+ex);
	    }	
		
		return visitanteRepository;
		
	}

	public static Pessoa update(Long id, Pessoa visitante) {

		Pessoa velho = VisitanteRepository.findById(id);
		Pessoa novo = null;
		if (velho == null || velho.getId() != visitante.getId()) {
			return VisitanteRepository.save(visitante);
		}
		novo = VisitanteRepository.update(visitante);
		return novo;
	}

	public static boolean delete(Long visitanteId) throws SQLException {
		if (existe(visitanteId)) {
			return VisitanteRepository.delete(visitanteId);
		} else {
			return false;
		}

	}
	
	 public static boolean validacaoVisitante(Pessoa visitante) {
	        boolean retorno = false;

	        if(visitante.getDocumento().equals("") || visitante.getNome().equals("") || visitante.getTelefone().equals("")) {
	            return retorno = false;
	        }
	        else if (!validarCPF.validarCPF(visitante.getDocumento())){
	        	 System.out.println("CPF inválido.");
	        	retorno = false;
	        }
	        else 
	        	retorno = true;
	        
	        return retorno;
	    }

}
