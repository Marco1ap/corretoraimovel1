package br.com.corretoraImovel.services;

import br.com.corretoraImovel.model.Imovel;
import br.com.corretoraImovel.repository.ImovelRepository;

import java.sql.SQLException;

public class ImovelService {
    
        static ImovelRepository imovelRepository = new ImovelRepository();

        public static boolean existe(Long id) {
            return ImovelRepository.findById(id) != null ? true : false;
        }

        public static Imovel findById(Long id) {
            return ImovelRepository.findById(id);
        }

        public static Imovel save(Imovel imovel) {

            Imovel imovelRepository = null;
            try {
       //         if(validacaoImovel(imovel) == false) {
        //            return null;
       //         }
       //         else {
                    imovelRepository = ImovelRepository.save(imovel);
                    return imovelRepository;
        //        }
            }
            catch (RuntimeException ex) {
                System.out.println("Exceção de runtime ao salvar imovel: "+ ex);
            } catch (Exception ex) {
                System.out.println("Exceção ao salvar imovel: "+ex);
            }

            return imovelRepository;

        }

        public static Imovel update(Long id, Imovel imovel) throws SQLException {

            Imovel velho = ImovelRepository.findById(id);
            Imovel novo = null;
            System.out.println("oi"+velho.getId());
            if (velho == null || velho.getId() != imovel.getId()) {
                return ImovelRepository.save(imovel);
            }
            novo = ImovelRepository.update(imovel);
            return novo;
        }

        public static boolean delete(Long imovelId) throws SQLException {
            if (existe(imovelId)) {
                return ImovelRepository.delete(imovelId);
            } else {
                return false;
            }

        }
/*
        public static boolean validacaoImovel(Imovel imovel) {
            boolean retorno = false;

            if(imovel.getDocumento().equals("") || imovel.getNome().equals("") || imovel.getTelefone().equals("")) {
                System.out.println("validaçao de campos.");
                return retorno = false;
            }
            else if (!validarCPF(imovel.getDocumento())){
                System.out.println("CPF inválido.");
                retorno = false;
            }
            else
                retorno = true;

            return retorno;
        }*/



    }


