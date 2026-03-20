package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {

		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department \r\n" + "(Name)  \r\n" + "VALUES  \r\n" + "(?) ",
					Statement.RETURN_GENERATED_KEYS);// RETORNA O ID DO SELLER INSERIDO

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate(); // A VARIAVEL RECEBE O NUMERO DE LINHAS AFETADAS.

			if (rowsAffected > 0) {// SE CASO MAIOR QUE 0 FOI INSERIDO
				ResultSet rs = st.getGeneratedKeys();// RS ST PEGA O NUMERO DO SELLER INSERIDO
				if (rs.next()) {
					int id = rs.getInt(1);// ID RECEBE A POSIÇÃO 1
					obj.setId(id);// ATRIBUI O ID AO SELLER ID
				}

				DB.closeResultSet(rs);
			} else {
				throw new DbException("UNEXPECTED ERROR! NO ROWS AFFECTED!");

			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();// CHAMA A CONECÇÃO

			// COMANDO PARA ATUALIZAR O UPDATE LEMBRANDO QUE ESTE METODO RECEBE UM OBJ DE
			// SELLER
			st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ? ");// RETORNA O ID DO SELLER
																						// INSERIDO

			st.setString(1, obj.getName());// SET O NOME PARA ATUALIZAR

			st.setInt(2, obj.getId()); // ONDE O ID PARA MUDANÇA RECEBE ID

			st.executeUpdate(); // ATUALIZA

		} catch (SQLException e) {// SE CASO HOUVER ALGUN ERRO MOSTRA
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);// FECHA O STATEMENT
		}

	}

	@Override
	public void deletById(Integer id) {
		PreparedStatement st = null; // PREPARA PARA RECEBER O COMANDO SQL
		try {
			conn = DB.getConnection();
			// ST CHAMA A CONECÇÃO E ENVIA O COMANDO SQL
			st = conn.prepareStatement("delete from department where id = ?");
			st.setInt(1, id);// NESTE CASO O 1 RECEBE PARA DELETAR O ID
			int rows = st.executeUpdate(); // VARIAVEL RECEBE A LINHA AFETADA PARA DELETAR
			if (rows == 0) {
				throw new DbException("NÃO DELETADO, ID NÃO EXISTE");
			}

		} catch (SQLException e) {// SE CASO HOUVER ALGUN ERRO MOSTRA
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);// FECHA O STATEMENT
		}
	}

	@Override
	public Department findyId(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		try {
			st = conn.prepareStatement("select id, name from department where id= ? ");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {// RETORNA ULMA LISTA DEPARTMENT
		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		// INSTANCIA UMA LISTA PARA ARMAZENAR O RESULTSET COM OS DEPARTMENT
		List<Department> list = new ArrayList<>();
		try {
			st = conn.prepareStatement("select * from department order by Name");// SELECIONA E ORDENA POR NOME

			rs = st.executeQuery();
			while (rs.next()) {// ENQUANTO HOUVER DEPARTAMENTO
				Department obj = new Department();// INTANCIA O DEPARTAMASNTO COM OS CAMPOS
				obj.setId(rs.getInt("Id"));// CAMPO ID NO OBJETO IDD
				obj.setName(rs.getString("Name"));// CAMPO NOME NO OBJ NOME
				list.add(obj); // ADICIONA A LISTA
			}
			return list; // 4. RETORNA A LISTA COMPLETA
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}
}
