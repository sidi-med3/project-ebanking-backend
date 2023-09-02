package gl.sidi.ebankingbackend;

import gl.sidi.ebankingbackend.entities.*;
import gl.sidi.ebankingbackend.enums.AccountStatus;
import gl.sidi.ebankingbackend.enums.OperationType;
import gl.sidi.ebankingbackend.exceptions.BalanceNotSufficientException;
import gl.sidi.ebankingbackend.exceptions.BanckAccountNotFoundException;
import gl.sidi.ebankingbackend.exceptions.CustomerNotFoundException;
import gl.sidi.ebankingbackend.repositories.AccountOperationRepostory;
import gl.sidi.ebankingbackend.repositories.BankAccountRepostory;
import gl.sidi.ebankingbackend.repositories.CustomerRepostory;
import gl.sidi.ebankingbackend.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {
    @Autowired
    private BankAccountRepostory bankAccountRepostory;
    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Hassan", "Mohamed", "Toutou").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);

            });

            bankAccountService.listCustomers().forEach(customer ->{
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*1500, 5.5,customer.getId());
                    List<BankAccount> bankAccountList= bankAccountRepostory.findAll();
                    for (BankAccount bankAccount:bankAccountList){
                        bankAccountService.credit(bankAccount.getId(),10000+Math.random()*120000,"credit");
                        bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000,"Debit");
                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BanckAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
        };
}
    //@Bean
    CommandLineRunner start(AccountOperationRepostory accountOperationRepostory,
                            BankAccountRepostory accountRepostory,
                            CustomerRepostory customerRepostory) {
        return  args -> {
            Stream.of("Hassan","Mohamed","Toutou").forEach(name ->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepostory.save(customer);
            });
           customerRepostory.findAll().forEach(cust ->{
               CurrentAccount currentAccount=new CurrentAccount() ;
               currentAccount.setId(UUID.randomUUID().toString());
               currentAccount.setBalance(Math.random()*90000);
               currentAccount.setCreatedAt(new Date());
               currentAccount.setAccountStatus(AccountStatus.CREATED);
               currentAccount.setOverDraft(9000);
               currentAccount.setCustomer(cust);
             accountRepostory.save(currentAccount);
               SavingAccount savingAccount=new SavingAccount();
               savingAccount.setId(UUID.randomUUID().toString());
               savingAccount.setBalance(Math.random()*90000);
               savingAccount.setCreatedAt(new Date());
               savingAccount.setAccountStatus(AccountStatus.CREATED);
               savingAccount.setInterestRate(5.5);
               savingAccount.setCustomer(cust);
               accountRepostory.save(savingAccount);


           });
            accountRepostory.findAll().forEach(acc ->{
                for (int i=0;i<10;i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setAmount(Math.random()*1000);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setBankAccount(acc);
                    accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                    accountOperationRepostory.save(accountOperation);
                }
            });
        };
    }
}
