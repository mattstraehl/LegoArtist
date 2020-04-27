package legoartist

/**
 * x Black
 * x Brick yellow
 * x Bright orange
 *   Bright purple
 * x Bright red
 *   Bright yellowish green
 * x Dark green
 * x Dark stone grey
 *   Flame yellowish orange
 * x Medium stone grey
 *   New dark red
 *   Olive green
 * x Reddish brown
 *   Sand yellow
 * x White
 *
 * 4
 * 7
 * 12
 * 18
 * 19
 * 22
 */
object AvailableColors {

  val all = List(BLACK,
    BRICK_YELLOW,
    BRIGHT_BLUE,
    BRIGHT_ORANGE,
    BRIGHT_PURPLE,
    BRIGHT_RED,
    BRIGHT_YELLOW,
    BRIGHT_YELLOWISH_GREEN,
    DARK_AZUR,
    DARK_GREEN,
    DARK_STONE_GREY,
    EARTH_BLUE,
    FLAME_YELLOWISH_ORANGE,
    LIGHT_PURPLE,
    MEDIUM_AZURE,
    MEDIUM_BLUE,
    MEDIUM_LAVENDEL,
    MEDIUM_STONE_GREY,
    NEW_DARK_RED,
    OLIVE_GREEN,
    REDDISH_BROWN,
    SAND_GREEN,
    SAND_YELLOW,
    WHITE)

  sealed abstract class Color(val rgba: RGBA)

  final case object BLACK extends Color(toRGBA("#05131D")) //
  final case object BRICK_YELLOW extends Color(toRGBA("#E4CD9E")) //
  final case object BRIGHT_BLUE extends Color(toRGBA("#0055BF")) //
  final case object BRIGHT_ORANGE extends Color(toRGBA("#FE8A18")) //
  final case object BRIGHT_PURPLE extends Color(toRGBA("#C870A0"))
  final case object BRIGHT_RED extends Color(toRGBA("#C91A09")) //
  final case object BRIGHT_YELLOW extends Color(toRGBA("#F2CD37")) //
  final case object BRIGHT_YELLOWISH_GREEN extends Color(toRGBA("#D9E4A7"))
  final case object DARK_AZUR extends Color(toRGBA("#078BC9"))
  final case object DARK_GREEN extends Color(toRGBA("#237841")) //
  final case object DARK_STONE_GREY extends Color(toRGBA("#6C6E68")) //
  final case object EARTH_BLUE extends Color(toRGBA("#0A3463")) //
  final case object FLAME_YELLOWISH_ORANGE extends Color(toRGBA("#F8BB3D"))
  final case object LIGHT_PURPLE extends Color(toRGBA("#CD6298"))
  final case object MEDIUM_AZURE extends Color(toRGBA("#36AEBF"))
  final case object MEDIUM_BLUE extends Color(toRGBA("#5A93DB"))
  final case object MEDIUM_LAVENDEL extends Color(toRGBA("#AC78BA")) //
  final case object MEDIUM_STONE_GREY extends Color(toRGBA("#A0A5A9")) //
  final case object NEW_DARK_RED extends Color(toRGBA("#720E0F"))
  final case object OLIVE_GREEN extends Color(toRGBA("#9B9A5A"))
  final case object REDDISH_BROWN extends Color(toRGBA("#582A12")) //
  final case object SAND_GREEN extends Color(toRGBA("#A0BCAC"))
  final case object SAND_YELLOW extends Color(toRGBA("#958A73"))
  final case object WHITE extends Color(toRGBA("#FFFFFF")) //

  val altColor = Map[RGBA, RGBA](
    BRIGHT_PURPLE.rgba -> MEDIUM_LAVENDEL.rgba,
    BRIGHT_YELLOWISH_GREEN.rgba -> BRICK_YELLOW.rgba,
    FLAME_YELLOWISH_ORANGE.rgba -> BRIGHT_YELLOW.rgba,
    NEW_DARK_RED.rgba -> REDDISH_BROWN.rgba,
    OLIVE_GREEN.rgba -> DARK_STONE_GREY.rgba,
    SAND_YELLOW.rgba -> DARK_STONE_GREY.rgba)

  val colors = Map[String, RGBA](
    "Black" -> toRGBA("#05131D"),
    "Brick yellow" -> toRGBA("#E4CD9E"),
    "Bright blue" -> toRGBA("#0055BF"),
    //"Bright green" -> toRGBA("#4B9F4A"),
    "Bright orange" -> toRGBA("#FE8A18"),
    "Bright purple" -> toRGBA("#C870A0"),
    "Bright red" -> toRGBA("#C91A09"),
    //"Bright reddish violet" -> toRGBA("#923978"),
    "Bright yellow" -> toRGBA("#F2CD37"),
    "Bright yellowish green" -> toRGBA("#D9E4A7"),
    //"Cool yellow" -> toRGBA("#FFF03A"),
    "Dark azur" -> toRGBA("#078BC9"),
    //"Dark brown" -> toRGBA("#352100"),
    "Dark green" -> toRGBA("#237841"),
    //"Dark orange" -> toRGBA("#A95500"),
    "Dark stone grey" -> toRGBA("#6C6E68"),
    "Earth blue" -> toRGBA("#0A3463"),
    //"Earth green" -> toRGBA("#184632"),
    "Flame yellowish orange" -> toRGBA("#F8BB3D"),
    "Light purple" -> toRGBA("#CD6298"),
    "Medium azure" -> toRGBA("#36AEBF"),
    "Medium blue" -> toRGBA("#5A93DB"),
    "Medium lavendel" -> toRGBA("#AC78BA"),
    //"Medium lilac" -> toRGBA("#3F3691"),
    //"Medium nougat" -> toRGBA("#CC702A"),
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
        all.minBy(c => euclideanDistance(c.rgba, rgba)).rgba
      case "manhattan-distance" =>
        all.minBy(c => manhattanDistance(c.rgba, rgba)).rgba
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
    (0xFF << 24) | Integer.parseInt(color.substring(color.indexOf("#") + 1), 16)
  }

}