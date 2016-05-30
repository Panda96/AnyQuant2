package data.stockdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.download.DownloadDataTxt;
import tool.io.FileIOHelper;
import data.json.JsonReader;
import dataservice.stockdataservice.CacheDataService;
import po.StockPO;
import po.TradeInfoLine;
import po.TradeInfoPO;
import tool.constant.CacheFilePath;
import tool.enums.TradeEnum;
import tool.exception.NotFoundException;

/**
 * Created by kylin on 16/3/7.
 */
public class CacheDataTxtImpl implements CacheDataService {

    private JsonReader jsonReader;

    private String mainPath = DownloadDataTxt.fileMainDir;

    private HashMap<String, String> stockNumberAndName;
    private HashMap<String, String> benchNumberAndName;

    public CacheDataTxtImpl() throws IOException {
        jsonReader = new JsonReader();
        jsonReader.setAllFieldsName(this.getAllFields());
        this.stockNumberAndName = new HashMap<>();
        this.benchNumberAndName = new HashMap<>();
        //TODO hide file path
        //read stock number and name
        String info = FileIOHelper.readTxtFile("cache/cacheStockInfo.txt");
        String[] stocks = info.split(";");
        for (String astock : stocks) {
            String[] numAndName = astock.split(":");
            stockNumberAndName.put(numAndName[0], numAndName[1]);
        }
        List<String> benchNumbers = this.getAllBenchmark();
        for (String bench : benchNumbers) {
            benchNumberAndName.put(bench, "暂且没有名称");
        }
    }

    public HashMap<String, String> getStockNumAndName() throws IOException {
        return stockNumberAndName;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getNamesAndNames() throws IOException {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();

        HashMap<String, String> oneIndustry = new HashMap<>();
        ArrayList<String> oneIndusStr = new ArrayList<>();

        ArrayList<String> selectedStocks = FileIOHelper.readTxtFileLines("cache/selectedStocks.txt");

        for (String line : selectedStocks) {
            //stock
            if (line.startsWith("s")) {


                //行业
            } else if (line.startsWith("!")) {
                oneIndusStr.add(line);
            }
        }
        return null;
    }

    @Override
    public StockPO getStock(String num) throws NotFoundException, IOException {
//        System.out.println(num);
//        System.out.println();
        if (!this.stockNumberAndName.containsKey(num))
            throw new NotFoundException("目标股票信息不存在");

        else {
            String jsonResult = FileIOHelper.readTxtFile(mainPath + "stockInfo/" + num + ".txt");
            StockPO po = jsonReader.readStock(jsonResult);

            String stockName = this.stockNumberAndName.get(num);
            po.setName(stockName);

            return po;
        }
    }

    @Override
    public List<String> getAllBenchmark() throws IOException {
        String jsonResult = FileIOHelper.readTxtFile(mainPath + "stockNumber/benchmarkNumbers.txt");
        return jsonReader.readAllStockNames(jsonResult);
    }

    @Override
    public StockPO getBenchmark(String bench) throws NotFoundException, IOException {
        if (!this.benchNumberAndName.containsKey(bench))
            throw new NotFoundException("目标大盘信息不存在");
        String jsonResult = FileIOHelper.readTxtFile(mainPath + "stockInfo/" + bench + ".txt");
        return jsonReader.readStock(jsonResult);
    }

    @Override
    public String getAllFields() throws IOException {
        String jsonResult = FileIOHelper.readTxtFile(mainPath + "availableFields.txt");
        return jsonReader.readAllFieldsToString(jsonResult);
    }

    @Override
    public TradeInfoPO getLatestDayTradeInfo(String number, String date) throws NotFoundException, IOException {
        String file = CacheFilePath.LATEST_INFO_PATH + date + "/" + number + ".txt";
        ArrayList<String> strInfos = FileIOHelper.readTxtFileLines(file);
        HashMap<String, TradeInfoLine> stockTradeInfo = new HashMap<>();

//        System.out.println(strInfos.size());
//        System.out.println(file);
        // check data
        String headLine = strInfos.get(1);
        if (headLine.contains("当天没有数据")){
            throw new NotFoundException(number +" " + date +" 当天没有数据");
        }

        // get open and close price
        int size = strInfos.size();

        String firstLine = strInfos.get(1);
        String lastLine = strInfos.get(size - 1);

        String[] split = lastLine.split(",");
        double open = Double.parseDouble(split[2]);
        split = firstLine.split(",");
        double close = Double.parseDouble(split[2]);

        for (String line : strInfos) {


            if (line.startsWith(",")) {
            } else {
                //id,time,price,change,volume,amount,type
                split = line.split(",");
                int id = Integer.parseInt(split[0]);
                String time = split[1];
                double price = Double.parseDouble(split[2]);
                double change = 0;
                try {
                    change = Double.parseDouble(split[3]);
                } catch (NumberFormatException ignored) {

                }
                double volume = Double.parseDouble(split[4]);
                double amount = Double.parseDouble(split[5]);
                TradeEnum tradeEnum = TradeEnum.getTradeEnum(split[6]);

                //get one line
                TradeInfoLine tradeInfoLine = new TradeInfoLine(id, time, price,
                        change, volume, amount, tradeEnum);
                stockTradeInfo.put(time, tradeInfoLine);
            }
        }
        TradeInfoPO tradeInfoPO = new TradeInfoPO(stockTradeInfo);
        tradeInfoPO.setOpen(open);
        tradeInfoPO.setClose(close);
        return tradeInfoPO;
    }

    @Override
    public String getLatestDayOfLatestInfo() {
        String filePath = CacheFilePath.LATEST_INFO_PATH;
        File file = new File(filePath);
        File[] tempList = file.listFiles();
        String date = "2016-01-01";
        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile()) {
//                String thisDate = tempList[i].getName();
//                System.out.println(thisDate);
//            }
            if (tempList[i].isDirectory()) {
                String thisDate = tempList[i].getName();
                if(date.compareTo(thisDate) < 0)
                    date = thisDate;
            }
        }
        return date;
    }

}