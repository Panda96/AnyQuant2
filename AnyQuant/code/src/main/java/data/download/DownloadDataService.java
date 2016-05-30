package data.download;

import tool.constant.ResultMsg;

import java.io.IOException;

/**
 * Created by kylin on 16/3/7.
 */
public interface DownloadDataService {

    /**
     * download all stockNumber from AnyQuant website
     *
     * @param year year
     * @param exchange sz or sh
     * @return download result
     */
    ResultMsg downloadStockNumber(String year, String exchange) throws IOException;

    /**
     * download a stock(with all available info)
     *
     * @param name stock number
     * @param start startPrice time
     * @param end end time
     * @return download result
     */
    ResultMsg downloadStock(String name, String start, String end) throws IOException;

    /**
     * download all available benchmark numbers
     *
     * @return download result
     */
    ResultMsg downloadBenchmarkNumber() throws IOException;

    /**
     * download a benchmark(with all available info)
     *
     * @param bench benchmark number
     * @param start time
     * @param end end time
     * @return download result
     */
    ResultMsg downloadBenchmark(String bench, String start, String end) throws IOException;

    /**
     * down available fields
     *
     * @return download result
     */
    ResultMsg downloadFields() throws IOException;

}
