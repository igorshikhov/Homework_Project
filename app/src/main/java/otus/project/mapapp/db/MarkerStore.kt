package otus.project.mapapp.db

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import otus.project.mapapp.model.Item
import otus.project.mapapp.model.Place
import javax.inject.Inject
/*
class MarkerStore @Inject constructor(@ApplicationContext private val ctx : Context) {

    @Inject lateinit var db : MarkerDatabase

    private val dao : MarkerDao = db.getDao()
*/
class MarkerStore (private val ctx : Context) {

    companion object {
        private lateinit var context: Context
        private val db: MarkerDatabase by lazy { provideDatabase(context) }

        fun getDao (ctx : Context) : MarkerDao {
            context = ctx
            return db.getDao()
        }
    }

    private val dao : MarkerDao = getDao(ctx)
/* */
    fun addItem(item : Item, place : Place) {
        var err : String? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cats : List<Long> = dao.getCategoryId(item.category)
                dao.addObject(
                    ObjectData(
                        id = item.id,
                        name = item.name,
                        info = item.details,
                        address = item.address,
                        catid = if (cats.isEmpty()) null else cats.first()
                    ))
                dao.addMarker(
                    MarkerData(
                        id = 0,
                        latitude = place.latitude,
                        longitude = place.longitude,
                        objid = item.id
                    ))
            }
            catch(t : Throwable) {
                err = t.message ?: "unknown error"
            }
        }
        err?.let {
            Toast.makeText(ctx, err, Toast.LENGTH_LONG).show()
        }
    }

    fun getItems() : List<Item> {
        var err : String? = null
        val items: MutableList<Item> = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data: List<ObjectData> = dao.getObjects()
                data.forEach {
                    var cat = ""
                    if (it.catid != null) {
                        val cats: List<String> = dao.getCategory(it.catid)
                        if (cats.isNotEmpty())
                            cat = cats.first();
                    }
                    items.add(Item(it.id, it.name, it.info, it.address, cat))
                }
            }
            catch(t : Throwable) {
                err = t.message ?: "unknown error"
            }
        }
        err?.let {
            Toast.makeText(ctx, err, Toast.LENGTH_LONG).show()
        }
        return items.toList()
    }

    fun getPlace(objId : Long) : Place {
        val place = Place()
        var err : String? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val marks: List<MarkerData> = dao.getMarker(objId)
                if (marks.isNotEmpty()) {
                    val mark : MarkerData = marks.first()
                    place.latitude = mark.latitude
                    place.longitude = mark.longitude
                }
            }
            catch(t : Throwable) {
                err = t.message ?: "unknown error"
            }
        }
        err?.let {
            Toast.makeText(ctx, err, Toast.LENGTH_LONG).show()
        }
        return place
    }
}
