package data.logindata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dataservice.logindataservice.UserDataService;
import tool.constant.ResultMsg;
import tool.constant.UserInfo;

/**
 * Created by kylin on 16/3/3.
 */
public class UserDataTxtImpl implements UserDataService {

    private UserList userList;

    private String listPath;

    public UserDataTxtImpl() throws ClassNotFoundException {
        listPath = "bindata/user/userList.ser";
        userList = new UserList();
        //读取本地文件,初始化用户列表信息
        userList.readFile(listPath);
    }

    
    public ResultMsg login(String usrName, String password) {
        return userList.loginUser(new UserInfo(usrName, password));
    }

    public ResultMsg addUser(String userName, String password) throws ClassNotFoundException {
        return userList.addUser(new UserInfo(userName, password));
    }

    public List<String> getUserCollections(String userName, String password) {
        ResultMsg check = this.checkUser(userName, password);
        if (check.isPass())
            return this.userList.getUserCollection(userName);
        else
            return new ArrayList<>();
    }

    public ResultMsg addUserCollection(String userName, String password, String stockNumber) throws ClassNotFoundException {
        ResultMsg check = this.checkUser(userName, password);
        if (check.isPass()) {
            return this.userList.addUserCollection(userName, stockNumber);
        } else
            return check;
    }

    public ResultMsg deleteCollcetion(String userName, String password, String stockNumber) throws ClassNotFoundException {
        ResultMsg check = this.checkUser(userName, password);
        if (check.isPass())
            return this.userList.deleteUserCollection(userName, stockNumber);
        else
            return check;
    }


    private ResultMsg checkUser(String usrName, String password) {
        return userList.loginUser(new UserInfo(usrName, password));
    }


    public static void main(String[] args) throws ClassNotFoundException {
        UserDataTxtImpl dataTxt = new UserDataTxtImpl();
    }
}
