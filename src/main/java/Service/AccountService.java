package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    private AccountDAO AccountDAO;
  
    public AccountService(){
        AccountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO AccountDAO){
        this.AccountDAO = AccountDAO;
       }

    public Account login(Account accountInfo){
        return AccountDAO.login(accountInfo);
    }

    public Account register(Account accountInfo){
        return AccountDAO.register(accountInfo);
    }
}
