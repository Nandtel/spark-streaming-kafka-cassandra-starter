package com.dengo.spark.consumer

import org.apache.log4j.{Level, Logger}

import org.apache.spark.Logging

/**
  * Object SparkLogging
  *
  * Object stores one method setStreamingLogLevels(),
  * which initializes log4j of current spark application
  * and set WARN logger level for it's own logging
  *
  * @author Dmitry Sheremet
  * @since 0.0.1
  */
object SparkLogging extends Logging {

  def setStreamingLogLevels() {
    val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if (!log4jInitialized) {
      logInfo("Setting log level to [WARN] for this app.")
      Logger.getRootLogger.setLevel(Level.WARN)
    }
  }
}