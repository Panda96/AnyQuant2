package data.download;

import java.io.IOException;

import data.factory.DataFactoryUrlImpl;
import tool.io.FileIOHelper;
import data.json.JsonReader;
import tool.constant.ResultMsg;

/**
 * Created by kylin on 16/3/7.
 */
public class DownloadDataTxt implements DownloadDataService {

    public static final String fileMainDir = "cache/";

    private APIDataServiceJson apiDataServiceJson;

    private String currentFields;

    private JsonReader jsonReader;

    public DownloadDataTxt() throws IOException {

        apiDataServiceJson = new APIDataServiceUrlImplJson();
        String jsonFields = apiDataServiceJson.getAllFieldsJson();

        jsonReader = new JsonReader(DataFactoryUrlImpl.getInstance().getAPIDataService().getAllFields());
        currentFields = jsonReader.readAllFieldsToString(jsonFields);
    }

    public ResultMsg downloadStockNumber(String year, String exchange) throws IOException {
        String allBenchmarkNum = this.apiDataServiceJson.getAllStockJson(year, exchange);
        String fileDir = fileMainDir + "stockNumber/" + exchange;
        String fileName = fileDir + "/" + year + ".txt";
        return FileIOHelper.creaTxtFileInDirs(allBenchmarkNum, fileDir, fileName);
    }

    public ResultMsg downloadStock(String name, String start, String end) throws IOException {
        String allBenchmarkNum = this.apiDataServiceJson.getStockJson(name, start, end, this.currentFields);
        String fileDir = fileMainDir + "stockInfo";
        String fileName = fileDir + "/" + name + ".txt";
        return FileIOHelper.creaTxtFileInDirs(allBenchmarkNum, fileDir, fileName);
    }

    public ResultMsg downloadBenchmarkNumber() throws IOException {
        String allBenchmarkNum = this.apiDataServiceJson.getAllBenchmarkJson();
        String fileName = fileMainDir + "stockNumber/" + "benchmarkNumbers.txt";
        return FileIOHelper.creatTxtFile(allBenchmarkNum, fileName);
    }

    public ResultMsg downloadBenchmark(String bench, String start, String end) throws IOException {
        String allBenchmarkNum = this.apiDataServiceJson.getBenchmarkJson(bench, start, end, this.currentFields);
//        System.out.println(allBenchmarkNum);
        String fileDir = fileMainDir + "stockInfo";
        String fileName = fileDir + "/" + bench + ".txt";
        return FileIOHelper.creaTxtFileInDirs(allBenchmarkNum, fileDir, fileName);
    }

    public ResultMsg downloadFields() throws IOException {
        String fieldsJson = this.apiDataServiceJson.getAllFieldsJson();
        String fileName = fileMainDir + "availableFields.txt";
        return FileIOHelper.creatTxtFile(fieldsJson, fileName);
    }

    public JsonReader getJsonReader() {
        return jsonReader;
    }
}
