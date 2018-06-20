package com.orange.sophia.route.routine.integrity

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataType, StructType}

class DataIntegrityRoutine(sparkUrl : String = "spark:7077",
                           format : List[String],
                           localisation : List[String],
                           sparkSession: SparkSession) {

  def checkIntegrity(): Unit ={
    val formated = format.map(DataType.fromJson(_).asInstanceOf[StructType])
    localisation.foreach(loc => {
      formated.map(format => {
        try {
          sparkSession.read.schema(format).json(loc)
          true
        }
        catch {
          case Exception => false
        }
      })
    }
   )
  }


}
