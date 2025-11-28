package com.rahim.yadino.base

import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.enums.message.success.SuccessMessage

fun ErrorMessage.toMessageUi(): MessageUi = when (this) {
  ErrorMessage.GET_PROCESS -> MessageUi.GET_PROCESS
  ErrorMessage.EQUAL_ROUTINE_MESSAGE -> MessageUi.EQUAL_ROUTINE_MESSAGE
  ErrorMessage.SAVE_PROSES -> MessageUi.SAVE_PROSES
  ErrorMessage.NOTIFICATION_PERMISSION -> MessageUi.NOTIFICATION_PERMISSION
  ErrorMessage.REMINDER_PERMISSION -> MessageUi.REMINDER_PERMISSION
  ErrorMessage.NOTIFICATION_AND_REMINDER_PERMISSION -> MessageUi.NOTIFICATION_AND_REMINDER_PERMISSION
  ErrorMessage.TIME_PASSED -> MessageUi.TIME_PASSED
  ErrorMessage.SEARCH_ROUTINE -> MessageUi.SEARCH_ROUTINE
}

fun SuccessMessage.toMessageUi(): MessageUi = when (this) {
  SuccessMessage.SAVE_REMINDER -> MessageUi.SAVE_REMINDER
  SuccessMessage.UPDATE_REMINDER -> MessageUi.UPDATE_REMINDER
}
