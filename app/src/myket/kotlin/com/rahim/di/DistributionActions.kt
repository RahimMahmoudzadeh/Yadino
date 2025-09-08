import android.content.Context
import com.rahim.MyketDistributionActionsImpl
import com.rahim.data.flavor.AppDistributionActions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DistributionActions {
  @Provides
  @Singleton
  fun provideMyketDistributionActions(@ApplicationContext context: Context): AppDistributionActions = MyketDistributionActionsImpl(context)
}
