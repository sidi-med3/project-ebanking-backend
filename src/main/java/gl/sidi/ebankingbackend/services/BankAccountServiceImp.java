package gl.sidi.ebankingbackend.services;

import gl.sidi.ebankingbackend.entities.*;
import gl.sidi.ebankingbackend.enums.OperationType;
import gl.sidi.ebankingbackend.exceptions.BalanceNotSufficientException;
import gl.sidi.ebankingbackend.exceptions.BanckAccountNotFoundException;
import gl.sidi.ebankingbackend.exceptions.CustomerNotFoundException;
import gl.sidi.ebankingbackend.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class BankAccountServiceImp implements BankAccountService{
    private BankAccountRepostory bankAccountRepostory;
    private CustomerRepostory customerRepostory;
    private AccountOperationRepostory accountOperationRepostory;
    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("save new Customer");
        Customer cust=customerRepostory.save(customer);
        return cust;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepostory.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedSavingAccount=bankAccountRepostory.save(savingAccount);
        return savedSavingAccount;
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
       Customer customer=customerRepostory.findById(customerId).orElse(null);
       if (customer == null) throw new CustomerNotFoundException("Customer not found");
       CurrentAccount currentAccount = new CurrentAccount();
       currentAccount.setId(UUID.randomUUID().toString());
       currentAccount.setCreatedAt(new Date());
       currentAccount.setBalance(initialBalance);
       currentAccount.setOverDraft(overDraft);
       currentAccount.setCustomer(customer);
       CurrentAccount savedCurrentAccount=bankAccountRepostory.save(currentAccount);
       return savedCurrentAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return customerRepostory.findAll();
    }

    @Override
    public BankAccount getBanckAccount(String accountId) throws BanckAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepostory.findById(accountId)
                .orElseThrow(()->new BanckAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BanckAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount =getBanckAccount(accountId);
        if (bankAccount.getBalance()<amount) throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepostory.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepostory.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BanckAccountNotFoundException {
        BankAccount bankAccount =getBanckAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepostory.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepostory.save(bankAccount);

    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BanckAccountNotFoundException, BalanceNotSufficientException {
       debit(accountIdSource,amount,"transfert to "+accountIdDestination);
       credit(accountIdDestination,amount,"transfert from"+accountIdSource);
    }
}
