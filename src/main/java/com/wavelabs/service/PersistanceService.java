package com.wavelabs.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.wavelabs.model.Company;
import com.wavelabs.model.Employee;
import com.wavelabs.utility.Helper;

/**
 * Persist Company and provides methods to get persisted {@link Company} and
 * {@link Employee} records.
 * 
 * @author gopikrishnag
 */
public class PersistanceService {
	/**
	 * <h3>persist Company domain object</h3>
	 * <ul>
	 * <li>Creates Transient Company</li>
	 * <li>sets properties of Company</li>
	 * <li>persist Company</li>
	 * </ul>
	 * 
	 * @param id
	 *            of Company
	 * @param name
	 *            of Company
	 * @param numberOfEmployees
	 *            in Company
	 * @param area
	 *            of Company
	 */
	public static void createCompany(int id, String name, int numberOfEmployees, String area) {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Company c1 = new Company();
		c1.setId(id);
		c1.setName(name);
		c1.setArea(area);
		c1.setNumberOfEmployees(numberOfEmployees);
		session.save(c1);
		tx.commit();
		session.close();
	}

	/**
	 * <h3>Returns proxy object of Company</h3>
	 * <ul>
	 * <li>loads given Company for given id</li>
	 * <li>return back the Company proxy</li>
	 * </ul>
	 * 
	 * @param id
	 *            of Company
	 * @return Company
	 */
	public static Company loadCompany(int id) {
		Session session = Helper.getSession();
		Company c1 = (Company) session.load(Company.class, id);
		session.close();
		return c1;
	}

	/**
	 * <h3>Loads Company from table with matching id</h3>
	 * <ul>
	 * <li>Load Company from table with matching id</li>
	 * <li>return number of employees of Company</li>
	 * </ul>
	 * 
	 * @param id
	 *            of Company
	 * @return numberOfEmployees
	 */
	public static int getNumberOfEmployeesOfCompany(int id) {
		Session session = Helper.getSession();
		Company c1 = (Company) session.get(Company.class, id);
		return c1.getNumberOfEmployees();
	}

	/**
	 * <p>
	 * Get Employee record for give id
	 * </p>
	 * 
	 * @param id
	 *            of Employee
	 * @return {@linkplain Employee}
	 */
	public static Employee getEmployee(int id) {
		Session session = Helper.getSession();
		Employee e1 = (Employee) session.get(Employee.class, id);
		session.close();
		return e1;
	}
}
