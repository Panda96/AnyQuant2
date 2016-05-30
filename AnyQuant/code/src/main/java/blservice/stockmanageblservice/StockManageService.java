package blservice.stockmanageblservice;

import tool.constant.UserInfo;
import tool.constant.ResultMsg;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.util.List;

/**
 * Created by kylin on 16/3/4.
 */
public interface StockManageService {

    /**
     * 返回用户收藏的所有股票名称列表
     *
     * @param info 用户信息
     * @return 用户收藏的所有股票名称列表
     */
    List<String> getUserCollections(UserInfo info);

    /**
     * 返回已经登录的用户收藏的所有股票的信息
     *
     * @return 用户收藏的所有股票的信息
     */
    List<StockVO> getUserStock(UserInfo info,String searchDate) throws NotFoundException;

    /**
     * 向已经登录的用户收藏列表中添加股票
     *
     * @param stockNum 股票代码
     * @return 添加结果
     */
    ResultMsg addUserCollection(UserInfo info,String stockNum);

    /**
     * 从已经登录的用户收藏列表中删除股票
     *
     * @param stockNum 股票代码
     * @return 删除结果
     */
    ResultMsg deleteUserCollection(UserInfo info,String stockNum);
}
