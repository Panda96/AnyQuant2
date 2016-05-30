package data.stub;

import tool.constant.ResultMsg;

/**
 * Created by Seven on 16/3/6.
 */
public class LoginDataStub {

    /**
     * 根据用户输入的用户名与密码返回登录结果
     *
     * @param userName 用户名称
     * @param password 用户密码
     * @return 登录信息
     */
    public ResultMsg login(String userName, String password) {

        if (userName.equals("test") && password.equals("666666"))
            return new ResultMsg(true);
        else
            return new ResultMsg(false, "用户名称不是test");
    }

    /**
     * 新增用户信息
     *
     * @param userName 用户名称
     * @param password 用户密码
     * @return 新增结果
     */
    public ResultMsg addUser(String userName, String password) {
        if (userName.equals("new"))
            return new ResultMsg(true);
        else
            return new ResultMsg(false, "同户名称不是new");
    }
}