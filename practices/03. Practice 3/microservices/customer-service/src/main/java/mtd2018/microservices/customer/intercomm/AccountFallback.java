package mtd2018.microservices.customer.intercomm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import mtd2018.microservices.customer.model.Account;

@Component
public class AccountFallback implements AccountClient {

	@Override
	public List<Account> getAccounts(Integer customerId) {
		List<Account> acc = new ArrayList<Account>();
		return acc;
	}
	
}
