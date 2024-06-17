package br.com.corretoraImovel.services;

import br.com.corretoraImovel.model.Agendamento;
import br.com.corretoraImovel.model.Imovel;
import br.com.corretoraImovel.model.Pessoa;
import br.com.corretoraImovel.repository.AgendamentoRepository;
import br.com.corretoraImovel.utils.Utils;

import java.sql.SQLException;

public class AgendamentoService {

    static AgendamentoRepository agendamentoRepository = new AgendamentoRepository();
    static Utils validarCPF = new Utils();

    public static boolean existe(Long id) throws SQLException {
        return AgendamentoRepository.findById(id) != null ? true : false;
    }

    public static Agendamento findById(Long id) throws SQLException {
        return AgendamentoRepository.findById(id);
    }

    public static Agendamento save(Agendamento agendamento) {

        Agendamento agendamentoRepository = null;
        try {
            if (validacaoAgendamento(agendamento) == false) {
                return null;
            } else {
                agendamentoRepository = AgendamentoRepository.save(agendamento);
                return agendamentoRepository;
            }
        } catch (RuntimeException ex) {
            System.out.println("Exceção de runtime ao salvar agendamento: " + ex);
        } catch (Exception ex) {
            System.out.println("Exceção ao salvar agendamento: " + ex);
        }

        return agendamentoRepository;

    }

    public static Agendamento update(Long id, Agendamento agendamento) throws SQLException {

        Agendamento velho = AgendamentoRepository.findById(id);
        Agendamento novo = null;
        System.out.println("ola"+ velho);
        System.out.println("ola"+ agendamento.getId()+"ola 2 "+ velho.getId());
        if (velho == null || velho.getId() != agendamento.getId()) {
            System.out.println("dd");
            return AgendamentoRepository.save(agendamento);
        }
        try {
            System.out.println("dddddddddd");
            if (validacaoAgendamento(agendamento) == false) {
                System.out.println("dddeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                return null;
            } else {
                System.out.println("444444444444444");
                novo = AgendamentoRepository.update(agendamento);
                return novo;

            }
        } catch (RuntimeException ex) {
            System.out.println("Exceção de runtime ao atualizar agendamento: " + ex);
        } catch (Exception ex) {
            System.out.println("Exceção ao salvar agendamento: " + ex);
        }
        return novo;
    }



    public static boolean delete(Long agendamentoId) throws SQLException {
        if (existe(agendamentoId)) {
            return AgendamentoRepository.delete(agendamentoId);
        } else {
            return false;
        }

    }

    public static boolean validacaoAgendamento(Agendamento agendamento) {
        boolean retorno = false;

        if(validacaoVisitante(agendamento.getPessoa()) && validacaoImovel(agendamento.getImovel())) {
            if(agendamento.getData().equals("") || agendamento.getHora().equals("")) {
                retorno = false;
            } else
                retorno = true;
        }
        return retorno;
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



    public static boolean validacaoImovel(Imovel imovel) {
        boolean retorno = false;

        if(imovel.getDormitorios() <= 0 || imovel.getValorAluguel() <= 0 || imovel.getValorCondominio() <= 0 || imovel.getValorIptu() <= 0) {
            return retorno = false;
        }
        else{
            retorno = true;

        return retorno;}
    }

}



