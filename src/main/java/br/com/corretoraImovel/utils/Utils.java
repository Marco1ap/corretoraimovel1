package br.com.corretoraImovel.utils;

public class Utils {

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
