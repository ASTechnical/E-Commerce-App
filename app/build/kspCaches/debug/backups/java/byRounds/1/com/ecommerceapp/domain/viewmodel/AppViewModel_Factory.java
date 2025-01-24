package com.ecommerceapp.domain.viewmodel;

import android.app.Application;
import com.ecommerceapp.data.repository.AppRepository;
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
  private final Provider<AppRepository> appRepositoryProvider;

  private final Provider<Application> applicationProvider;

  public AppViewModel_Factory(Provider<AppRepository> appRepositoryProvider,
      Provider<Application> applicationProvider) {
    this.appRepositoryProvider = appRepositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(appRepositoryProvider.get(), applicationProvider.get());
  }

  public static AppViewModel_Factory create(Provider<AppRepository> appRepositoryProvider,
      Provider<Application> applicationProvider) {
    return new AppViewModel_Factory(appRepositoryProvider, applicationProvider);
  }

  public static AppViewModel newInstance(AppRepository appRepository, Application application) {
    return new AppViewModel(appRepository, application);
  }
}
