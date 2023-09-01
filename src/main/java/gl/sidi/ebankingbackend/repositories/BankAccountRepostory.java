package gl.sidi.ebankingbackend.repositories;

import gl.sidi.ebankingbackend.entities.BankAccount;
import gl.sidi.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepostory extends JpaRepository<BankAccount,String> {
}
