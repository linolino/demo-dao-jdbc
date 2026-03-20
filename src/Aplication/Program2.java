package Aplication;

import java.util.Locale;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		Locale.setDefault(Locale.US);

		DepartmentDao departmetDao = DaoFactory.createDepartmentDao();
		
		
		System.out.println("=== TEST 1: department findById ===");
		Department department = departmetDao.findyId(1);
		System.out.println(department);
		
		
		
		
		sc.close();

	}

}
