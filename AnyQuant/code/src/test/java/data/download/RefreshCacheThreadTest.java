package data.download;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kylin on 16/4/6.
 */
public class RefreshCacheThreadTest {

    @Test
    public void testRefresh() throws Exception {
        RefreshCacheThread refreshCacheThread = new RefreshCacheThread();
        refreshCacheThread.refresh();
    }
}