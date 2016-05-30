package data.url;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by kylin on 16/3/3.
 */
public class UrlRequestHttpClient implements UrlRequestService {

    public String getString(String strUrl, String openCode) throws IOException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(strUrl);
        httpGet.setHeader("X-Auth-Code", openCode);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity);
    }

    @Override
    public String getSring(String strUrl) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(strUrl);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity);

    }
}
