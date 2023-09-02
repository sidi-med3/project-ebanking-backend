package gl.sidi.ebankingbackend.services;

import gl.sidi.ebankingbackend.entities.BankAccount;
import gl.sidi.ebankingbackend.entities.CurrentAccount;
import gl.sidi.ebankingbackend.entities.Customer;
import gl.sidi.ebankingbackend.entities.SavingAccount;
import gl.sidi.ebankingbackend.exceptions.BalanceNotSufficientException;
import gl.sidi.ebankingbackend.exceptions.BanckAccountNotFoundException;
import gl.sidi.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    CurrentAccount saveCurrentAccount(double initialBalance,double overDraft, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomers();
    BankAccount getBanckAccount(String accountId) throws BanckAccountNotFoundException;
    void debit(String accountId,double amount, String description) throws BanckAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId,double amount, String description) throws BanckAccountNotFoundException;
    void transfert(String accountIdSource,String accountIdDestination,double amount) throws BanckAccountNotFoundException, BalanceNotSufficientException;
}
