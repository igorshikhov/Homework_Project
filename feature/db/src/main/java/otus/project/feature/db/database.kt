package otus.project.feature.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

/*
CREATE TABLE categories (
    id INTEGER NOT NULL PRIMARY KEY,
    category TEXT NOT NULL
);

CREATE TABLE objects (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    info TEXT,
    address TEXT,
    FOREIGN KEY (catid) REFERENCES categories(id)
);

CREATE TABLE markers (
    id INTEGER NOT NULL PRIMARY KEY,
    latitude DECIMAL(10,6) NOT NULL,
    longitude DECIMAL(10,6) NOT NULL,
    FOREIGN KEY (objid) REFERENCES objects(id)
);
 */

// Categories
@Entity(tableName = "categories",
    indices = [Index("id"), Index("category")]
)
data class CategoryData (
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val category : String
)

// Objects
@Entity(tableName = "objects",
    indices = [Index("id"), Index("name"), Index("catid")],
    foreignKeys = [
        ForeignKey(
            entity = CategoryData::class,
            parentColumns = ["id"],
            childColumns = ["catid"]
        )
    ]
)
data class ObjectData (
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val name : String,
    val info : String,
    val address : String,
    val catid : Long?
)

// Markers
@Entity(tableName = "markers",
    indices = [Index("id"), Index("objid")],
    foreignKeys = [
        ForeignKey(
            entity = ObjectData::class,
            parentColumns = ["id"],
            childColumns = ["objid"]
        )
    ]
)
data class MarkerData (
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val latitude : Float,
    val longitude : Float,
    val objid : Long?,
)

@Dao
interface MarkerDao {
    // найти метку объекта
    @Query("SELECT * FROM markers WHERE objid = :objid")
    fun getMarker(objid : Long) : List<MarkerData>

    // добавить метку объекта
    @Insert(entity = MarkerData::class)
    fun addMarker(data : MarkerData)

    // получить ID объекта
    @Query("SELECT id FROM objects WHERE name LIKE :name")
    fun getObjectId(name : String) : List<Long>

    // выбрать все объекты
    @Query("SELECT * FROM objects")
    fun getObjects() : List<ObjectData>

    // выбрать объект
    @Query("SELECT * FROM objects WHERE id = :objid")
    fun getObject(objid : Long) : List<ObjectData>

    // добавить объект
    @Insert(entity = ObjectData::class)
    fun addObject(data : ObjectData)

    // изменить объект
    //@Update(entity = ObjectData::class)
    //fun update(data : ObjectData) : Int

    // удалить объект
    //@Delete(entity = ObjectData::class)
    //fun delete(vararg data : ObjectData) : Int

    @Query("DELETE FROM objects WHERE id = :objid")
    fun deleteObject(objid : Long)

    // выбрать по категории
    @Query("SELECT objects.* FROM categories, objects WHERE categories.category = :cat AND categories.id = objects.catid")
    fun getByCategory(cat : String) : List<ObjectData>

    // ID категории
    @Query("SELECT id FROM categories WHERE category = :cat")
    fun getCategoryId(cat : String) : List<Long>

    // выбрать категорию
    @Query("SELECT category FROM categories WHERE id = :catid")
    fun getCategory(catid : Long) : List<String>

    // выбрать все категории
    @Query("SELECT * FROM categories")
    fun getCategories() : List<CategoryData>

    // добавить категорию
    @Insert(entity = CategoryData::class)
    fun addCategory(cat : CategoryData)
}

@Database(
    version = 1,
    entities = [
        CategoryData::class,
        ObjectData::class,
        MarkerData::class
    ],
    exportSchema = false
)
abstract class MarkerDatabase : RoomDatabase() {
    abstract fun getDao() : MarkerDao

    companion object {

        private const val MAPAPP_DB = "mapapp.db"

        private var instance : MarkerDatabase? = null

        fun provideDatabase(context: Context) : MarkerDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, MarkerDatabase::class.java, MAPAPP_DB)
                    .allowMainThreadQueries()
                    .build()
                    .also { instance = it }
            }
        }
    }
}

