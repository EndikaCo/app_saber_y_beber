
import androidx.room.Database
import androidx.room.RoomDatabase
import com.endcodev.saber_y_beber.data.database.dao.PlayerDao
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity


@Database(
    entities = [
        PlayerEntity::class
    ],
    version = 2
)
abstract class RoomDB : RoomDatabase() {

    abstract fun getPlayerDao(): PlayerDao
}