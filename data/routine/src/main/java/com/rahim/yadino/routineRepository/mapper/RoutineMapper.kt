package com.rahim.yadino.routineRepository.mapper

import com.rahim.yadino.routine.model.RoutineModel
import com.rahim.yadino.routineRepository.model.RoutineEntity

fun RoutineModel.toRoutineEntity(): RoutineEntity = RoutineEntity(name = name, colorTask = colorTask, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, timeHours = timeHours, isChecked = isChecked, id = id, explanation = explanation, isSample = isSample)
fun RoutineEntity.toRoutineModel(): RoutineModel = RoutineModel(name = name, colorTask = colorTask, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, timeHours = timeHours, isChecked = isChecked, id = id, explanation = explanation, isSample = isSample)
