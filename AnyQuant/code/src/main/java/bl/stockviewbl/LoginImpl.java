package bl.stockviewbl;

import blservice.stockviewblservice.LoginBLService;
import data.factory.DataFactoryTxtImpl;
import dataservice.logindataservice.UserDataService;
import tool.constant.ResultMsg;

import java.io.IOException;

/**
 * Created by kylin on 16/3/4.
 */
public class LoginImpl implements LoginBLService{

    private UserDataService loginDataService;
    private DataFactoryTxtImpl dataFactory;
    
    public LoginImpl(){
    	dataFactory = DataFactoryTxtImpl.getInstance();
    	loginDataService = dataFactory.getUserDataService();
    }

    public ResultMsg logIn(String userName, String password) {
    	return loginDataService.login(userName, password);
    }

    public ResultMsg addUser(String userName, String password) {
        try {
            return loginDataService.addUser(userName, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ResultMsg(false,"异常错误^..^");
    }
}
