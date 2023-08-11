package dev.ykzza.chesstimer.data

import dev.ykzza.chesstimer.domain.TimeMode

class TimeModeMapper {

    fun dbModelToEntity(dbModel: TimeModeDbModel): TimeMode = TimeMode(
        id = dbModel.id,
        baseTime = dbModel.baseTime,
        addTime = dbModel.addTime
    )

    fun entityToDbModel(entity: TimeMode): TimeModeDbModel = TimeModeDbModel(
        id = entity.id,
        baseTime = entity.baseTime,
        addTime = entity.addTime
    )

    fun mapListDbModelToEntity(listDbModel: List<TimeModeDbModel>): List<TimeMode> = listDbModel.map {
        dbModelToEntity(it)
    }
}