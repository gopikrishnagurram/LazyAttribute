package com.wavelabs.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.util.xml.XmlDocument;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.wavelabs.metadata.ClassAttributes;
import com.wavelabs.metadata.HbmFileMetaData;
import com.wavelabs.metadata.XmlDocumentBuilder;
import com.wavelabs.model.Company;
import com.wavelabs.model.Employee;
import com.wavelabs.tableoperations.CRUDTest;
import com.wavelabs.utility.Helper;

/**
 * Performs unit tests on {@link PersistanceService} methods.
 * 
 * @author gopikrishnag
 *
 */
public class PersistanceServiceTest {

	private HbmFileMetaData companyHbm = null;
	private CRUDTest crud = null;
	private HbmFileMetaData empHbm = null;

	/**
	 * <p>
	 * Initializes {@link HbmFileMetaData}, {@link CRUDTest} Class objects. This
	 * objects useful through out all unit test cases.
	 * </p>
	 * 
	 */
	@BeforeTest
	public void intillization() {
		XmlDocumentBuilder xmb = new XmlDocumentBuilder();
		XmlDocument xd = xmb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Company.hbm.xml");
		companyHbm = new HbmFileMetaData(xd, Helper.getSessionFactory());
		crud = new CRUDTest(Helper.getSessionFactory(), Helper.getConfiguration(), Helper.getSession());
		xd = xmb.getXmlDocumentObject("src/main/resources/com/wavelabs/model/Employee.hbm.xml");
		empHbm = new HbmFileMetaData(xd, Helper.getSessionFactory());
	}

	/**
	 * Tests lazy attribute value in Company mappings.
	 * <ul>
	 * <li>{@code lazy="true"} test case will pass</li>
	 * <li>{@code lazy="false"} test case will fail</li>
	 * </ul>
	 * 
	 */
	@Test(priority = 1, description = "verifies lazy attribute value of Company mapping")
	public void testForLazyTrueIncomHbm() {
		Assert.assertEquals(companyHbm.getClassAttribute(ClassAttributes.lazy), "true",
				"lazy value of company mapping should be as true");
	}

	/**
	 * Tests lazy attribute value in Employee mappings.
	 * <ul>
	 * <li>{@code lazy="true"} test case will pass</li>
	 * <li>{@code lazy="false"} test case will fail</li>
	 * </ul>
	 * 
	 */
	@Test(priority = 2, description = "Verfies lazy attribute value of Employee mapping")
	public void testForLazyFalseInEmpHbm() {
		Assert.assertEquals(empHbm.getClassAttribute(ClassAttributes.lazy), "false",
				"lazy attribute value in employee mapping should be as false");
	}

	/**
	 * tests {@link PersistanceService#createCompany(int, String, int, String)}
	 * inserting records in table or not.
	 * <ul>
	 * <li>if record inserted successfully then test case will pass</li>
	 * <li>if record insertion failed then test case will fail
	 * <li>
	 * </ul>
	 */
	@Test(priority = 3, description = "Verifies CreateCompany(int, String, int, String) inserting record or not in table", dependsOnMethods = {
			"testForLazyTrueIncomHbm", "testForLazyFalseInEmpHbm" })
	public void testCreateCompany() {
		PersistanceService.createCompany(1, "Wave labs", 100, "Hitech");
		crud.setSession(Helper.getSession());
		Assert.assertEquals(crud.isRecordInserted(Company.class, 1), true,
				"createCompany(int, String, in, String) fails to Insert record in table");
	}

	/**
	 * Checks proxy loading is enabled or not for Company domain
	 */
	@Test(priority = 4, description = "Verifies the loadCompany(int) returning proxy or not.", dependsOnMethods = "testCreateCompany")
	public void testLoadComapny() {
		Company c1 = PersistanceService.loadCompany(1);
		boolean flag = false;
		if (c1.getClass().getName().contains("$")) {
			flag = true;
		}
		Assert.assertEquals(flag, true,
				"Please use load method and return proxy object, Don't use proxy object before use");
	}

	/**
	 * Test {@link PersistanceService#getNumberOfEmployeesOfCompany(int)} giving
	 * exact number of Employees or not.
	 * <ul>
	 * <li>if numberOfEmployees are as expected then test case will pass</li>
	 * <li>If numberOfEmployees are not matched then test case will fail</li>
	 * </ul>
	 * 
	 */
	@Test(priority = 5, description = "Verifies number of Employees for given Company is correct or not", dependsOnMethods = "testLoadComapny")
	public void testGetNumberOfEmployees() {
		int num = PersistanceService.getNumberOfEmployeesOfCompany(1);
		Assert.assertEquals(num, 100, " numberOfEmployees are not matching for given id");
	}

	@Test(priority = 6, description = "")
	public void testLoadEmployee() {
		createEmployee();
		Employee e = PersistanceService.getEmployee(1);
		boolean flag = false;
		if (!e.getClass().getName().contains("$"))
			flag = true;
		Assert.assertEquals(flag, true, " use lazy=\"false\" for Employee mappings.");
	}
	/**
	 * To persist objects of Employee, 
 	 */
	public void createEmployee() {
		Session session = Helper.getSession();
		Transaction tx = session.beginTransaction();
		Employee e1 = new Employee();
		e1.setId(1);
		e1.setName("Gopi krishna");
		e1.setSal(100000);
		session.save(e1);
		tx.commit();
	}

	/**
	 * Closing of SessionFactory
	 */
	@AfterTest(description = "closing of SessionFactory")
	public void closeResources() {
		try {
			Helper.getSessionFactory().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
