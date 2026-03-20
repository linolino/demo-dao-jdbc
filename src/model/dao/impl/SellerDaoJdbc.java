package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJdbc implements SellerDao {

	private Connection conn;

	public SellerDaoJdbc(Connection conn) {

		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;
		try {
			conn = DB.getConnection();

			// EXAMPLE 1:
			st = conn.prepareStatement("INSERT INTO seller \r\n"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)  \r\n" + "VALUES  \r\n" + "(?, ?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS);// RETORNA O ID DO SELLER INSERIDO

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBrithDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());// NEVEGAR NA TABELA DEPARTMENT E PEGAR ID DO DEPARTMENT

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
	public void update(Seller obj) {// NESTE METODO NÃO TEM O RESULTSET

		PreparedStatement st = null;
		try {
			conn = DB.getConnection();// CHAMA A CONECÇÃO

			// COMANDO PARA ATUALIZAR O UPDATE LEMBRANDO QUE ESTE METODO RECEBE UM OBJ DE
			// SELLER
			st = conn.prepareStatement("UPDATE seller  \r\n"
					+ "		SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?  \r\n"
					+ "		WHERE Id = ? ");// RETORNA O ID DO SELLER INSERIDO

			st.setString(1, obj.getName());//
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBrithDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());// NEVEGAR NA TABELA DEPARTMENT E PEGAR ID DO DEPARTMENT
			st.setInt(6, obj.getId()); // MUDA O VENDEDOR DE DEPARTAMENTO

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
		PreparedStatement st = null;

		try {
			conn = DB.getConnection();// CHAMA A CONECÇÃO

			// COMANDO PARA DELETAR
			st = conn.prepareStatement("DELETE FROM seller  \r\n" + "WHERE Id = ? ");// RETORNA O ID DO SELLER INSERIDO

			st.setInt(1, id);// NESTE CASO O PRIMEIRO ? VAIS RECEBER O ID

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
	public Seller findyId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName  \r\n" + "FROM seller INNER JOIN department  \r\n"
							+ "ON seller.DepartmentId = department.Id  \r\n" + "WHERE seller.Id = ? ");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBrithDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);// TODO Auto-generated method stub
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName  \r\n" + "FROM seller INNER JOIN department  \r\n"
							+ "ON seller.DepartmentId = department.Id \r\n" + "ORDER BY Name");

			// no caso aqui nao tem o id porque lista todos
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();// criase uma lista de Seller para que o while abastece com os
													// resultados
			// CRIASE UMA LIST MAP PARA RECEBER O INTEIROS DO DEPARTMENT SE CASO EXISTIR UM
			// ID O MAP NAO ADICIONA RETORNANDO NULL
			Map<Integer, Department> map = new HashMap<>();
			// ENQUANTO O RESULT SET NAO FOR NULO
			while (rs.next()) {
				// ADICIONA OS IDS DE DEPARTAMENT E VERIRFICA SE EXISTIR NA LISTA
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);// CHAMA O METODO QUE INSTANCIA O RESULTSET
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName  \r\n"
					+ "FROM seller INNER JOIN department  \r\n" + "ON seller.DepartmentId = department.Id \r\n"
					+ "WHERE DepartmentId = ? \r\n" + "ORDER BY Name  ");

			st.setInt(1, department.getId());// departament id da tabela department
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();// criase uma lista de Seller para que o while abastece com os
													// resultados
			// CRIASE UMA LIST MAP PARA RECEBER O INTEIROS DO DEPARTMENT SE CASO EXISTIR UM
			// ID O MAP NAO ADICIONA RETORNANDO NULL
			Map<Integer, Department> map = new HashMap<>();
			// ENQUANTO O RESULT SET NAO FOR NULO
			while (rs.next()) {
				// ADICIONA OS IDS DE DEPARTAMENT E VERIRFICA SE EXISTIR NA LISTA
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);// CHAMA O METODO QUE INSTANCIA O RESULTSET
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
