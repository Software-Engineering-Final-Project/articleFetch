package com.articlefetch.app.Busniess.Service;

import com.articlefetch.app.Busniess.Exceptions.AccountNotFoundException;
import com.articlefetch.app.Busniess.Exceptions.DuplicateEntryException;
import com.articlefetch.app.Controller.JacksonModels.Account;
import com.articlefetch.app.Controller.JacksonModels.AccountStatus;
import com.articlefetch.app.DataAccess.ModelDomain.AccountEntity;
import com.articlefetch.app.DataAccess.Repository.AccountRepository;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.GenericDeclaration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for interfacing Hibernate data retrieval API for AccountEntity
 */
@Service
public class AccountServiceImpl implements AccountService, Conversion<AccountEntity, Account> {

    @Autowired AccountRepository accountRepository;

    @Override
    public void createAccount(Account account) {
        // Check if an account exists
        if(!accountRepository.findExistingConflicts(account.username, account.password).isEmpty()) {
            throw new DuplicateEntryException();
        }
        accountRepository.save(convertToDAO(account));
    }

    @Override
    public Account getAccount(Integer account_id) {
         return convertToJackson( accountRepository.findById(account_id)
                 .orElseThrow(() -> new AccountNotFoundException(account_id)));
    }

    //TODO: UPDATE
    @Override
    public List<Account> getAllAccounts() {
       List<AccountEntity> list = (List<AccountEntity>) accountRepository.findAll();
       Stream<Account> stream = list.stream().map( (account) -> convertToJackson(account));
       return stream.collect(Collectors.toList());
    }

    @Override
    public void deactivateAccount(Integer id) {
        AccountEntity currentAccount = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException(id));
        currentAccount.setStatus(false);
        accountRepository.save(currentAccount);
    }

    @Override
    public void reactivateAccount(Integer id) {
        AccountEntity currentAccount = accountRepository.findById(id).orElseThrow(
                () -> new AccountNotFoundException(id));
        currentAccount.setStatus(true);
        accountRepository.save(currentAccount);
    }



    @Override
    public Account updateAccount(Integer id, Account account) {
        return null;
    }

    @Override
    public AccountEntity convertToDAO(Account obj) {
        AccountEntity entity = new AccountEntity();
        entity.setUsername(obj.username);
        entity.setPassword(obj.password);
        entity.setFirst_name(obj.first_name);
        entity.setLast_name(obj.last_name);
        entity.setStatus(obj.status);
        entity.setEmail(obj.email);
        entity.setPath(obj.path);
        return entity;
    }

    @Override
    public Account convertToJackson(AccountEntity obj) {
        return new Account(obj.getUsername(), obj.getPassword(), obj.getFirst_name(), obj.getLast_name(),
                obj.getEmail(),obj.getAccount_id(), obj.getPath(), obj.getStatus());
    }
}
