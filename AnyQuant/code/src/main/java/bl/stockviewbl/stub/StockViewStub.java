package bl.stockviewbl.stub;

import blservice.stockviewblservice.StockViewService;
import tool.constant.ConditionSelect;
import tool.constant.StockAttribute;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kylin on 16/3/6.
 */
public class StockViewStub implements StockViewService {

    public List<StockVO> getAllStock(String year, String exchange, String date) {
//        if (year.equals("2014") && exchange.equals("sh")) {
//            final StockVO stockVO1 = new StockVO("sh600000", "浦发银行",
//                    new HashMap<String, HashMap<String, String>>());
//            final StockVO stockVO2 = new StockVO("sh600004", "不造",
//                    new HashMap<String, HashMap<String, String>>());
//            final StockVO stockVO3 = new StockVO("sh600005", "不造",
//                    new HashMap<String, HashMap<String, String>>());
//            final StockVO stockVO4 = new StockVO("sh603017", "不造",
//                    new HashMap<String, HashMap<String, String>>());
//
//            return new ArrayList<StockVO>() {
//                {
//                    add(stockVO1);
//                    add(stockVO2);
//                    add(stockVO3);
//                    add(stockVO4);
//                }
//            };
//        } else
            return new ArrayList<StockVO>();
    }
    
    
    public StockVO getStock(String name, String start, String end, String fields,List<ConditionSelect> ranges) {
        HashMap<String, String> day1 = new HashMap<String, String>() {
            {
                put("high", String.valueOf(9.83));
                put("open", String.valueOf(9.79));
                put("close", String.valueOf(9.73));
            }
        };
        HashMap<String, String> day2 = new HashMap<String, String>() {
            {
                put("high", String.valueOf(9.82));
                put("open", String.valueOf(9.72));
                put("close", String.valueOf(9.72));
            }
        };
        HashMap<String, String> day3 = new HashMap<String, String>() {
            {
                put("high", String.valueOf(19.82));
                put("open", String.valueOf(29.72));
                put("close", String.valueOf(39.72));
            }
        };
        HashMap<String, String> day4 = new HashMap<String, String>() {
            {
                put("high", String.valueOf(29.82));
                put("open", String.valueOf(29.72));
                put("close", String.valueOf(49.72));
            }
        };
        HashMap<String, HashMap<String, String>> hashMap = new HashMap<String, HashMap<String, String>>();
        String date1 = "2014-09-12";
        String date2 = "2014-09-13";
        String date3 = "2014-09-14";
        String date4 = "2014-09-15";
        hashMap.put(date1, day1);
        hashMap.put(date2, day2);
        hashMap.put(date3, day3);
        hashMap.put(date4, day4);
//        StockVO vo1 = new StockVO("sh600000", "浦发银行", hashMap);
        return null;
    }

    public List<StockVO> getAllBenchmark(String date) {
        return new ArrayList<StockVO>() {
            {
                add(new StockVO("hs300", "",new ArrayList<StockAttribute>()));
            }
        };
    }

    @Override
    public List<String> getAllBenches() {
        return null;
    }

    @Override
    public StockVO getBenchmark(String bench, String start, String end, String fields, List<ConditionSelect> ranges) throws NotFoundException, BadInputException {
        return null;
    }

    /**
     * 返回指定大盘的指定时间字段的信息
     *
     * @param bench  大盘代码
     * @param start  起点日期
     * @param end    终点日期
     * @param fields 股票信息字段
     * @return 大盘的指定时间字段的信息
     */
//    @Override
//    public StockVO getBenchmark(String bench, String startPrice, String end, String fields, List<ConditionSelect> ranges) throws NotFoundException {
//        return null;
//    }

    public StockVO getBenchmark(String bench, String start, String end, String fields) {
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
        
       
        final StockVO vo1 = new StockVO("hs300","",atts);
		return vo1;
    }

    public List<String> getAllFields() {
        return new ArrayList<String>() {
            {
                add("open");
                add("high");
                add("low");
                add("close");
                add("adj_price");
                add("volume");
                add("turnover");
                add("pe");
                add("pb");
            }
        };
    }

	@Override
	public List<String> getBenchFields() {
		return new ArrayList<String>() {
            {
                add("open");
                add("high");
                add("low");
                add("close");
                add("adj_price");
                add("volume");
            }
        };
	}
}
