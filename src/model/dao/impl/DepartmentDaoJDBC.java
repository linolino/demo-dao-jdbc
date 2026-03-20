package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Department obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletById(Integer id) {
		// TODO Auto-generated method stub

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
	public List<Department> findAll() {//RETORNA ULMA LISTA DEPARTMENT
		PreparedStatement st = null;
		ResultSet rs = null;// resultado do consulta
		// INSTANCIA UMA LISTA PARA ARMAZENAR O RESULTSET COM OS DEPARTMENT
		List<Department> list = new ArrayList<>();
		try {
			st = conn.prepareStatement("select * from department order by Name");//SELECIONA E ORDENA POR NOME

			rs = st.executeQuery();
			 while (rs.next()) {//ENQUANTO HOUVER DEPARTAMENTO
		            Department obj = new Department();//INTANCIA O DEPARTAMASNTO COM OS CAMPOS 
		            obj.setId(rs.getInt("Id"));//CAMPO ID NO OBJETO IDD
		            obj.setName(rs.getString("Name"));//CAMPO NOME NO OBJ  NOME
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
