package tool.constant;

/**
 * Created by Seven on 16/3/9.
 */
public class LocalInfo {
    //用户名称
    private static String localName;
    //用户密码
    private static String password;

    public LocalInfo(String userName, String password) {
        LocalInfo.localName=userName;
        LocalInfo.password=password;
    }

    public static String getPassword() {
        return password;
    }

    public static String getLocalName() {
        return localName;
    }

    public static LocalInfo getLocalInfo(){
        return new LocalInfo(LocalInfo.localName,LocalInfo.password);
    }
}
