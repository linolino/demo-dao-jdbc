package model.dao;

import java.util.List;

import Aplication.Seller;
import model.entities.Department;

public interface SellerDao {
	void insert(Seller obj);

	void update(Seller obj);

	void deletById(Integer id);

	Seller findyId(Integer id);

	List<Seller> findAll();

}
