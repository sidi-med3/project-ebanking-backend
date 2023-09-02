package gl.sidi.ebankingbackend.dtos;

import gl.sidi.ebankingbackend.enums.AccountStatus;
import lombok.Data;
import java.util.Date;
@Data
public class SavingBankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double interestRate;
}
