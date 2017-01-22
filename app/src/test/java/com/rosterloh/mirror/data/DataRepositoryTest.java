package com.rosterloh.mirror.data;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;

import android.content.Context;

import com.rosterloh.mirror.models.Weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class DataRepositoryTest {

    private static Object[] DATA;

    private DataRepository dataRepository;

    private TestSubscriber<Weather> weatherTestSubscriber;

    @Mock
    private DataSource remoteDataSource;

    @Mock
    private DataSource localDataSource;

    @Mock
    private Context context;

    @Before
    public void setupDataRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        dataRepository = DataRepository.getInstance(
                remoteDataSource, localDataSource);

        weatherTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void destroyRepositoryInstance() {
        DataRepository.destroyInstance();
    }
/*
    @Test
    public void getData_repositoryCachesAfterFirstSubscription_whenTasksAvailableInLocalStorage() {
        // Given that the local data source has data available
        setDataAvailable(remoteDataSource, DATA);
        // And the remote data source does not have any data available
        setDataNotAvailable(localDataSource);

        // When two subscriptions are set
        TestSubscriber<Weather> testSubscriber1 = new TestSubscriber<>();
        dataRepository.getWeather().subscribe(testSubscriber1);

        TestSubscriber<Weather> testSubscriber2 = new TestSubscriber<>();
        dataRepository.getWeather().subscribe(testSubscriber2);

        // Then tasks were only requested once from remote and local sources
        verify(remoteDataSource).getWeather();
        verify(localDataSource).getWeather();
        assertFalse(dataRepository.cacheIsDirty);
        testSubscriber1.assertValue(DATA);
        testSubscriber2.assertValue(DATA);
    }

    @Test
    public void getData_requestsAllDataFromLocalDataSource() {
        // When tasks are requested from the data repository
        dataRepository.getData(loadDataCallback);

        // Then tasks are loaded from the local data source
        verify(localDataSource).getData(any(DataSource.LoadDataCallback.class));
    }

    @Test
    public void saveData_savesDataToServiceAPI() {
        // Given a stub task with title and description
        Object[] newData = new Object[3];

        // When a task is saved to the tasks repository
        dataRepository.saveData(newData);

        // Then the service API and persistent repository are called and the cache is updated
        verify(remoteDataSource).saveData(newData);
        verify(localDataSource).saveData(newData);
        assertThat(dataRepository.cachedData.length, is(3));
    }

    @Test
    public void getDataWithDirtyCache_dataIsRetrievedFromRemote() {
        // When calling getData in the repository with dirty cache
        dataRepository.refreshData();
        dataRepository.getData(loadDataCallback);

        // And the remote data source has data available
        setDataAvailable(remoteDataSource, DATA);

        // Verify the data from the remote source is returned, not the local
        verify(localDataSource, never()).getData(loadDataCallback);
        verify(loadDataCallback).onDataLoaded(DATA);
    }

    @Test
    public void getDataWithLocalSourceUnavailable_dataRetrievedFromRemote() {
        // When calling getData in the repository
        dataRepository.getData(loadDataCallback);

        // And the local data source has no data available
        setDataNotAvailable(localDataSource);

        // And the remote data source has data available
        setDataAvailable(remoteDataSource, DATA);

        // Verify the data from the local source is returned
        verify(loadDataCallback).onDataLoaded(DATA);
    }

    @Test
    public void getDataWithBothSourcesUnavailable_firesOnDataUnavailable() {
        // When calling getData in the repository
        dataRepository.getData(loadDataCallback);

        // And the local data source has no data available
        setDataNotAvailable(localDataSource);

        // And the remote data source has no data available
        setDataNotAvailable(remoteDataSource);

        // Verify no data is returned
        verify(loadDataCallback).onDataNotAvailable();
    }

    @Test
    public void getTasks_refreshesLocalDataSource() {
        // Mark cache as dirty to force a reload of data from remote data source.
        dataRepository.refreshData();

        // When calling getData in the repository
        dataRepository.getData(loadDataCallback);

        // Make the remote data source return data
        setDataAvailable(remoteDataSource, DATA);

        // Verify that the data fetched from the remote data source was saved in local.
        verify(localDataSource, times(DATA.length)).saveData(any(Object[].class));
    }

    private void setDataNotAvailable(DataSource dataSource) {
        when(dataSource.getWeather()).thenReturn(Observable.just(0));
    }

    private void setDataAvailable(DataSource dataSource, Object[] data) {
        // don't allow the data sources to complete.
        when(dataSource.getWeather()).thenReturn(Observable.just(data).concatWith(Observable.<Weather>never()));
    }
*/
}
