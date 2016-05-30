package data.download;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/6.
 */
public class DownloadDataTxtTest {

    @Test
    public void testDownloadStock() throws Exception {
        DownloadDataTxt downloadDataTxt = new DownloadDataTxt();
//        downloadDataTxt.downloadStock("sh600000","2009-01-01","2016-04-06");
        downloadDataTxt.downloadStock("sh600000","2009-04-01","2016-04-06");
    }

    @Test
    public void testDownloadBenchmark() throws Exception {

    }
}