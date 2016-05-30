package data.logindata;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tool.io.FileIOHelper;
import tool.ISDebug;
import tool.constant.ResultMsg;

import tool.constant.UserInfo;


/**
 * Created by kylin on 16/3/8.
 */
public class UserList implements Serializable {

    /**
     * 存储用户账户密码与收藏的股票
     */
    private List<UserInfo> userInfoList;

    /**
     * 本地文件路径
     */
    private String filePath;

    /**
     * 每次读取本地文件获取已经存储的用户信息
     *
     * @param listPath 本地文件路径
     */
    public void readFile(String listPath){

        if(ISDebug.isDebug)
            try {
                firstInit(listPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        filePath = listPath;
        try {
            userInfoList = (ArrayList<UserInfo>) FileIOHelper.readObject(listPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getUser(String name) {
        UserInfo user = null;
        for (int i = 0; i < this.userInfoList.size(); i++) {
            // user found
            if (userInfoList.get(i).getUserName().equals(name))
                return i;
        }
        return -1;
    }

    /**
     * 新增用户
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public ResultMsg addUser(UserInfo userInfo){
        if (this.getUser(userInfo.getUserName()) != -1)
            return new ResultMsg(false, "用户已经存在!");

        this.userInfoList.add(userInfo);

        //更新用户信息
        try {
            FileIOHelper.writeObject((Serializable) userInfoList, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            userInfoList = (List<UserInfo>) FileIOHelper.readObject(filePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResultMsg(true, "新增用户成功");
    }

    /**
     * 用户登录
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    public ResultMsg loginUser(UserInfo userInfo) {
        for (UserInfo user : this.userInfoList) {
            // userdatatest found
            if (user.getUserName().equals(userInfo.getUserName())) {
                String password = user.getPassword();
                if (password.equals(userInfo.getPassword()))
                    return new ResultMsg(true);
                else
                    return new ResultMsg(false, "密码错误");
            }
        }
        // can't find userdatatest
        return new ResultMsg(false, "用户不存在");
    }

    /**
     * 增加用户收藏股票
     *
     * @param userName 用户名称
     * @param stock    股票编号
     * @return 结果
     */
    public ResultMsg addUserCollection(String userName, String stock){
        int userIndex = this.getUser(userName);
        if (userIndex != -1) {
            ResultMsg resultMsg = this.userInfoList.get(userIndex).addCollection(stock);
            //更新用户信息
            try {
                FileIOHelper.writeObject((Serializable) userInfoList, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                userInfoList = (List<UserInfo>) FileIOHelper.readObject(filePath);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultMsg;
        } else
            return new ResultMsg(false, "user data not found");
    }

    /**
     * 删除用户股票信息
     *
     * @param userName 用户名
     * @param stock    股票编号
     * @return 结果
     */
    public ResultMsg deleteUserCollection(String userName, String stock) {
        int userIndex = this.getUser(userName);
        if (userIndex != -1) {
            ResultMsg resultMsg = this.userInfoList.get(userIndex).deleteCollection(stock);
            //更新用户信息
            try {
                FileIOHelper.writeObject((Serializable) userInfoList, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                userInfoList = (List<UserInfo>) FileIOHelper.readObject(filePath);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultMsg;
        } else
            return new ResultMsg(false, "userdatatest not found");
    }

    /**
     * 得到用户收藏股票列表
     *
     * @param userName 用户名
     * @return 股票列表
     */
    public List<String> getUserCollection(String userName) {
        int userIndex = this.getUser(userName);
        if (userIndex != -1) {
            return userInfoList.get(userIndex).getCollections();
        } else
            return null;
    }

    @Override
    public String toString() {
        String list = "";
        for (UserInfo user : this.userInfoList) {
            list += (user.toString());
        }
        return list;
    }

    /**
     * 系统数据第一次初始化方法
     *
     * @param path
     */
    private void firstInit(String path) throws IOException {
        this.userInfoList = new ArrayList<>();

        UserInfo user1 = new UserInfo("test", "test");
        user1.addCollection("sh600000");
        user1.addCollection("sh600015");
        user1.addCollection("sh600036");
        user1.addCollection("sh600151");
        user1.addCollection("sh600519");

        UserInfo user2 = new UserInfo("123", "123");
        user2.addCollection("sh600600");
        user2.addCollection("sh600714");

        UserInfo user3 = new UserInfo("666", "666");
        user3.addCollection("sh600779");

        userInfoList.add(user1);
        userInfoList.add(user2);
        userInfoList.add(user3);

        FileIOHelper.writeObject((Serializable) userInfoList, path);

    }

}
