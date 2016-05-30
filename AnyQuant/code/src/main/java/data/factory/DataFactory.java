package data.factory;

import dataservice.logindataservice.UserDataService;
import dataservice.stockdataservice.CacheDataService;

/**
 * Created by kylin on 16/3/7.
 */
public interface DataFactory {

    CacheDataService getCacheDataService();

//    APIDataService getAPIDataService();

    UserDataService getUserDataService();
}
