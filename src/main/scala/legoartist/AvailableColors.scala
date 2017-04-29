package legoartist

object AvailableColors {

  val colors = Map[String, RGBA](
    "Black" -> toRGBA("#05131D"),
    "Brick yellow" -> toRGBA("#E4CD9E"),
    "Bright blue" -> toRGBA("#0055BF"),
    "Bright green" -> toRGBA("#4B9F4A"),
    "Bright orange" -> toRGBA("#FE8A18"),
    "Bright purple" -> toRGBA("#C870A0"),
    "Bright red" -> toRGBA("#C91A09"),
    "Bright reddish violet" -> toRGBA("#923978"),
    "Bright yellow" -> toRGBA("#F2CD37"),
    "Bright yellowish green" -> toRGBA("#D9E4A7"),
    "Cool yellow" -> toRGBA("#FFF03A"),
    "Dark azur" -> toRGBA("#078BC9"),
    "Dark brown" -> toRGBA("#352100"),
    "Dark green" -> toRGBA("#237841"),
    "Dark orange" -> toRGBA("#A95500"),
    "Dark stone grey" -> toRGBA("#6C6E68"),
    "Earth blue" -> toRGBA("#0A3463"),
    "Earth green" -> toRGBA("#184632"),
    "Flame yellowish orange" -> toRGBA("#F8BB3D"),
    "Light purple" -> toRGBA("#CD6298"),
    "Medium azure" -> toRGBA("#36AEBF"),
    "Medium blue" -> toRGBA("#5A93DB"),
    "Medium lavendel" -> toRGBA("#AC78BA"),
    "Medium lilac" -> toRGBA("#3F3691"),
    "Medium nougat" -> toRGBA("#CC702A"),
    "Medium stone grey" -> toRGBA("#A0A5A9"),
    "New dark red" -> toRGBA("#720E0F"),
    "Olive green" -> toRGBA("#9B9A5A"),
    "Reddish brown" -> toRGBA("#582A12"),
    "Sand green" -> toRGBA("#A0BCAC"),
    "Sand yellow" -> toRGBA("#958A73"),
    "White" -> toRGBA("#FFFFFF"))

  def closestTo(rgba: RGBA, distanceFunction: String): RGBA = {
    distanceFunction match {
      case "euclidean-distance" =>
        colors.values.minBy(c => euclideanDistance(c, rgba))
      case "manhattan-distance" =>
        colors.values.minBy(c => manhattanDistance(c, rgba))
    }
  }

  private def euclideanDistance(from: RGBA, to: RGBA): Double = {
    val dr = red(from) - red(to)
    val dg = green(from) - green(to)
    val db = blue(from) - blue(to)
    Math.sqrt((dr * dr) + (dg * dg) + (db * db))
  }

  private def manhattanDistance(from: RGBA, to: RGBA): Double = {
    Math.abs(red(from) - red(to)) + Math.abs(green(from) - green(to)) + Math.abs(blue(from) - blue(to));
  }

  private def toRGBA(color: String): RGBA = {
    (0xFF << 24) | Integer.parseInt(color.substring(1), 16)
  }

}