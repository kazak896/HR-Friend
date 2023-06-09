package ru.yandex.hrfriend.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.yandex.hrfriend.data.remote.chat.ChatSocketService
import ru.yandex.hrfriend.data.remote.chat.ChatSocketServiceImpl
import ru.yandex.hrfriend.domain.use_case.auth.AuthUseCases
import ru.yandex.hrfriend.domain.use_case.auth.AppLoginUseCase
import ru.yandex.hrfriend.domain.use_case.auth.AppSignUpUseCase
import ru.yandex.hrfriend.data.remote.api.AuthApi
import ru.yandex.hrfriend.data.remote.api.PersonApi
import ru.yandex.hrfriend.data.remote.api.ResumeResponseApi
import ru.yandex.hrfriend.data.remote.api.VacancyApi
import ru.yandex.hrfriend.data.remote.api.VacancyTypeApi
import ru.yandex.hrfriend.data.remote.repository.AuthRepositoryImpl
import ru.yandex.hrfriend.data.remote.repository.PersonRepositoryImpl
import ru.yandex.hrfriend.data.remote.repository.ResumeResponseRepositoryImpl
import ru.yandex.hrfriend.data.remote.repository.VacancyRepositoryImpl
import ru.yandex.hrfriend.data.remote.repository.VacancyTypeRepositoryImpl
import ru.yandex.hrfriend.domain.repository.AuthRepository
import ru.yandex.hrfriend.domain.repository.PersonRepository
import ru.yandex.hrfriend.domain.repository.ResumeResponseRepository
import ru.yandex.hrfriend.domain.repository.VacancyRepository
import ru.yandex.hrfriend.domain.repository.VacancyTypeRepository
import ru.yandex.hrfriend.domain.use_case.person.PersonUseCases
import ru.yandex.hrfriend.domain.use_case.person.UpdatePersonLinkUseCase
import ru.yandex.hrfriend.domain.use_case.resume_response.AddResumeResponseUseCase
import ru.yandex.hrfriend.domain.use_case.resume_response.DeleteResumeResponseUseCase
import ru.yandex.hrfriend.domain.use_case.resume_response.GetByUserUseCase
import ru.yandex.hrfriend.domain.use_case.resume_response.GetByVacancyIdUseCase
import ru.yandex.hrfriend.domain.use_case.resume_response.ResumeResponseUseCases
import ru.yandex.hrfriend.domain.use_case.resume_response.UpdateStatusResumeResponseUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.AddVacancyUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.DeleteVacancyUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.GetVacanciesUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.GetVacancyByIdUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.UpdateVacancyUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy.VacancyUseCases
import ru.yandex.hrfriend.domain.use_case.vacancy_type.AddVacancyTypeUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy_type.DeleteVacancyTypeUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy_type.GetVacanciesTypesUseCase
import ru.yandex.hrfriend.domain.use_case.vacancy_type.VacancyTypeUseCases
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.DispatcherProvider
import ru.yandex.hrfriend.util.PreferencesManager
import javax.inject.Singleton

//private const val BASE_LOCALHOST = "http://${Constants.domain}/"
private const val BASE_LOCALHOST = "http://84.201.131.3:8080/"
private const val BASE_PATH = "api/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthApi(): AuthApi = Retrofit.Builder()
        .baseUrl(BASE_LOCALHOST + BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)

    @Singleton
    @Provides
    fun providePersonApi(): PersonApi = Retrofit.Builder()
        .baseUrl(BASE_LOCALHOST + BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PersonApi::class.java)

    @Singleton
    @Provides
    fun provideVacancyApi(): VacancyApi = Retrofit.Builder()
        .baseUrl(BASE_LOCALHOST + BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VacancyApi::class.java)

    @Singleton
    @Provides
    fun provideVacancyTypeApi(): VacancyTypeApi = Retrofit.Builder()
        .baseUrl(BASE_LOCALHOST + BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VacancyTypeApi::class.java)

    @Singleton
    @Provides
    fun provideResumeResponseApi(): ResumeResponseApi = Retrofit.Builder()
        .baseUrl(BASE_LOCALHOST + BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ResumeResponseApi::class.java)

    @Provides
    @Singleton
    fun provideVacancyUseCases(
        repository: VacancyRepository
    ) : VacancyUseCases {
        return VacancyUseCases(
            getVacanciesUseCase = GetVacanciesUseCase(repository),
            addVacancyUseCase = AddVacancyUseCase(repository),
            updateVacancyUseCase = UpdateVacancyUseCase(repository),
            deleteVacancyUseCase = DeleteVacancyUseCase(repository),
            getVacancyByIdUseCase = GetVacancyByIdUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideVacancyTypeUseCases(
        repository: VacancyTypeRepository
    ) : VacancyTypeUseCases {
        return VacancyTypeUseCases(
            getVacanciesTypesUseCase = GetVacanciesTypesUseCase(repository),
            addVacancyTypeUseCase = AddVacancyTypeUseCase(repository),
            deleteVacancyTypeUseCase = DeleteVacancyTypeUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        repository: AuthRepository,
        preferencesManager: PreferencesManager
    ) : AuthUseCases {
        return AuthUseCases(
            appLoginUseCase = AppLoginUseCase(repository, preferencesManager),
            appSignUpUseCase = AppSignUpUseCase(repository, preferencesManager)
        )
    }

    @Provides
    @Singleton
    fun providePersonUseCases(
        repository: PersonRepository
    ) : PersonUseCases {
        return PersonUseCases(
           updatePersonLinkUseCase = UpdatePersonLinkUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideResumeResponseUseCases(
        repository: ResumeResponseRepository
    ) : ResumeResponseUseCases {
        return ResumeResponseUseCases(
            addResumeResponseUseCase = AddResumeResponseUseCase(repository),
            deleteResumeResponseUseCase = DeleteResumeResponseUseCase(repository),
            updateStatusResumeResponseUseCase = UpdateStatusResumeResponseUseCase(repository),
            getByUserUseCase = GetByUserUseCase(repository),
            getByVacancyIdUseCase = GetByVacancyIdUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providePrefsManager(
        app: Application
    ) : PreferencesManager {
        return PreferencesManager(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        authApi: AuthApi
    ) : AuthRepository = AuthRepositoryImpl(authApi)

    @Singleton
    @Provides
    fun provideVacancyRepository(
        vacancyApi: VacancyApi,
        preferencesManager: PreferencesManager
    ) : VacancyRepository = VacancyRepositoryImpl(vacancyApi, preferencesManager)

    @Singleton
    @Provides
    fun provideVacancyTypeRepository(
        vacancyTypeApi: VacancyTypeApi,
        preferencesManager: PreferencesManager
    ) : VacancyTypeRepository = VacancyTypeRepositoryImpl(vacancyTypeApi, preferencesManager)

    @Singleton
    @Provides
    fun provideResumeResponseRepository(
        responseApi: ResumeResponseApi,
        preferencesManager: PreferencesManager
    ) : ResumeResponseRepository = ResumeResponseRepositoryImpl(responseApi, preferencesManager)

    @Singleton
    @Provides
    fun providePersonRepository(
        responseApi: PersonApi,
        preferencesManager: PreferencesManager
    ) : PersonRepository = PersonRepositoryImpl(responseApi, preferencesManager)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient): ChatSocketService {
        return ChatSocketServiceImpl(client)
    }

}