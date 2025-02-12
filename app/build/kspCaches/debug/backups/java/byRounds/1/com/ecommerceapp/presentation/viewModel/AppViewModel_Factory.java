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
  private final Provider<Repository> repositoryProvider;

  private final Provider<Application> applicationProvider;

  public AppViewModel_Factory(Provider<Repository> repositoryProvider,
      Provider<Application> applicationProvider) {
    this.repositoryProvider = repositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(repositoryProvider.get(), applicationProvider.get());
  }

  public static AppViewModel_Factory create(Provider<Repository> repositoryProvider,
      Provider<Application> applicationProvider) {
    return new AppViewModel_Factory(repositoryProvider, applicationProvider);
  }

  public static AppViewModel newInstance(Repository repository, Application application) {
    return new AppViewModel(repository, application);
  }
}
