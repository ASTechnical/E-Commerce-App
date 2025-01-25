package com.ecommerceapp.presentation.viewModel;

import android.app.Application;
import com.ecommerceapp.data.repository.Repository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppViewModel_Factory implements Factory<AppViewModel> {
  private final Provider<Repository> appRepositoryProvider;

  private final Provider<Application> applicationProvider;

  public AppViewModel_Factory(Provider<Repository> appRepositoryProvider,
      Provider<Application> applicationProvider) {
    this.appRepositoryProvider = appRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(appRepositoryProvider.get(), applicationProvider.get());
  }

  public static AppViewModel_Factory create(Provider<Repository> appRepositoryProvider,
      Provider<Application> applicationProvider) {
    return new AppViewModel_Factory(appRepositoryProvider, applicationProvider);
  }

  public static AppViewModel newInstance(Repository repository, Application application) {
    return new AppViewModel(repository, application);
  }
}
