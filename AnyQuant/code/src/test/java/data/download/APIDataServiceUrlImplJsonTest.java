package data.download;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/6.
 */
public class APIDataServiceUrlImplJsonTest {

    @Test
    public void testGetStockJson() throws Exception {
        APIDataServiceUrlImplJson json = new APIDataServiceUrlImplJson();
        System.out.print(json.getStockJson("sh600015","2015-01-01","2016-05-03","open"));
    }
}