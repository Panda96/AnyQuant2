package data.stockdata.sinastock;

import java.io.IOException;

/**
 * Created by kylin on 16/3/9.
 */
public interface SinaStockDataService {

    public String getNameFromStockNum(String stockNum) throws IOException;

}
