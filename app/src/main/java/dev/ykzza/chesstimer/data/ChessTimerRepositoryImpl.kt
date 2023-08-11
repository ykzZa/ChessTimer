package dev.ykzza.chesstimer.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dev.ykzza.chesstimer.domain.ChessTimerRepository
import dev.ykzza.chesstimer.domain.TimeMode

class ChessTimerRepositoryImpl(
    application: Application
): ChessTimerRepository {

    private val timeModeDao = AppDatabase.getInstance(application).timeModeDao()
    private val mapper = TimeModeMapper()
    override suspend fun getTimeModeById(id: Int): TimeMode {
        val timeModeDbModel = timeModeDao.getTimeModeById(id)
        return mapper.dbModelToEntity(timeModeDbModel)
    }

    override suspend fun deleteTimeMode(id: Int) {
        timeModeDao.deleteTimeMode(id)
    }

    override suspend fun addTimeMode(timeMode: TimeMode) {
        val timeModeEntity = mapper.entityToDbModel(timeMode)
        timeModeDao.addTimeMode(timeModeEntity)
    }

    override fun getTimeModeList(): LiveData<List<TimeMode>> {
        val listDbModel = timeModeDao.getTimeModeList()
        return listDbModel.map {
            mapper.mapListDbModelToEntity(it)
        }
    }
}