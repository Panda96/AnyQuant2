package vo.chart.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import po.StockPO;
import tool.utility.DateCount;
import tool.constant.StockAttribute;
import vo.analyse.ComputableStock;

/**
 * Created by kylin on 16/3/4. Modified by seven on 16/3/5
 */
public class StockVO implements ComputableStock{
	// 股票编号
	String number;
	// 股票名称
	String name;
	// 股票属性 (日期 --> (指标名称,指标数据))
	// HashMap<String,HashMap<String,String>> attribute;
	List<StockAttribute> attributes;

    private boolean sorted;

	 public StockVO(){
		 number = "";
		 name = "";
		 attributes = new ArrayList<>();
         sorted = false;
	 }

	/**
	 * 根据获得的数据构造StockVO
	 * 
	 * @param number
	 * @param name
	 * @param attributes
	 */
	public StockVO(String number, String name, List<StockAttribute> attributes) {
		this.number = number;
		this.name = name;
		this.attributes = attributes;
	}

	public static List<StockVO> toStockVOList(List<StockPO> POs, String beginDate, String endDate, String fields) {
		List<StockVO> vos = new ArrayList<StockVO>();
		if (POs != null && POs.size() > 0) {
			for (int i = 0; i < POs.size(); i++) {
				StockPO po = POs.get(i);
				StockVO vo = toStockVO(po, beginDate, endDate, fields);
				vos.add(vo);

			}
			if (vos.size() != 0) {
				return vos;
			}
		}
		return null;

	}

	public static StockVO toStockVO(StockPO po, String beginDate, String endDate, String fields) {
		List<StockAttribute> atts = new ArrayList<StockAttribute>();
		HashMap<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();
		if (beginDate.compareTo(endDate) <= 0) {
			info = po.getInfomation();
			// 选择的数据域
			String[] field = fields.split("\\+");
			
			// 获得筛选的日期取出对应的信息
			List<String> days = DateCount.splitDays(beginDate, endDate);
			for (int i = 0; i < days.size(); i++) {
				String day = days.get(i);
				HashMap<String, String> allatts = info.get(day);
				// 获得有效的日期的属性
				if (allatts==null) {
					continue;
				}
				HashMap<String, String> temp = new HashMap<String, String>();
				// 遍历筛选的字段，获得属性
				for (int j = 0; j < field.length; j++) {
					String key = field[j];
					String value = allatts.get(key);
//					System.out.println(value);
					if(value!=null){
						double d = Double.parseDouble(value);
						DecimalFormat df = new DecimalFormat("");
						if(key.equals("volume")){
							df = new DecimalFormat("0");
						}else{
							df = new DecimalFormat("0.00");
						}

						value = df.format(d);
					}

//					System.out.println("after"+value);
					temp.put(key, value);

//                    System.out.println(key+" "+value);
                }
				StockAttribute stockatt = new StockAttribute(day, temp);
				atts.add(stockatt);
			}

		}

		if (atts.size() == 0) {
//			System.out.println("今天是周末，不让人休息啦！！！");
		}
		return new StockVO(po.getNumber(), "", atts);

	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	/**
	 * 根据股票编号获取股票名称
	 * 
	 * @param number
	 * @return 股票名称
	 */
	public String getName(String number) {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回股票所有指标信息
	 * 
	 * @return
	 */
	public List<StockAttribute> getAttribute() {
		return attributes;
	}

	/**
	 * 设置股票的指标信息
	 * 
	 * @param attributes
	 */
	public void setAttribute(List<StockAttribute> attributes) {
		this.attributes = attributes;
	}

	public void show() {
		System.out.println(this.number + ":" + this.name);
		 for (StockAttribute att : this.attributes) {
		 System.out.println(att.getDate() + ":");
		 HashMap<String, String> single = att.getAttribute();
		 Iterator<Entry<String, String>> iter = single.entrySet().iterator();
		 while (iter.hasNext()) {
		 Entry entry = iter.next();
		 System.out.print(entry.getKey() + ":" + entry.getValue() + " ");
		 }
		 System.out.println();
		 }

	}

    @Override
    public int getNumberOfDays() {
        return this.attributes.size();
    }

    @Override
    public double getPriceAtDay(int dayIndex) {
        if(!this.sorted){
            this.attributes.sort((at1,at2) ->
                    at1.getDate().compareTo(at2.getDate()));
            this.sorted = true;
        }
        String price = this.attributes.get(dayIndex).getAttribute().get("close");
        return Double.parseDouble(price);
    }

    @Override
    public double getChangeAtDay(int dayIndex) {
        if(!this.sorted){
            this.attributes.sort((at1,at2) ->
                    at1.getDate().compareTo(at2.getDate()));
            this.sorted = true;
        }
        String strClose = this.attributes.get(dayIndex).getAttribute().get("close");
        String strOpen = this.attributes.get(dayIndex).getAttribute().get("open");
        double close = Double.parseDouble(strClose);
        double open = Double.parseDouble(strOpen);
        return close-open;
    }

    @Override
    public String getDateString(int dayIndex) {
        if(!this.sorted){
            this.attributes.sort((at1,at2) ->
                    at1.getDate().compareTo(at2.getDate()));
            this.sorted = true;
        }
        return this.attributes.get(dayIndex).getDate();
    }

}
