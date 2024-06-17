package br.com.corretoraImovel.repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import br.com.corretoraImovel.model.Pessoa;
import br.com.corretoraImovel.model.Proprietario;
import jakarta.validation.Valid;

public class ProprietarioRepository extends Repository{
	
	private static List<Proprietario> proprietarios = null;
	
	
	static {
		
		proprietarios = new ArrayList<>();
		
		Proprietario p1 = new Proprietario("Clemerson","17011845602","999999990","email1@google.com");
		Proprietario p2 = new Proprietario("Lucas","19011545602","999999990","email2@google.com");
		
		proprietarios.add(p1);
		proprietarios.add(p2);
		
	}

	public static List<Proprietario> findAll(){
		return proprietarios;
	}
	
	//Consulta que chama banco de dados
	public static List<Proprietario> consultaTodosProprietarios(){
		
			String sql = "SELECT * FROM proprietario";

			PreparedStatement ps = null;

			ResultSet rs = null;

			List<Proprietario> proprietarios = new ArrayList<>();

			try {
				ps = getConnection().prepareStatement(sql);

				rs = ps.executeQuery();

				if (rs.isBeforeFirst()) {

					while (rs.next()) {

						Proprietario pessoaProprietario = new Proprietario();

						pessoaProprietario.setId(rs.getLong("idproprietario"));
						pessoaProprietario.setNome(rs.getString("nome"));
						pessoaProprietario.setDocumento(rs.getString("cpf"));
						pessoaProprietario.setTelefone(rs.getString("telefone"));
						pessoaProprietario.setEmail(rs.getString("email"));

						proprietarios.add(pessoaProprietario);
					}
				} else {
					System.out.println("Não foram encontrados registros na tabela do banco de dados.");
				}

			} catch (SQLException e) {
				System.out.println("Não foi possível consultar a listagem de proprietarios: " + e.getMessage());
			}

			return proprietarios;
	}
	
	public static Proprietario save(Proprietario proprietario) {

		String sql1 = "SELECT MAX (idproprietario) FROM proprietario";
		// @formatter:off
 		String sql = "INSERT INTO proprietario ("
 				+ "    idproprietario,"
				+ "    nome,"
				+ "    cpf,"
				+ "    email,"
				+ "    telefone"
				+ ") VALUES ("
				+ "    ?,"
				+ "    ?,"
				+ "    ?,"
				+ "    ?,"
				+ "    ? )";
 		// @formatter:on
 		
 		Long nextID_from_seq = null;
		CallableStatement cs = null;
		PreparedStatement pstmt = null; 
	
		try {
			pstmt = getConnection().prepareStatement(sql1);

			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				nextID_from_seq = rs.getLong(1);
			
			}
			
			pstmt = getConnection().prepareStatement(sql);

			pstmt.setLong(1, nextID_from_seq+1);
			pstmt.setString(2, proprietario.getNome());
			pstmt.setString(3, proprietario.getDocumento());
			pstmt.setString(4, proprietario.getTelefone());
			pstmt.setString(5, proprietario.getEmail());

			
			pstmt.executeUpdate();
			
			proprietario.setId((long) nextID_from_seq+1);
			
			return proprietario;

		} catch (SQLException e) {
			System.out.println("Erro para salvar o proprietario no banco de dados: " + e.getMessage());
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

	public static boolean delete(Long ProprietarioId) {

		Proprietario proprietario = null;
		String sql = "DELETE FROM Proprietario where idproprietario = ?";
		PreparedStatement ps = null;

		proprietario = findById(ProprietarioId);

		if (proprietario == null) {
			return false;
		}

		try {
			ps = getConnection().prepareStatement(sql);

			ps.setLong(1, ProprietarioId);

			ps.executeUpdate();

			return true;

		} catch (SQLException e) {
			System.out.println("Erro para deletar o proprietario no banco de dados: " + e.getMessage());
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

	public static Proprietario findById(Long id) {
		String sql = "SELECT * FROM Proprietario where idproprietario = ?";

		PreparedStatement ps = null;

		ResultSet rs = null;

		try {
			getConnection().setAutoCommit(false);
			ps = getConnection().prepareStatement(sql);

			ps.setLong(1, id);

			rs = ps.executeQuery();
			System.out.println("pro "+id);
			if (rs.isBeforeFirst()) {
				Proprietario proprietario = new Proprietario();
				while (rs.next()) {
					proprietario.setId(rs.getLong("idproprietario"));
					proprietario.setNome(rs.getString("nome"));
					proprietario.setDocumento(rs.getString("cpf"));
					proprietario.setEmail(rs.getString("email"));
					proprietario.setTelefone(rs.getString("telefone"));

				}
				getConnection().commit();
				return proprietario;
			}

		} catch (SQLException e) {
			System.out.println("Erro para consultar o proprietario no banco de dados: " + e.getMessage());
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

	public static Proprietario update(Proprietario proprietario) {

		String sql = "UPDATE PROPRIETARIO set NOME=?, CPF =? , TELEFONE= ?, EMAIL= ? where IDPROPRIETARIO = ?";

		CallableStatement cs = null;

		try {
			getConnection().setAutoCommit(false);
			cs = getConnection().prepareCall(sql);

			cs.setString(1, proprietario.getNome());
			cs.setString(2, proprietario.getDocumento());
			cs.setString(3, proprietario.getTelefone());
			cs.setString(4, proprietario.getEmail());

			cs.setLong(5, proprietario.getId());
			cs.executeUpdate();
			getConnection().commit();

			return proprietario;

		} catch (SQLException e) {
			System.out.println("Erro ao atualizar o proprietario no banco de dados: " + e.getMessage());
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
