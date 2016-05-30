package ui.loginui.driver;

import bl.stockviewbl.LoginImpl;
import blservice.stockviewblservice.LoginBLService;
import org.junit.Test;
import tool.constant.ResultMsg;

import static org.junit.Assert.assertEquals;

/**
 * Created by kylin on 16/3/6.
 */
public class loginDriver {

    public static void main(String[] args) {
        LoginBLService loginBLService = new LoginImpl();
        loginDriver driver = new loginDriver();
        driver.drive(loginBLService);
    }

    @Test
    public void drive(LoginBLService loginBLService) {
        ResultMsg resultMsg = loginBLService.addUser("test001", "test001");
        assertEquals(resultMsg.isPass(),true);
        loginBLService.logIn("test001", "test001");
        assertEquals(resultMsg.isPass(),true);
        loginBLService.logIn("test001", "test002");
        assertEquals(resultMsg.isPass(),false);
    }

}
