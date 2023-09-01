package gl.sidi.ebankingbackend.repositories;

import gl.sidi.ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepostory extends JpaRepository<Customer ,Long> {
}
