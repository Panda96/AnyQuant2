package tool.constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kylin on 16/3/20.
 */
public class UserInfo implements Serializable {
    /**
     * 用户名称
     */
    String userName;

    /**
     * 密码
     */
    String password;

    /**
     * 用户收藏股票编号列表
     */
    List<String> collections;

    public UserInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.collections = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getCollections() {
        return collections;
    }

    //methods to modify user's collection

    private boolean findCollection(String stockNumber){
        for(String stock:this.collections){
            if(stock.equals(stockNumber))
                return true;
        }
        return false;
    }
    public ResultMsg addCollection(String stockNumber){
        if(this.findCollection(stockNumber)){
            return new ResultMsg(false,"股票已经收藏!");
        }
        this.collections.add(stockNumber);
        return new ResultMsg(true,"新增收藏成功");
    }

    public ResultMsg deleteCollection(String stockNumber){
        if(!this.findCollection(stockNumber)){
            return new ResultMsg(false,"暂未收藏此股票!");
        }
        this.collections.remove(stockNumber);
        return new ResultMsg(true,"删除收藏成功");
    }

    @Override
    public String toString(){
        String collections = "";
        for (String stock:this.collections)
            collections+=(stock+";");
        return "userName: "+userName+" password: "+password+" collections:"+collections+"\n";
    }
}
