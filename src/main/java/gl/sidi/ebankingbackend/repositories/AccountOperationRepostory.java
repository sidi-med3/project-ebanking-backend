package gl.sidi.ebankingbackend.repositories;

import gl.sidi.ebankingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepostory extends JpaRepository<AccountOperation,Long> {
}
