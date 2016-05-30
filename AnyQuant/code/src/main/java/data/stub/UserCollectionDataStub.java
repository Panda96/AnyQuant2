package data.stub;

import java.util.ArrayList;
import java.util.List;

import tool.constant.ResultMsg;
import tool.constant.UserInfo;

/**
 * Created by Seven on 16/3/6.
 */
public class UserCollectionDataStub {
    ArrayList<String> userCollections = new ArrayList<String>();
    /**
     * 根据当前的用户信息返回用户收藏的股票编号列表
     *
     * @param info 当前的用户信息
     * @return 用户收藏的股票编号列表
     */
    public List<String> getUserCollections(UserInfo info) {
         if (info.getUserName().equals("test")){
             userCollections.add("sh600000");
             userCollections.add("sh600004");
             userCollections.add("sh600005");
             userCollections.add("sh603017");
             userCollections.add("sh603889");
         }
        else{
             userCollections.add("sh600000");
             userCollections.add("sh600004");
         }
        return userCollections;
    }

    /**
     * 向某用户的账户股票个人收藏列表中添加一只股票
     *
     * @param info        用户的账户信息
     * @param stockNumber 欲收藏股票编号
     * @return 添加收藏结果
     */
    public ResultMsg addUserCollection(UserInfo info, String stockNumber) {

        if (info.getUserName().equals("test")){
            userCollections.add("sh6000001");
            return new ResultMsg(true);
        }
        else{
            return new ResultMsg();

        }
    }

    /**
     * 从某用户的账户股票个人收藏列表中删除一只股票
     *
     * @param info        用户的账户信息
     * @param stockNumber 欲删除股票编号
     * @return 删除收藏结果
     */
    public ResultMsg deleteCollcetion(UserInfo info, String stockNumber) {
        return new ResultMsg(userCollections.remove("sh600001"));
    }

}
