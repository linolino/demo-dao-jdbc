package Aplication;

import java.util.Iterator;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJdbc;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("=== TEST 1: seller findById ===");
		Seller seller = sellerDao.findyId(3);
		System.out.println(seller);

		System.out.println("\n=== TEST 2: seller findById ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.fundByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
			
		}
			
			System.out.println("\n=== TEST 3: seller findById ===");
		
			 list = sellerDao.findAll();
			for (Seller obj : list) {
				System.out.println(obj);

		}

	}
}
