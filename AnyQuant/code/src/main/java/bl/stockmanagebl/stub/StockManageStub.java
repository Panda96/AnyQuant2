package bl.stockmanagebl.stub;

import blservice.stockmanageblservice.StockManageService;
import tool.constant.ResultMsg;
import tool.constant.StockAttribute;
import tool.constant.UserInfo;
import vo.chart.common.StockVO;

import java.util.*;

/**
 * Created by kylin on 16/3/6.
 */
public class StockManageStub implements StockManageService {

    public List<String> getUserCollections(UserInfo info) {
        if (info.getUserName().equals("test"))
            return new ArrayList<String>() {
                {
                    add("sh600000");
                    add("sh600004");
                    add("sh600005");
                    add("sh603017");
                    add("sh603889");
                }
            };
        else
            return new ArrayList<String>() {
                {
                    add("sh600000");
                    add("sh600004");
                }
            };
    }

    public List<StockVO> getUserStock(UserInfo info,String searchDate) {
    	List<HashMap<String, String>> days = new ArrayList<HashMap<String, String>>();
    	days.add( new HashMap<String, String>() {
            {
                put("high", String.valueOf(9.83));
                put("open", String.valueOf(9.79));
                put("close", String.valueOf(9.73));
            }
        });
    	
        days.add( new HashMap<String, String>() {
            {
                put("high", String.valueOf(9.82));
                put("open", String.valueOf(9.72));
                put("close", String.valueOf(9.72));
            }
        });
        
        days.add( new HashMap<String, String>() {
            {
                put("high", String.valueOf(19.82));
                put("open", String.valueOf(29.72));
                put("close", String.valueOf(39.72));
            }
        });
        
        days.add(new HashMap<String, String>() {
            {
                put("high", String.valueOf(29.82));
                put("open", String.valueOf(29.72));
                put("close", String.valueOf(49.72));
            }
        });
        
        String[] dates = new String[4];
        dates[0] = "2014-09-12";
        dates[1] = "2014-09-13";
        dates[2] = "2014-09-14";
        dates[3] = "2014-09-15";
        
        List<StockAttribute> atts = new ArrayList<StockAttribute>();
        for (int i = 0;i<4;i++){
        	 StockAttribute att = new StockAttribute(dates[i],days.get(i));
        }
        
       
        final StockVO vo1 = new StockVO("sh600000", "浦发银行", atts);
        
        return new ArrayList<StockVO>() {
            {
                add(vo1);
            }
        };
//        return null;
    }

    public ResultMsg addUserCollection(UserInfo info,String stockNum) {
        if (stockNum.equals("sh600000")&&info.getUserName().equals("plw"))
            return new ResultMsg(true);
        else
            return new ResultMsg(false, "股票不是sh600000,新增失败");
    }

    public ResultMsg deleteUserCollection(UserInfo info,String stockNum) {
        if (stockNum.equals("sh600000")&&info.getUserName().equals("plw"))
            return new ResultMsg(true);
        else
            return new ResultMsg(false, "股票不是sh600000,删除失败");
    }

	
}
