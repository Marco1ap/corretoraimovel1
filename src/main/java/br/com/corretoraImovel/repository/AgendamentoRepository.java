package br.com.corretoraImovel.repository;

import br.com.corretoraImovel.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoRepository extends Repository {

    private static List<Agendamento> agendamentos = null;


    static {
        agendamentos = new ArrayList<>();
    }

    public static List<Agendamento> findAll() {
        return agendamentos;
    }

    //Consulta que chama banco de dados
    public static List<Agendamento> consultaTodosAgendamentos() {

        String sql = "SELECT * FROM agendamento";
        String sqlPessoa = "Select * FROM pessoa where idpessoa= ?";
        String sqlImovel = "Select * FROM imovel where idimovel= ?";

        PreparedStatement ps = null;
        ImovelRepository imovelR = new ImovelRepository();
        ResultSet rs = null;
        ResultSet rsPessoa = null;
        ResultSet rsImovel = null;

        List<Agendamento> agendamentos = new ArrayList<>();

        try {
            ps = getConnection().prepareStatement(sql);

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {

                    Agendamento agendamento1 = new Agendamento();
                    Pessoa pessoa1 = new Pessoa();
                    Imovel imovel1 = new Imovel();

                    // Consultar Agendamento
                    pessoa1.setId(rs.getLong("idpessoa"));
                    imovel1.setId(rs.getLong("idimovel"));
                    agendamento1.setId(rs.getLong("idagendamento"));
                    agendamento1.setData(rs.getDate("data_visita").toLocalDate());
                    agendamento1.setHora(rs.getTime("horario").toLocalTime());
                    agendamento1.setPessoa(pessoa1);
                    agendamento1.setImovel(imovel1);

                    // Consultar Pessoa
                    ps = getConnection().prepareStatement(sqlPessoa);
                    ps.setLong(1, agendamento1.getPessoa().getId());


                    rsPessoa = ps.executeQuery();

                    if (rsPessoa.isBeforeFirst()) {

                        while (rsPessoa.next()) {

                            pessoa1.setId(rsPessoa.getLong("idpessoa"));
                            pessoa1.setNome(rsPessoa.getString("nome"));
                            pessoa1.setDocumento(rsPessoa.getString("cpf"));
                            pessoa1.setTelefone(rsPessoa.getString("telefone"));
                            pessoa1.setEmail(rsPessoa.getString("email"));
                        }
                    }
                    //consultar Imovel
                    ps = getConnection().prepareStatement(sqlImovel);
                    ps.setLong(1, agendamento1.getImovel().getId());

                    rsImovel = ps.executeQuery();

                    if (rsImovel.isBeforeFirst()) {

                        while (rsImovel.next()) {

                            imovel1 = imovelR.findById(agendamento1.getImovel().getId());
                        }
                    }
                    agendamento1.setPessoa(pessoa1);
                    agendamento1.setImovel(imovel1);
                    agendamentos.add(agendamento1);
                }
            } else {
                System.out.println("Não foram encontrados registros na tabela do banco de dados.");
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível consultar a listagem de agendamentos: " + e.getMessage());
        }

        return agendamentos;
    }


    public static Agendamento save(Agendamento agendamento) throws SQLException {

        String sqlConsultaAgendamento = "SELECT MAX (idagendamento) FROM agendamento";
        // @formatter:off
        String sqlAgendamento = "INSERT INTO agendamento ("
                + "    idagendamento,"
                + "    horario,"
                + "    data_visita,"
                + "    idpessoa,"
                + "    idimovel"
                + ") VALUES ("
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?"
                + ")";

        String sqlConsultaPessoa = "SELECT MAX (idpessoa) FROM pessoa";

        String sqlConsultaProprietario = "SELECT MAX (idproprietario) FROM proprietario";

        String sqlConsultaImovel = "SELECT MAX (idimovel) FROM Imovel";


        Long nextID_from_seq_agendamento = null;
        Long nextID_from_seq_pessoa = null;
        Long nextID_from_seq_imovel= null;
        Long nextID_from_seq_proprietario = null;
        PreparedStatement pstmt = null;

        ImovelRepository imovelR = new ImovelRepository();
        ProprietarioRepository proprietarioR = new ProprietarioRepository();
        VisitanteRepository visitanteR = new VisitanteRepository();

        try {
                getConnection().setAutoCommit(false);
                //encontrar ultimo id agendamento
                pstmt = getConnection().prepareStatement(sqlConsultaAgendamento);

                ResultSet rs = pstmt.executeQuery();

                if(rs.next()) {
                    nextID_from_seq_agendamento = rs.getLong(1);
                }

                //encontrar ultimo id pessoa
                pstmt = getConnection().prepareStatement(sqlConsultaPessoa);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    nextID_from_seq_pessoa = rs.getLong(1);
                }

                //encontrar ultimo id proprietario
                pstmt = getConnection().prepareStatement(sqlConsultaProprietario);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    nextID_from_seq_proprietario = rs.getLong(1);
                }

                //encontrar ultimo id imovel
                pstmt = getConnection().prepareStatement(sqlConsultaImovel);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    nextID_from_seq_proprietario = rs.getLong(1);
                }

                // incluir imovel
               Imovel imovelAgendamento = imovelR.save(agendamento.getImovel());


                // incluir pessoa
                Pessoa visitanteAgendamento = visitanteR.save(agendamento.getPessoa());


                //incluir agendamento
                pstmt = getConnection().prepareStatement(sqlAgendamento);

                pstmt.setLong(1, nextID_from_seq_agendamento + 1);
                pstmt.setTime(2, Time.valueOf(agendamento.getHora()));
                pstmt.setDate(3, Date.valueOf(agendamento.getData()));
                pstmt.setLong(4, agendamento.getPessoa().getId());
                pstmt.setLong(5, agendamento.getImovel().getId());

                pstmt.executeUpdate();
                getConnection().commit();
                agendamento.setId((long) nextID_from_seq_agendamento + 1);

                return agendamento;

        } catch (SQLException e) {
            System.out.println("Erro para salvar o agendamento no banco de dados: " + e.getMessage());
            getConnection().rollback();
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Não foi possível fechar o Callable Statment: " + e.getMessage());
                }
        }

        return null;

    }

    public static boolean delete(Long AgendamentoId)throws SQLException {

        Agendamento agendamento = null;
        String sql = "DELETE FROM Agendamento where idagendamento = ?";
        PreparedStatement ps = null;

        agendamento = findById(AgendamentoId);

        if (agendamento == null) {
            return false;
        }

        try {
                getConnection().setAutoCommit(false);
                ps = getConnection().prepareStatement(sql);

                ps.setLong(1, AgendamentoId);

                ps.executeUpdate();
                getConnection().commit();

                return true;

        } catch (SQLException e) {
            System.out.println("Erro para deletar o agendamento no banco de dados: " + e.getMessage());
            getConnection().rollback();
        } finally {

            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o Prepared Statment: " + e.getMessage());
                }
        }

        return false;
    }

    public static Agendamento findById(Long id) {
        String sql = "SELECT * FROM agendamento where idagendamento = ?";

        PreparedStatement ps = null;
        ResultSet rs  ;
        VisitanteRepository visitanteR = new VisitanteRepository();
        ImovelRepository imovelR = new ImovelRepository();

        try {

                ps = getConnection().prepareStatement(sql);

                ps.setLong(1, id);

                rs = ps.executeQuery();

                if (rs.isBeforeFirst()) {
                    Agendamento agendamento = new Agendamento();

                    while (rs.next()) {

                        Pessoa pessoa1 = new Pessoa();
                        Imovel imovel1 = new Imovel();

                        agendamento.setId(rs.getLong("idagendamento"));
                        agendamento.setData(rs.getDate("data_visita").toLocalDate());
                        agendamento.setHora(rs.getTime("horario").toLocalTime());
                        pessoa1.setId(rs.getLong("idpessoa"));
                        imovel1.setId(rs.getLong("idimovel"));

                        //consulta pessoa
                        pessoa1 = visitanteR.findById(pessoa1.getId());

                        //consulta imovel
                        imovel1 = imovelR.findById(imovel1.getId());

                        agendamento.setPessoa(pessoa1);
                        agendamento.setImovel(imovel1);

                    }

                    return agendamento;
                }

        } catch (SQLException e) {
            System.out.println("Erro para consultar o agendamento no banco de dados: " + e.getMessage());
          //  getConnection().rollback();
        } finally {

            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o Prepared Statment: " + e.getMessage());
                }

        }
        return null;
    }

    public static Agendamento update(Agendamento agendamento)throws SQLException {

        String sql = "UPDATE Agendamento set HORARIO=?, DATA_VISITA =? , IDPESSOA= ? , IDIMOVEL= ? where IDAGENDAMENTO = ?";

        CallableStatement cs = null;

        ImovelRepository imovelR = new ImovelRepository();
        VisitanteRepository visitanteR = new VisitanteRepository();

        try {
            getConnection().setAutoCommit(false);
            cs = getConnection().prepareCall(sql);

            cs.setTime(1, Time.valueOf(agendamento.getHora()));
            cs.setDate(2, Date.valueOf(agendamento.getData()));
            cs.setLong(3, agendamento.getPessoa().getId());
            cs.setLong(4, agendamento.getImovel().getId());

            cs.setLong(5, agendamento.getId());
            cs.executeUpdate();

            //alterar imovel
            Imovel imovelAgendamento = imovelR.update(agendamento.getImovel());

            //alterar visitante
            Pessoa visitanteAgendamento = visitanteR.update(agendamento.getPessoa());

            agendamento.setImovel(imovelAgendamento);
            agendamento.setPessoa(visitanteAgendamento);
            getConnection().commit();
            return agendamento;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o agendamento no banco de dados: " + e.getMessage());
            getConnection().rollback();
            if (cs != null)
                try {
                    cs.close();
                } catch (SQLException ex) {
                    System.out.println("Não foi possível fechar o Callable Statment: " + ex.getMessage());
                }
        }

        return null;
    }
}

