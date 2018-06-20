package com.orange.sophia.route.routine

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

class SparkJobs(sparkUrl: String = "spark:7077",
                format: StructType,
                localisation: String,
                pipeline: Pipeline,
                sparkSession: SparkSession) {

  // TODO : is the data are well formatted ?
  def checkIntegrity(): Unit = {}

  private var model: PipelineModel = _

  def learningJob(): Unit = {
    val data = sparkSession.read.schema(format).json(localisation + "/*")
    model = pipeline.fit(data)
  }

  def classifierJob(): Unit ={



  }

}
