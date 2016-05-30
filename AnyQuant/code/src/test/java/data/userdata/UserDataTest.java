package data.userdata;

import data.logindata.UserDataTxtImpl;
import dataservice.logindataservice.UserDataService;
import org.junit.Test;
import tool.constant.ResultMsg;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by kylin on 16/3/8.
 */
public class UserDataTest {

    @Test
    public void testLogin() throws ClassNotFoundException {
        UserDataService userDataService = new UserDataTxtImpl();
        ResultMsg resultMsg = userDataService.login("test", "test");
        assertEquals(resultMsg.isPass(), true);

        resultMsg = userDataService.login("test2", "test2");
        assertEquals(resultMsg.isPass(), false);
    }

    @Test
    public void testAddUser() throws ClassNotFoundException {
        UserDataService userDataService = new UserDataTxtImpl();

        userDataService.addUser("txy2", "txy2");

        ResultMsg resultMsg = userDataService.login("txy2", "txy2");
        assertEquals(resultMsg.isPass(), true);

        resultMsg = userDataService.login("txy2", "txy14");
        assertEquals(resultMsg.isPass(), false);
    }


    @Test
    public void testDeleteCollection() throws ClassNotFoundException {
        UserDataService userDataService = new UserDataTxtImpl();

//        List<String> list = userDataService.getUserCollections("test", "test");

        ResultMsg resultMsg= userDataService.deleteCollcetion("test","test","sz600519");
        System.out.println(resultMsg.getMessage());

        List<String> list = userDataService.getUserCollections("test", "test");

        System.out.println(list.size());
        for (String x : list)
            System.out.println(x);


    }

    @Test
    public void testAddCollection() throws ClassNotFoundException {
        UserDataService userDataService = new UserDataTxtImpl();

        List<String> list = userDataService.getUserCollections("test", "test");

//        userDataService.addUserCollection("test","test","sh601328");
//        userDataService.addUserCollection("test","test","sh600779");
//        userDataService.addUserCollection("test","test","sh600519");

//        userDataService.addUserCollection("txy","txy","sh600006");

//        assertEquals(list.size(),3);

        System.out.println(list.size());
        for (String x : list)
            System.out.println(x);

    }

}
