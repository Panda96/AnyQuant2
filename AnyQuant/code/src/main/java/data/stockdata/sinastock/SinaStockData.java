package data.stockdata.sinastock;

import data.url.UrlRequestHttpClient;
import data.url.UrlRequestService;

import java.io.IOException;

/**
 * Created by kylin on 16/3/9.
 */
public class SinaStockData implements SinaStockDataService{

    private UrlRequestService urlRequestService;

    public SinaStockData() {
        urlRequestService = new UrlRequestHttpClient();
    }

    public String getNameFromStockNum(String stockNum) throws IOException {
        String url = "http://hq.sinajs.cn/list=" + stockNum;
        String result = this.urlRequestService.getSring(url);
        int start = result.indexOf('"');
        result = result.substring(start + 1);
        String[] strings = result.split(",");
        return strings[0];
    }

}
