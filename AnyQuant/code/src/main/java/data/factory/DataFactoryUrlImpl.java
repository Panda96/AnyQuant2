package data.factory;

import java.io.IOException;

import data.logindata.UserDataTxtImpl;
import data.stockdata.APIDataUrlImpl;
import data.stockdata.CacheDataTxtImpl;
import dataservice.logindataservice.UserDataService;
import dataservice.stockdataservice.APIDataService;
import dataservice.stockdataservice.CacheDataService;

/**
 * Created by kylin on 16/3/8.
 */
public class DataFactoryUrlImpl implements DataFactory{
    /**
     * 数据实现的提供工厂,单件模式
     * volatile确保实例被初始化的时候,多个线程正确处理实例变量
     */
    private volatile static DataFactoryUrlImpl dataFactoryTxt;

    private UserDataService userDataService;

    private APIDataService apiDataService;

    private CacheDataService cacheDataService;

    private DataFactoryUrlImpl() throws IOException {
        try {
            cacheDataService = new CacheDataTxtImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            userDataService = new UserDataTxtImpl();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        apiDataService = new APIDataUrlImpl();
    }

    public static DataFactoryUrlImpl getInstance() throws IOException {
        if (dataFactoryTxt == null) {
            //如果实例没有被创建,进行同步,只有第一次同步加锁
            synchronized (DataFactoryTxtImpl.class) {
                if (dataFactoryTxt == null)
                    dataFactoryTxt = new DataFactoryUrlImpl();
            }
        }
        return dataFactoryTxt;
    }

    @Override
    public CacheDataService getCacheDataService() {
        return cacheDataService;
    }

    public APIDataService getAPIDataService() {
        return apiDataService;
    }

    public UserDataService getUserDataService() {
        return userDataService;
    }
}
