package com.rosterloh.mirror.ui.mirror;

import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

public class MirrorViewModel extends ViewModel {

    private final LiveData<Resource<List<Repo>>> results;

    @Inject
    MirrorViewModel(RepoRepository repoRepository) {
        nextPageHandler = new NextPageHandler(repoRepository);
        results = Transformations.switchMap(query, search -> {
            if (search == null || search.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.search(search);
            }
        });
    }

    LiveData<Resource<List<Repo>>> getResults() {
        return results;
    }

}
