package com.rahim.yadino.base

import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.enums.message.success.SuccessMessage

fun ErrorMessage.toMessageUi(): MessageUi = when (this) {
  ErrorMessage.GET_PROCESS -> MessageUi.ERROR_GET_PROCESS
  ErrorMessage.EQUAL_ROUTINE_MESSAGE -> MessageUi.ERROR_EQUAL_ROUTINE_MESSAGE
  ErrorMessage.SAVE_PROSES -> MessageUi.ERROR_SAVE_REMINDER
  ErrorMessage.NOTIFICATION_PERMISSION -> MessageUi.ERROR_NOTIFICATION_PERMISSION
  ErrorMessage.REMINDER_PERMISSION -> MessageUi.ERROR_REMINDER_PERMISSION
  ErrorMessage.NOTIFICATION_AND_REMINDER_PERMISSION -> MessageUi.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION
  ErrorMessage.TIME_PASSED -> MessageUi.ERROR_TIME_PASSED
  ErrorMessage.SEARCH_ROUTINE -> MessageUi.ERROR_SEARCH_ROUTINE
}

fun SuccessMessage.toMessageUi(): MessageUi = when (this) {
  SuccessMessage.SAVE_REMINDER -> MessageUi.SUCCESS_SAVE_REMINDER
  SuccessMessage.UPDATE_REMINDER -> MessageUi.SUCCESS_UPDATE_REMINDER
}
