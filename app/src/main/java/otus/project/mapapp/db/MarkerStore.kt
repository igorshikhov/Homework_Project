package otus.project.mapapp.db

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import otus.project.mapapp.model.Item
import otus.project.mapapp.model.Place
import javax.inject.Inject

class MarkerStore @Inject constructor(@ApplicationContext private val ctx : Context) {

    private val db: MarkerDatabase by lazy { provideDatabase(ctx) }
    private val dao : MarkerDao by lazy { db.getDao() }

    suspend fun addItem(item : Item, place : Place) {
        withContext(currentCoroutineContext()) {
            try {
                val cats: List<Long> = dao.getCategoryId(item.category)
                dao.addObject(
                    ObjectData(
                        id = item.id,
                        name = item.name,
                        info = item.details,
                        address = item.address,
                        catid = if (cats.isEmpty()) null else cats.first()
                    )
                )
                dao.addMarker(
                    MarkerData(
                        id = 0,
                        latitude = place.latitude,
                        longitude = place.longitude,
                        objid = item.id
                    )
                )
            }
            catch (t: Throwable) {
                Toast.makeText(ctx, t.message ?: "unknown error", Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun getItems() : List<Item> {
        val items: MutableList<Item> = mutableListOf()
        withContext(currentCoroutineContext()) {
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
            catch (t: Throwable) {
                Toast.makeText(ctx, t.message ?: "unknown error", Toast.LENGTH_LONG).show()
            }
        }
        return items.toList()
    }

    suspend fun getPlace(objId : Long) : Place {
        val place = Place()
        withContext(currentCoroutineContext()) {
            try {
                val marks: List<MarkerData> = dao.getMarker(objId)
                if (marks.isNotEmpty()) {
                    val mark: MarkerData = marks.first()
                    place.latitude = mark.latitude
                    place.longitude = mark.longitude
                }
            }
            catch (t: Throwable) {
                Toast.makeText(ctx, t.message ?: "unknown error", Toast.LENGTH_LONG).show()
            }
        }
        return place
    }
}
