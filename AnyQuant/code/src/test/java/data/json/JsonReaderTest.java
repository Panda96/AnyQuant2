package data.json;

import data.download.APIDataServiceJson;
import data.download.APIDataServiceUrlImplJson;
import data.factory.DataFactoryUrlImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/13.
 */
public class JsonReaderTest {

    private APIDataServiceJson apiDataServiceJson = new APIDataServiceUrlImplJson();

    private JsonReader jsonReader;

    public JsonReaderTest() throws IOException {
    }

    @Test
    public void testSetAllFieldsName() throws Exception {

    }

    @Test
    public void testReadAllStock() throws Exception {
//        String benchNameStr = this.apiDataServiceJson.getAllBenchmarkJson();
//        List<String> benchNames = this.jsonReader.readAllStockNames(benchNameStr);
//        System.out.println(benchNames.get(0));
    }

    @Test
    public void testReadAllFields() throws Exception {

    }

    @Test
    public void testReadAllFieldsToString() throws Exception {

    }

    @Test
    public void testReadStock() throws Exception {

    }
}