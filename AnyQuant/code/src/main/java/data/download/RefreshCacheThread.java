package data.download;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.factory.DataFactoryTxtImpl;
import data.factory.DataFactoryUrlImpl;
import tool.io.FileIOHelper;
import data.stockdata.sinastock.SinaStockData;
import data.stockdata.sinastock.SinaStockDataService;
import dataservice.stockdataservice.APIDataService;
import dataservice.stockdataservice.CacheDataService;
import po.StockPO;
import tool.utility.DateCount;
import tool.exception.NotFoundException;

/**
 * Created by kylin on 16/3/7.
 */

public class RefreshCacheThread extends Thread {

    private SinaStockDataService sinaStockDataService;

    private CacheDataService cacheDataService;

    private DownloadDataService downLoadDataService;

    private APIDataService apiDataService;

    private final String startDay = "2006-01-01";

    /**
     * 本小组选取的目标股票
     */
    private List<String> stocksAndName;

    public RefreshCacheThread() throws IOException {
        sinaStockDataService = new SinaStockData();

        downLoadDataService = new DownloadDataTxt();

        apiDataService = DataFactoryUrlImpl.getInstance().getAPIDataService();

        cacheDataService = DataFactoryTxtImpl.getInstance().getCacheDataService();

        stocksAndName = new ArrayList<>();
        this.getSelectedStocks();
    }

    public void run() {
        try {
            //fresh or not
            boolean shouldRefresh = refreshOrNot();

            if (shouldRefresh)
                this.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("刷新成功结束");
    }

    public boolean refreshOrNot() throws NotFoundException, IOException {
        StockPO po = cacheDataService.getBenchmark("hs300");
        //已经缓存的最新日期
        String latestDate = po.getLatestDate();
        //最新的工作日期
        String lastedWorkDay = DateCount.getRecentWorkDay();
        return latestDate.compareTo(lastedWorkDay) < 0;
    }

    private void getSelectedStocks() throws IOException {

        ArrayList<String> selectedStocks = FileIOHelper.readTxtFileLines("cache/selectedStocks.txt");

        for (String line : selectedStocks) {
            //stock
            if (line.startsWith("s")) {
//                System.out.println(line);
                this.stocksAndName.add(line);
                //行业
            } else {
//                System.out.println("行业:" + line);
            }
        }

    }

    public void refreshFields() throws IOException {
        downLoadDataService.downloadFields();
    }

    public void refreshStock() throws IOException {

        //download stock info
        String namesAndStocks = "";

        int numbers = stocksAndName.size();
        double count = 0;
        System.out.println("number of stock :" + numbers);
        System.out.println("downloading------------------------");

        for (String stock : stocksAndName) {
            DecimalFormat decimalFormat = new DecimalFormat("00.0");
            String percent = decimalFormat.format((++count) / numbers * 100) + "%";
            System.out.println(percent);
            String[] split = stock.split(",");
            //download stock info
            downLoadDataService.downloadStock(split[0], startDay, DateCount.dateToStr(new Date()));
            //download stock name
            String name = split[1];
            namesAndStocks += (split[0] + ":" + name + ";");
        }

        FileIOHelper.creatTxtFile(namesAndStocks, "cache/cacheStockInfo.txt");

    }

    public void refreshBenchmark() throws IOException {
        //down load benchmark number
        downLoadDataService.downloadBenchmarkNumber();
        //down load benchmark info
        List<String> stockNumList = apiDataService.getAllBenchmark();
        for (String stock : stockNumList) {
            // System.out.println(stock);
            downLoadDataService.downloadBenchmark(stock, startDay, DateCount.dateToStr(new Date()));
        }
    }


    public boolean refresh() throws IOException {
        System.out.println("downloading available fields");
        this.refreshFields();
        System.out.println("downloading benchmarks");
        this.refreshBenchmark();
        System.out.println("downloading all stocks");
        this.refreshStock();
        System.out.println("refresh done");
        return true;
    }


}
