package otus.project.feature.db

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import otus.project.common.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerStore @Inject constructor(val dao : otus.project.feature.db.MarkerDao) {

    suspend fun addItem(item : otus.project.common.Item, place : otus.project.common.Place, onError : (String) -> Unit = {}) {
        withContext(currentCoroutineContext()) {
            try {
                val cats: List<Long> = dao.getCategoryId(item.category)
                dao.addObject(
                    otus.project.feature.db.ObjectData(
                        id = item.id,
                        name = item.name,
                        info = item.details,
                        address = item.address,
                        catid = if (cats.isEmpty()) null else cats.first()
                    )
                )
                dao.addMarker(
                    otus.project.feature.db.MarkerData(
                        id = 0,
                        latitude = place.latitude,
                        longitude = place.longitude,
                        objid = item.id
                    )
                )
            } catch (t: Throwable) {
                onError(t.message ?: "unknown error in addItem()")
            }
        }
    }

    suspend fun getItems(onError : (String) -> Unit = {}) : List<otus.project.common.Item> {
        val items: MutableList<otus.project.common.Item> = mutableListOf()
        withContext(currentCoroutineContext()) {
            try {
                val data: List<otus.project.feature.db.ObjectData> = dao.getObjects()
                data.forEach {
                    var cat = ""
                    if (it.catid != null) {
                        val cats: List<String> = dao.getCategory(it.catid)
                        if (cats.isNotEmpty())
                            cat = cats.first();
                    }
                    items.add(Item(it.id, it.name, it.info, it.address, cat))
                }
            } catch (t: Throwable) {
                onError(t.message ?: "unknown error in getItems()")
            }
        }
        return items.toList()
    }

    suspend fun getPlace(objId : Long, onError : (String) -> Unit = {}) : otus.project.common.Place {
        val place = otus.project.common.Place()
        withContext(currentCoroutineContext()) {
            try {
                val marks: List<otus.project.feature.db.MarkerData> = dao.getMarker(objId)
                if (marks.isNotEmpty()) {
                    val mark: otus.project.feature.db.MarkerData = marks.first()
                    place.latitude = mark.latitude
                    place.longitude = mark.longitude
                }
            } catch (t: Throwable) {
                onError(t.message ?: "unknown error in getPlace()")
            }
        }
        return place
    }
}