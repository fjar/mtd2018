package mtd2018.microservices.customer.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mtd2018.microservices.customer.intercomm.AccountClient;
import mtd2018.microservices.customer.model.Account;
import mtd2018.microservices.customer.model.Customer;
import mtd2018.microservices.customer.model.CustomerType;

@RestController
public class Api {
	
	@Autowired
	private AccountClient accountClient;
	
	protected static Logger logger = LoggerFactory.getLogger(Api.class.getName());
	
	private List<Customer> customers;
	
	public Api() {
		customers = new ArrayList<>();
		customers.add(new Customer(1, "301110001A", "Ramon Garcia", CustomerType.INDIVIDUAL));
		customers.add(new Customer(2, "301110002B", "Isabel Sevilla", CustomerType.INDIVIDUAL));
		customers.add(new Customer(3, "301110003C", "Paco Perez", CustomerType.INDIVIDUAL));
		customers.add(new Customer(4, "301110004D", "Ernesto Cervera", CustomerType.INDIVIDUAL));
	}
	
	@RequestMapping("/customers/dni/{dni}")
	public Customer findByDNI(@PathVariable("dni") String dni) {
		logger.info(String.format("Customer.findByDNI(%s)", dni));
		Customer c = customers.stream().filter(it -> it.getDNI().equals(dni)).findFirst().get();
		logger.info(String.format("Customer.findByDNI: %s", c));
		return c;
	}
	
	@RequestMapping("/customers")
	public List<Customer> findAll() {
		logger.info("Customer.findAll()");
		logger.info(String.format("Customer.findAll: %s", customers));
		return customers;
	}
	
	@RequestMapping("/customers/{id}")
	public Customer findById(@PathVariable("id") Integer id) {
		logger.info(String.format("Customer.findById(%s)", id));
		Customer customer = customers.stream().filter(it -> it.getId().intValue()==id.intValue()).findFirst().get();
		List<Account> accounts =  accountClient.getAccounts(id);
		customer.setAccounts(accounts);
		logger.info(String.format("Customer.findById: %s", customer));
		return customer;
	}
	
}
