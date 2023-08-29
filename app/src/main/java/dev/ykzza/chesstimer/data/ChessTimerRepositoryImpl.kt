package dev.ykzza.chesstimer.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dev.ykzza.chesstimer.domain.ChessTimerRepository
import dev.ykzza.chesstimer.domain.TimeMode

class ChessTimerRepositoryImpl(
    application: Application
) : ChessTimerRepository {

    private val timeModeDao = AppDatabase.getInstance(application).timeModeDao()
    private val dataStoreRepository = DataStoreRepository(application)
    private val mapper = TimeModeMapper()

    override fun getSavedTimeMode(): LiveData<TimeMode> {
        return dataStoreRepository.readTimeMode.asLiveData().map {
            mapper.dbModelToEntity(it)
        }
    }

    override suspend fun saveTimeMode(timeMode: TimeMode) {
        dataStoreRepository.saveTimeMode(
            id = timeMode.id,
            baseTime = timeMode.baseTime,
            addTime = timeMode.addTime
        )
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