package data;

import data.download.RefreshCacheThread;
import org.junit.Test;
import tool.exception.NotFoundException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by kylin on 16/3/18.
 */
public class RefreshTest {

    @Test
    public void test() throws NotFoundException, IOException {
        RefreshCacheThread cacheThread = new RefreshCacheThread();
        cacheThread.refresh();
        assertEquals(cacheThread.refreshOrNot(),false);
    }
}
