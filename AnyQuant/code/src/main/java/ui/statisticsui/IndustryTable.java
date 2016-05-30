package ui.statisticsui;

import bl.blfactory.BLFactory;
import blservice.statisticsblservice.IndustryViewService;
import blservice.stockviewblservice.StockViewService;
import javafx.scene.control.TableView;
import tool.enums.IndustryPeriodEnum;
import tool.exception.NotFoundException;
import ui.chartui.table.Table;
import vo.analyse.industry.IndustryVO;
import vo.chart.table.SingleLineVO;
import vo.chart.table.TableVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by Seven on 16/4/1.
 *
 * 一个行业的基本数据统计信息
 */
public class IndustryTable {

    private IndustryViewService industryViewService;

    private StockViewService stockViewService;

    public IndustryTable() throws IOException, NotFoundException {
        this.industryViewService = BLFactory.getInstance().getIndustryViewService();
        this.stockViewService = BLFactory.getInstance().getStockViewService();
    }

    public TableView<SingleLineVO> getIndustryTable(IndustryPeriodEnum period) throws NotFoundException {
        //获取行业名称
        HashMap<String,HashMap<String,String>> mapHashMap = stockViewService.getNamesAndNumbers();
        Set<String> nameOfIndustries = mapHashMap.keySet();

        //表格表头
        List<String> fields = new ArrayList<>();

        //表格每一行的数据
        List<SingleLineVO> lineVOs = new ArrayList<>();

        //对于一个行业,对应表格的一行
        for(String industryName:nameOfIndustries){
            //从逻辑层获取行业的数据
            IndustryVO industryVO = this.industryViewService.getBasicIndustryInfo(industryName,period);
            HashMap<String,String> lineData = industryVO.getKeyNameAndData();
            //添加这个行业对应的一行数据
            SingleLineVO lineVO = new SingleLineVO(lineData);
            lineVOs.add(lineVO);
            fields = industryVO.getDataNames();
        }
        TableVO tableVO = new TableVO(lineVOs,fields);
        //创建Table
        Table table = new Table();
        TableView<SingleLineVO> tableView=table.createTable(tableVO);
        return tableView;
    }
}
