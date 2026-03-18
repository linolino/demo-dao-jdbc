package Aplication;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.entities.Department;

public class Program {

	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Department obj = new Department(1,"Books");
		
		Seller seller =new Seller(1,"Paulo","paulo@gmail.com",new Date(),3000.00,obj);
				
				
				

		System.out.println(obj);
		System.out.println(seller);
	}

}
