package com.articlefetch.app.Busniess.Service;

import com.articlefetch.app.Busniess.Exceptions.AccountNotFoundException;
import com.articlefetch.app.Busniess.Exceptions.DuplicateEntryException;
import com.articlefetch.app.Controller.JacksonModels.Account;
import com.articlefetch.app.Controller.JacksonModels.AccountCreate;
import com.articlefetch.app.DataAccess.ModelDomain.AccountEntity;
import com.articlefetch.app.DataAccess.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for interfacing Hibernate data retrieval API for AccountEntity
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired AccountRepository accountRepository;


    @Transactional
    @Override
    public void createAccount(AccountCreate account) throws DuplicateEntryException {
        // Check if an article exists
        if(!accountRepository.findExistingConflicts(account.getUsername(), account.getPassword()).isEmpty()) {
            throw new DuplicateEntryException();
        }
        // If not picture is supplied then we will set it for the user
        if (account.getPath() == null) {
            account.setPath("/default_user.png");
        }
        accountRepository.save(Mapper.from(account));
    }

    @Override
    public Account getAccount(Integer account_id) throws AccountNotFoundException, IOException {
        AccountEntity entity = accountRepository.findById(account_id)
                .orElseThrow(() -> new AccountNotFoundException(account_id));

        return Mapper.from(entity);
    }

    @Override
    public List<Account> getAllAccounts() {
       List<AccountEntity> list = (List<AccountEntity>) accountRepository.findAll();
       Stream<Account> stream = list.stream().map( (account) -> {
           try {
               return Mapper.from(account);
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       });
       return stream.collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deactivateAccount(Integer id) throws AccountNotFoundException {
        accountRepository.setAccountStatus(0, id).orElseThrow(
                () -> new AccountNotFoundException(id));
    }

    @Transactional
    @Override
    public void reactivateAccount(Integer id) throws AccountNotFoundException {
        accountRepository.setAccountStatus(1, id).orElseThrow(
                () -> new AccountNotFoundException(id));
    }

    @Override
    public Account updateAccount(Integer id, AccountCreate accountCreate) throws AccountNotFoundException, IOException {
        AccountEntity entity = accountRepository.findById(id).orElseThrow( () -> new AccountNotFoundException(id));
        entity.setUsername(accountCreate.getUsername());
        entity.setPassword(accountCreate.getPassword());
        entity.setEmail(accountCreate.getEmail());
        entity.setStatus(entity.getStatus()); // We don' allow status updates here
        entity.setPath(accountCreate.getPath());
        entity.setLast_name(accountCreate.getLast_name());
        entity.setFirst_name(accountCreate.getFirst_name());
        accountRepository.save(entity);

        return Mapper.from(entity);
    }


}
