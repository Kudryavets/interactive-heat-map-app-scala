package observatory

import com.sksamuel.scrimage.Image

import scala.collection.parallel.immutable.ParVector
import scala.math._

/**
  * 2nd milestone: basic visualization
  */
object Visualization {
  val DEFAULT_POWER_PARAMETER = 2D

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val tempVec = temperatures.toVector.par

    tempVec.find{ case (loc, _) => loc == location }.map{ case (_, temp) => temp}
      .getOrElse(predictTemperatureImpl(tempVec, location, DEFAULT_POWER_PARAMETER))
  }

  def predictTemperatureImpl(temperatures: ParVector[(Location, Double)],
                             location: Location,
                             p: Double): Double = {
    val (sumWeightedTemps, sumWeghts) = temperatures
      .map{ case (loc, temp) => (temp, 1 / pow(distance(loc2Rad(loc), loc2Rad(location)), p))}
      .aggregate(0D, 0D) (
        (acc, tempWeigtPair) => (acc._1 + tempWeigtPair._1 * tempWeigtPair._2, acc._2 + tempWeigtPair._2),
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
      )

    sumWeightedTemps / sumWeghts
  }

  def distance(location1: Location, location2: Location): Double =
    acos(
      sin(location1.lat) * sin(location2.lat) + cos(location1.lat) * cos(location2.lat) * cos(
        abs(location1.lon - location2.lon)
      )
    )

  def loc2Rad(loc: Location): Location = Location(toRadians(loc.lat), toRadians(loc.lon))


  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

