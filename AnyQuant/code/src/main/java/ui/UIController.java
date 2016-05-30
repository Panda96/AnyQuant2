package ui;

/**
 * Created by kylin on 16/3/9.
 *
 * 所有界面Controller的父类,持有UIMain的引用,以便调用其界面跳转方法
 */
public class UIController {

    /**
     * UIMain引用
     */
    protected UIMain mainUI;

    public void setMainUI(UIMain mainUI) {
        this.mainUI = mainUI;
    }

}
