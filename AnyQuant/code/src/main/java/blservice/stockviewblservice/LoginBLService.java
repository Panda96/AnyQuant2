package blservice.stockviewblservice;

import tool.constant.ResultMsg;

/**
 * Created by kylin on 16/3/4.
 */
public interface LoginBLService {

    ResultMsg logIn(String userName, String password);

    ResultMsg addUser(String userName, String password);
}
