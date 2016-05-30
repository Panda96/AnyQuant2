package data.stockdata;


import java.io.IOException;
import java.util.List;

import data.download.APIDataServiceUrlImplJson;
import data.json.JsonReader;
import dataservice.stockdataservice.APIDataService;
import po.StockPO;

/**
 * Created by kylin on 16/2/29.
 */
public class APIDataUrlImpl extends APIDataServiceUrlImplJson implements APIDataService {

    private JsonReader jsonReader;

    public APIDataUrlImpl() throws IOException {
        jsonReader = new JsonReader();
        jsonReader.setAllFieldsName(this.getAllFields());
    }

    public List<String> getAllStock(String year, String exchange) throws IOException {
        String jsonResult = this.getAllStockJson(year,exchange);
        return jsonReader.readAllStockNames(jsonResult);
    }

    public StockPO getStock(String name, String start, String end, String fields) throws IOException {
        String jsonResult = this.getStockJson(name,start,end,fields);
        return jsonReader.readStock(jsonResult);
    }

    public List<String> getAllBenchmark() throws IOException {
        String jsonResult = this.getAllBenchmarkJson();
        return jsonReader.readAllStockNames(jsonResult);
    }

    public StockPO getBenchmark(String bench, String start, String end, String fields) throws IOException {
        String jsonResult = this.getBenchmarkJson(bench,start,end,fields);
        return jsonReader.readStock(jsonResult);
    }

    public String getAllFields() throws IOException {
        String jsonResult = this.getAllFieldsJson();
        return jsonReader.readAllFieldsToString(jsonResult);
    }

}
