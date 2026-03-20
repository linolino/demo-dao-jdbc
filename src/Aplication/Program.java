package Aplication;


import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		Locale.setDefault(Locale.US);

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
			

			System.out.println("\n=== TEST 4: seller insert ===");	
			Seller newseller = new Seller(null,"Greg","greg@gmail.com",new Date(),4000.00,department);
			sellerDao.insert(newseller);
			System.out.println("INSERTED! NEW ID= "+ newseller.getId());

			

			System.out.println("\n=== TEST 5: seller UPDATE ===");	
			seller = sellerDao.findyId(1);
			seller.setName("MARTHA WAINE");
			seller.setEmail("martha@gmail.com");
			
			sellerDao.update(seller);
			System.out.println("UPDATE! COMPLETED "+ seller);
			
			System.out.println("\n=== TEST 5: seller UPDATE ===");				
			System.out.println("Enter id for delete test: ");
			int id=sc.nextInt();
			sellerDao.deletById(id);
			System.out.println("DELETE COMPLETED");
			
			
			sc.close();
	}
}
