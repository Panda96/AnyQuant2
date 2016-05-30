package dataservice.logindataservice;

import tool.constant.ResultMsg;

import java.io.IOException;
import java.util.List;

/**
 * Created by kylin on 16/3/8.
 */
public interface UserDataService {
    /**
     * 根据用户输入的用户名与密码返回登录结果
     *
     * @param userName 用户名称
     * @param password 用户密码
     * @return 登录信息
     */
    ResultMsg login(String userName, String password);


    /**
     * 新增用户信息
     *
     * @param userName 用户名称
     * @param password 用户密码
     * @return 新增结果
     */
    ResultMsg addUser(String userName, String password) throws ClassNotFoundException;

    /**
     * 根据当前的用户信息返回用户收藏的股票编号列表
     *
     * @param userName 账户
     * @param password 密码
     * @return 用户收藏的股票编号列表
     */
    List<String> getUserCollections(String userName, String password);

    /**
     * 向某用户的账户股票个人收藏列表中添加一只股票
     *
     * @param userName 账户
     * @param password 密码
     * @return 添加收藏结果
     */
    ResultMsg addUserCollection(String userName, String password, String stockNumber) throws ClassNotFoundException;

    /**
     * 从某用户的账户股票个人收藏列表中删除一只股票
     *
     * @param userName 账户
     * @param password 密码
     * @return 删除收藏结果
     */
    ResultMsg deleteCollcetion(String userName, String password, String stockNumber) throws ClassNotFoundException;
}
