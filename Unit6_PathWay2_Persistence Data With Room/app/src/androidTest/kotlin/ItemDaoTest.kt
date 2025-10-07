import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private lateinit var itemDao: ItemDao
    private lateinit var db: InventoryDatabase

    private val item1 = Item(1,name = "Item 1", price = 10.0, quantity = 10)
    private val item2 = Item(2, name = "Item 2", price = 20.0, quantity = 20)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db =  Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java).build()
        itemDao = db.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private suspend fun addOneToDb() {
        itemDao.insert(item1)
    }

    private suspend fun addTwoToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneToDb()
        val allItems = itemDao.getAllItems().first()
        val expectedItem = item1.copy(id = allItems[0].id)
        assertEquals(allItems[0], expectedItem)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoToDb()
        val allItems = itemDao.getAllItems().first()
        val exceptedItems = allItems.map {
            it.copy(id = it.id)
        }
        for (i in allItems.indices) {
            assertEquals(allItems[i], exceptedItems[i])
        }
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoToDb()
        itemDao.update(Item(1, "Apples", 15.0, 25))
        itemDao.update(Item(2, "Bananas", 5.0, 50))
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], Item(1, "Apples", 15.0, 25))
        assertEquals(allItems[1], Item(2, "Bananas", 5.0, 50))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItem_deletesItemFromDB() = runBlocking {
        addTwoToDb()
        itemDao.delete(item1)
        itemDao.delete(item2)
        val allItems = itemDao.getAllItems().first()
        assert(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneToDb()
        val item = itemDao.getItem(1).first()
        assertEquals(item, item1)
    }




}