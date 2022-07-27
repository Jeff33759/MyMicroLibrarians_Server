package jeff.authentication.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jeff.authentication.model.po.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>{

	
}
