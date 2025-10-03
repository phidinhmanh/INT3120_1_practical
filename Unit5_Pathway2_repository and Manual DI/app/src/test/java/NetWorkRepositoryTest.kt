import com.example.marsphotos.data.NetWorkMarsPhotoRepository
import com.example.marsphotos.fake.FakeMarsApiService
import com.example.marsphotos.fake.FakeMarsResource
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals

class NetWorkRepositoryTest {
    @Test
    fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList() {
        runTest {
            val repository = NetWorkMarsPhotoRepository(
                FakeMarsApiService()
            )
            assertEquals(FakeMarsResource.photo, repository.getMarsPhotos())
        }
    }
}