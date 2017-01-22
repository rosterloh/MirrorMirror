package com.rosterloh.mirror.mirror;

import com.rosterloh.mirror.data.DataRepository;
import com.rosterloh.mirror.models.Weather;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link MirrorPresenter}
 */
public class MirrorPresenterTest {

    private static Weather DATA;

    @Mock
    private DataRepository dataRepository;

    @Mock
    private MirrorFragment mirrorView;

    private MirrorPresenter mirrorPresenter;

    @Before
    public void setupMirrorPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mirrorPresenter = new MirrorPresenter(dataRepository, mirrorView);

        DATA = new Weather.Builder().build();

        // The presenter won't update the view unless it's active.
        when(mirrorView.isActive()).thenReturn(true);
    }
/*
    @Test
    public void loadAllDataFromRepositoryAndLoadIntoView() {
        // Given an initialized MirrorPresenter with initialised data
        when(dataRepository.getWeather()).thenReturn(Observable.just(DATA));

        // Then progress indicator is shown
        verify(mirrorView).setLoadingIndicator(true);
        // Then progress indicator is hidden and all tasks are shown in UI
        verify(mirrorView).setLoadingIndicator(false);
    }
*/
}
