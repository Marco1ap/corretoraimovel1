package br.com.corretoraImovel.repository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import br.com.corretoraImovel.model.Pessoa;
import jakarta.validation.Valid;

public class VisitanteRepository extends Repository{
	
	private static List<Pessoa> visitantes = null;
	
	
	static {
		
		visitantes = new ArrayList<>();
		
		Pessoa p1 = new Pessoa("Clemerson","17011845602","999999990","email1@google.com");
		Pessoa p2 = new Pessoa("Lucas","19011545602","999999990","email2@google.com");
		
		visitantes.add(p1);
		visitantes.add(p2);
		
	}

	public static List<Pessoa> findAll(){
		return visitantes;
	}
	
	//Consulta que chama banco de dados
	public static List<Pessoa> consultaTodosVisitantes(){
		
			String sql = "SELECT * FROM PESSOA";

			PreparedStatement ps = null;

			ResultSet rs = null;

			List<Pessoa> visitantes = new ArrayList<>();

			try {
				ps = getConnection().prepareStatement(sql);

				rs = ps.executeQuery();

				if (rs.isBeforeFirst()) {
					while (rs.next()) {

						Pessoa pessoaVisitante = new Pessoa();

						pessoaVisitante.setId(rs.getLong("idpessoa"));
						pessoaVisitante.setNome(rs.getString("nome"));
						pessoaVisitante.setDocumento(rs.getString("cpf"));
						pessoaVisitante.setTelefone(rs.getString("telefone"));
						pessoaVisitante.setEmail(rs.getString("email"));



						visitantes.add(pessoaVisitante);
					}
				} else {
					System.out.println("Não foram encontrados registros na tabela do banco de dados.");
				}

			} catch (SQLException e) {
				System.out.println("Não foi possível consultar a listagem de visitantes: " + e.getMessage());
			}

			return visitantes;
	}
	
	public static Pessoa save(Pessoa visitante) {

		String sql1 = "SELECT MAX (idpessoa) FROM PESSOA";
		// @formatter:off
 		String sql = "INSERT INTO pessoa ("
 				+ "    idpessoa,"
				+ "    nome,"
				+ "    cpf,"
				+ "    telefone,"
				+ "    email"
				+ ") VALUES ("
				+ "    ?,"
				+ "    ?,"
				+ "    ?,"
				+ "    ?,"
				+ "    ?"
				+ ")";
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
			pstmt.setString(2, visitante.getNome());
			pstmt.setString(3, visitante.getDocumento());
			pstmt.setString(4, visitante.getTelefone());
			pstmt.setString(5, visitante.getEmail());

			
			pstmt.executeUpdate();
			
			visitante.setId((long) nextID_from_seq+1);
			
			return visitante;

		} catch (SQLException e) {
			System.out.println("Erro para salvar o visitante no banco de dados: " + e.getMessage());
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

	public static boolean delete(Long VisitanteId) throws SQLException {

		Pessoa pessoa = null;
		String sql = "DELETE FROM Pessoa where idpessoa = ?";
		PreparedStatement ps = null;

		pessoa = findById(VisitanteId);

		if (pessoa == null) {
			return false;
		}

		try {
			getConnection().setAutoCommit(false);
			ps = getConnection().prepareStatement(sql);

			ps.setLong(1, VisitanteId);

			ps.executeUpdate();
			getConnection().commit();

			return true;

		} catch (SQLException e) {
			System.out.println("Erro para deletar o visitante no banco de dados: " + e.getMessage());
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

	public static Pessoa findById(Long id) {
		String sql = "SELECT * FROM Pessoa where idpessoa = ?";

		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			ps = getConnection().prepareStatement(sql);

			ps.setLong(1, id);

			rs = ps.executeQuery();

			if (rs.isBeforeFirst()) {
				Pessoa visitante = new Pessoa();
				while (rs.next()) {
					visitante.setId(rs.getLong("IDPESSOA"));
					visitante.setNome(rs.getString("NOME"));
					visitante.setDocumento(rs.getString("CPF"));
					visitante.setTelefone(rs.getString("TELEFONE"));
					visitante.setEmail(rs.getString("EMAIL"));

				}

				return visitante;
			}

		} catch (SQLException e) {
			System.out.println("Erro para consultar o visitante no banco de dados: " + e.getMessage());
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

	public static Pessoa update(@Valid Pessoa visitante) {

		String sql = "UPDATE Pessoa set nome=?, cpf =? , telefone= ?, email= ? where idpessoa = ?";

		CallableStatement cs = null;

		try {
			getConnection().setAutoCommit(false);
			cs = getConnection().prepareCall(sql);

			cs.setString(1, visitante.getNome());
			cs.setString(2, visitante.getDocumento());
			cs.setString(3, visitante.getTelefone());
			cs.setString(4, visitante.getEmail());

			cs.setLong(5, visitante.getId());
			cs.executeUpdate();
			getConnection().commit();

			return visitante;

		} catch (SQLException e) {
			System.out.println("Erro ao atualizar o visitante no banco de dados: " + e.getMessage());
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
