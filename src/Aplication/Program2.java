package Aplication;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class Program2 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		Locale.setDefault(Locale.US);

		DepartmentDao departmetDao = DaoFactory.createDepartmentDao();
		
		
		System.out.println("=== TEST 1: department findById ===");
		Department dep = departmetDao.findyId(1);
		System.out.println(dep);
		///////////////////////////////////////////////////////////////
		System.out.println("\n=== TEST 2: department findById ===");
		
		List<Department> list = departmetDao.findAll();
		for (Department obj : list) {
			System.out.println(obj);
			
		}
		
		
		
		sc.close();

	}

}
