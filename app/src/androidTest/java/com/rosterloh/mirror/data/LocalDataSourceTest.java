package com.rosterloh.mirror.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * Integration test for the {@link DataSource}, which uses the {@link DataDbHelper}.
 */
@RunWith(AndroidJUnit4.class)
public class LocalDataSourceTest {

    private LocalDataSource localDataSource;

    @Before
    public void setup() {
        localDataSource = LocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext());
    }

    @After
    public void cleanUp() {
        //localDataSource.deleteAllData();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(localDataSource);
    }
}
