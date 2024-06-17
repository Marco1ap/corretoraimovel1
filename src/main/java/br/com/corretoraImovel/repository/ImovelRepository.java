package br.com.corretoraImovel.repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import br.com.corretoraImovel.model.Endereco;
import br.com.corretoraImovel.model.Imovel;
import br.com.corretoraImovel.model.Pessoa;
import br.com.corretoraImovel.model.Proprietario;
import jakarta.validation.Valid;

public class ImovelRepository extends Repository{

    private static List<Imovel> imovels = null;


    static {

        imovels = new ArrayList<>();

        Imovel v1 = new Imovel();

        imovels.add(v1);

    }

    public static List<Imovel> findAll(){
        return imovels;
    }

    //Consulta que chama banco de dados
    public static List<Imovel> consultaTodosimovels(){

        String sql = "SELECT * FROM Imovel";
        String sqlEndereco = "Select * FROM endereco where idendereco = ?";
        String SqlProprietario = "select * FROM proprietario where idproprietario";
        PreparedStatement ps = null;
        PreparedStatement psEnd = null;
        PreparedStatement psProp = null;
        ResultSet rs = null;
        ResultSet rsEndereco = null;
        ResultSet rsProprietario = null;

        ProprietarioRepository proprietarioR = new ProprietarioRepository();
        List<Imovel> imovels = new ArrayList<>();

        try {
            ps = getConnection().prepareStatement(sql);

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {

                while (rs.next()) {

                    Imovel imovel1 = new Imovel();
                    Endereco endereco = new Endereco();
                    Proprietario proprietario = new Proprietario();

                    //consultar imovel

                    imovel1.setId(rs.getLong("idimovel"));
                    endereco.setId(rs.getLong("idendereco"));
                    proprietario.setId(rs.getLong("idproprietario"));
                    imovel1.setProprietario(proprietario);
                    imovel1.setEndereco(endereco);
                    imovel1.setDormitorios(rs.getInt("dormitorios"));
                    imovel1.setValorAluguel(rs.getDouble("valor_aluguel"));
                    imovel1.setValorCondominio(rs.getDouble("valor_condominio"));
                    imovel1.setValorIptu(rs.getDouble("valor_iptu"));
                    imovel1.setValorTaxaIncendio(rs.getDouble("valor_taxa_incendio"));
                    endereco.setId(rs.getLong("idendereco"));
                    proprietario.setId(rs.getLong("idproprietario"));
                    imovel1.setProprietario(proprietario);
                    imovel1.setEndereco(endereco);

                    //consultar endereco
                    psEnd = getConnection().prepareStatement(sqlEndereco);
                    psEnd.setLong(1, imovel1.getEndereco().getId());

                    rsEndereco = psEnd.executeQuery();

                    if (rsEndereco.isBeforeFirst()) {

                        while (rsEndereco.next()) {
                            System.out.println("entro end"+ imovel1.getEndereco().getId());
                            endereco.setId(rsEndereco.getLong("idendereco"));
                            endereco.setBairro(rsEndereco.getString("Bairro"));
                            endereco.setLogradouro(rsEndereco.getString("Logradouro"));
                            endereco.setComplemento(rsEndereco.getString("complemento"));

                        }

                    }
                    //consulta proprietario
                    proprietario = proprietarioR.findById(proprietario.getId());
                    imovel1.setProprietario(proprietario);

                    imovel1.setEndereco(endereco);
                    imovel1.setProprietario(proprietario);
                    imovels.add(imovel1);
                }
            } else {
                System.out.println("Não foram encontrados registros na tabela do banco de dados.");
            }

        } catch (SQLException e) {
            System.out.println("Não foi possível consultar a listagem de Imovel: " + e.getMessage());
        }

        return imovels;
    }

    public static Imovel save(Imovel imovels) throws SQLException {

        String sql1 = "SELECT MAX (imovel.idimovel) FROM Imovel";

        String sql2 = "SELECT MAX (endereco.idendereco) FROM Endereco";

        String sql3 = "SELECT MAX (proprietario.idproprietario) FROM Proprietario";

        // @formatter:off
        String sql = "INSERT INTO imovel ("
                + "    idimovel,"
                + "    dormitorios,"
                + "    valor_aluguel,"
                + "    valor_condominio,"
                + "    valor_iptu,"
                + "    valor_taxa_incendio,"
                + "    idendereco,"
                + "    idproprietario"
                + ") VALUES ("
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?"
                + ")";

        String sqlEndereco = "INSERT INTO endereco ("
                + "    idendereco,"
                + "    logradouro,"
                + "    bairro,"
                + "    complemento"
                + ") VALUES ("
                + "    ?,"
                + "    ?,"
                + "    ?,"
                + "    ?"
                + ")";
        // @formatter:on

        Long nextID_from_seq_imovel= null;
        Long nextID_from_seq_endereco = null;
        Long nextID_from_seq_proprietario = null;
        CallableStatement cs = null;
        PreparedStatement pstmt = null;
        ProprietarioRepository proprietarioR = new ProprietarioRepository();
        Proprietario proprietarioImovel = new Proprietario();

        try {
            //encontrar ultimo id imovel
            pstmt = getConnection().prepareStatement(sql1);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                nextID_from_seq_imovel = rs.getLong(1);
            }

            //encontrar ultimo id endereco
            pstmt = getConnection().prepareStatement(sql2);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                nextID_from_seq_endereco = rs.getLong(1);
            }

            //encontrar ultimo id proprietario
            pstmt = getConnection().prepareStatement(sql3);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                nextID_from_seq_proprietario = rs.getLong(1);

            }

            //gravar endereco do imovel
            Endereco endereco = new Endereco();
            pstmt = getConnection().prepareStatement(sqlEndereco);

            pstmt.setLong(1, nextID_from_seq_endereco+1);
            pstmt.setString(2, imovels.getEndereco().getLogradouro());
            pstmt.setString(3, imovels.getEndereco().getBairro());
            pstmt.setString(4, imovels.getEndereco().getComplemento());

            pstmt.executeUpdate();

            // gravar proprietario
            proprietarioImovel = proprietarioR.save(imovels.getProprietario());
            imovels.setProprietario(proprietarioImovel);

            // gravar imovel
            pstmt = getConnection().prepareStatement(sql);
            pstmt.setLong(1, nextID_from_seq_imovel+1);
            pstmt.setInt(2, imovels.getDormitorios());
            pstmt.setDouble(3, imovels.getValorAluguel());
            pstmt.setDouble(4, imovels.getValorCondominio());
            pstmt.setDouble(5, imovels.getValorIptu());
            pstmt.setDouble(6, imovels.getValorTaxaIncendio());
            pstmt.setLong(7, nextID_from_seq_endereco+1);
            pstmt.setLong(8, imovels.getProprietario().getId());

            pstmt.executeUpdate();
            imovels.setId((long) nextID_from_seq_imovel+1);

            return imovels;

        } catch (SQLException e) {
            System.out.println("Erro para salvar o imovel no banco de dados: " + e.getMessage());
            getConnection().rollback();
        } finally {
            if (cs != null)
                try {
                    cs.close();
                } catch (SQLException e) {
                    System.out.println("Não foi possível fechar o Callable Statment: " + e.getMessage());
                }
        }

        return null;

    }

    public static boolean delete(Long imovelId) throws SQLException {

        Imovel imovel = null;
        String sql = "DELETE FROM imovel where idimovel = ?";
        String sqlEndereco = "DELETE FROM endereco where idimovel = ?";
        String sqlConsultaEndereco = "Select idendereco  from imovel where idimovel =?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Endereco endereco = new Endereco();

        imovel = findById(imovelId);

        if (imovel == null) {
            return false;
        }

        try {
            //consulta endereço
            getConnection().setAutoCommit(false);
            ps = getConnection().prepareStatement(sqlConsultaEndereco);

            ps.setLong(1, imovel.getId());

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {

                while (rs.next()) {
                    endereco.setId(rs.getLong("idendereco"));
                }
                imovel.setEndereco(endereco);
            }

            //deleta endereço primeiro



            //deleta imovel
            ps = getConnection().prepareStatement(sql);

            ps.setLong(1, imovelId);

            ps.executeUpdate();


            ps = getConnection().prepareStatement(sqlEndereco);

            ps.setLong(1, imovel.getEndereco().getId());

            ps.executeUpdate();
            getConnection().commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro para deletar o imovel no banco de dados: " + e.getMessage());
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

    public static Imovel findById(Long id) {
        String sql = "SELECT * FROM imovel where idimovel = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        ProprietarioRepository proprietarioR = new ProprietarioRepository();

        try {

            ps = getConnection().prepareStatement(sql);

            ps.setLong(1, id);

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                Imovel imovel = new Imovel();
                Proprietario proprietarioImovel = new Proprietario();
                Endereco enderecoImovel = new Endereco();

                while (rs.next()) {
                    imovel.setId(rs.getLong("IDIMOVEL"));
                    imovel.setDormitorios(rs.getInt("dormitorios"));
                    imovel.setValorAluguel(rs.getDouble("Valor_aluguel"));
                    imovel.setValorCondominio(rs.getDouble("Valor_condominio"));
                    imovel.setValorIptu(rs.getDouble("Valor_iptu"));
                    imovel.setValorTaxaIncendio(rs.getDouble("Valor_taxa_incendio"));
                    proprietarioImovel.setId(rs.getLong("idproprietario"));
                    enderecoImovel.setId(rs.getLong("idendereco"));

                    //consultar endereco
                    enderecoImovel = findEnderecoById(enderecoImovel.getId());
                    imovel.setEndereco(enderecoImovel);

                    //consulta proprietario
                    proprietarioImovel = proprietarioR.findById(proprietarioImovel.getId());
                    imovel.setProprietario(proprietarioImovel);
                }

                return imovel;
            }

        } catch (SQLException e) {
            System.out.println("Erro para consultar ao imovel no banco de dados: " + e.getMessage());
        } finally {

            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o Prepared Statment: " + e.getMessage());
                }

            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
                }

        }

        return null;
    }

    public static Endereco findEnderecoById(Long id) {
        String sql = "SELECT * FROM endereco where idendereco = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = getConnection().prepareStatement(sql);

            ps.setLong(1, id);

            rs = ps.executeQuery();

            if (rs.isBeforeFirst()) {
                Endereco endereco = new Endereco();
                while (rs.next()) {
                    endereco.setId(rs.getLong("idendereco"));
                    endereco.setBairro(rs.getString("bairro"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setLogradouro(rs.getString("logradouro"));
                }

                return endereco;
            }

        } catch (SQLException e) {
            System.out.println("Erro para consultar ao endereco no banco de dados: " + e.getMessage());
        } finally {

            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o Prepared Statment: " + e.getMessage());
                }

            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
                }

        }
        return null;

    }
    public static Imovel update(Imovel imovel) {

        String sql = "UPDATE Imovel  set dormitorios=?, valor_aluguel =? , valor_condominio= ?, valor_iptu= ? , valor_taxa_incendio= ? where idimovel =?";
        String sqlEndereco = "UPDATE Endereco set logradouro=?, bairro=?, complemento=? where idendereco =?";
        String sqlConsultaEndereco = "Select idendereco  from imovel where idimovel =?";
        String sqlConsultaProprietario = "Select idproprietario  from imovel where idimovel =?";

        CallableStatement cs = null;
        ResultSet rs = null;
        Long idEndereco = null;
        Long idProprietario = null;
        ProprietarioRepository proprietarioR = new ProprietarioRepository();

        try {
            getConnection().setAutoCommit(false);

            //localizar endereço por id
            cs = getConnection().prepareCall(sqlConsultaEndereco);

            cs.setLong(1, imovel.getId());

            rs = cs.executeQuery();

            if (rs.isBeforeFirst()) {

                while (rs.next()) {
                    idEndereco = rs.getLong("idendereco");
                }

            }
                cs = getConnection().prepareCall(sqlEndereco);

            cs.setString(1,imovel.getEndereco().getLogradouro());
            cs.setString(2,imovel.getEndereco().getBairro());
            cs.setString(3,imovel.getEndereco().getComplemento());
            cs.setLong(4,idEndereco);

            cs.executeUpdate();

            //localizar proprietario pelo id
            cs = getConnection().prepareCall(sqlConsultaProprietario);
            cs.setLong(1, imovel.getId());

            rs = cs.executeQuery();

            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    idProprietario = rs.getLong("idproprietario");
                }
            }
            //consulta proprietario

            Proprietario proprietarioImovel = imovel.getProprietario();
          //  proprietarioImovel = proprietarioR.findById(idProprietario);
            proprietarioImovel.setId(idProprietario);

            proprietarioImovel = proprietarioR.update(proprietarioImovel);
            imovel.setProprietario(proprietarioImovel);

            cs = getConnection().prepareCall(sql);

            cs.setInt(1, imovel.getDormitorios());
            cs.setDouble(2, imovel.getValorAluguel());
            cs.setDouble(3, imovel.getValorCondominio());
            cs.setDouble(4, imovel.getValorIptu());
            cs.setDouble(5,imovel.getValorTaxaIncendio());
            cs.setLong(6, imovel.getId());

            cs.executeUpdate();
            getConnection().commit();

            return imovel;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o imovel no banco de dados: " + e.getMessage());
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

