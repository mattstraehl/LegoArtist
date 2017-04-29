package object legoartist {

  type RGBA = Int

  type Tile = (Int, Int, RGBA)

  type TileGroup = List[Tile]

  type BrickPlacement = (Int, Int, AvailableBricks.Brick)

  def alpha(c: RGBA): Int = (0xff000000 & c) >>> 24

  def red(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  def green(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  def blue(c: RGBA): Int = (0x000000ff & c) >>> 0

  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (a << 24) | (r << 16) | (g << 8) | (b << 0)
  }

  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))
    def apply(x: Int, y: Int): RGBA = data(y * width + x)
    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  def average(src: Img, x: Int, y: Int, dx: Int, dy: Int): RGBA = {
    val is = clamp(x, 0, src.width - 1) to clamp(x + dx, 0, src.width - 1)
    val js = clamp(y, 0, src.height - 1) to clamp(y + dy, 0, src.height - 1)
    val data = is flatMap { i =>
      js map { j =>
        src(i, j)
      }
    }
    val (r, g, b, a) = data.foldLeft((0, 0, 0, 0)) { (agg, rgba) =>
      (agg._1 + red(rgba), agg._2 + green(rgba), agg._3 + blue(rgba), agg._4 + alpha(rgba))
    }
    rgba(r / data.length, g / data.length, b / data.length, a / data.length)
  }

}
