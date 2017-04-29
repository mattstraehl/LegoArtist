package legoartist

object AvailableBricks {

  val all = List(B3x2, B2x3, B2x2, B3x1, B1x3, B2x1, B1x2, C1, C2, C3, B1x1)

  sealed abstract class Brick(val ps: Array[(Int, Int, Array[Border])]) {
    def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit = ps.foreach(
      p => p._3.foreach(b => b.draw(dst, (x + p._1) * dx, (y + p._2) * dy, dx, dy)))
  }

  final case object B3x2 extends Brick(Array(
    (0, 0, Array(N, W)), (1, 0, Array(N)), (2, 0, Array(N, E)),
    (0, 1, Array(S, W)), (1, 1, Array(S)), (2, 1, Array(S, E))))

  final case object B2x3 extends Brick(Array(
    (0, 0, Array(N, W)), (1, 0, Array(N, E)),
    (0, 1, Array(W)), (1, 1, Array(E)),
    (0, 2, Array(S, W)), (1, 2, Array(S, E))))

  final case object B2x2 extends Brick(Array(
    (0, 0, Array(N, W)), (1, 0, Array(N, E)),
    (0, 1, Array(S, W)), (1, 1, Array(S, E))))

  final case object B3x1 extends Brick(Array((0, 0, Array(N, S, W)), (1, 0, Array(N, S)), (2, 0, Array(N, S, E))))

  final case object B1x3 extends Brick(Array((0, 0, Array(N, E, W)), (0, 1, Array(E, W)), (0, 2, Array(S, E, W))))

  final case object B2x1 extends Brick(Array((0, 0, Array(N, S, W)), (1, 0, Array(N, S, E))))

  final case object B1x2 extends Brick(Array((0, 0, Array(N, E, W)), (0, 1, Array(S, E, W))))

  final case object C1 extends Brick(Array((0, 0, Array(N, W)), (1, 0, Array(N, S, E)), (0, 1, Array(S, E, W))))

  final case object C2 extends Brick(Array((0, 0, Array(N, S, W)), (1, 0, Array(N, E)), (1, 1, Array(S, E, W))))

  final case object C3 extends Brick(Array((0, 0, Array(N, E, W)), (0, 1, Array(S, W)), (1, 1, Array(N, S, E))))

  final case object B1x1 extends Brick(Array((0, 0, Array(N, S, E, W))))

  sealed abstract class Border {
    val black = rgba(0, 0, 0, 255)
    def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit
  }

  final case object N extends Border {
    override def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit = (0 until dx) map { i => dst.update(x + i, y, black) }
  }

  final case object S extends Border {
    override def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit = (0 until dx) map { i => dst.update(x + i, y + dy, black) }
  }

  final case object E extends Border {
    override def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit = (0 until dy) map { i => dst.update(x + dx, y + i, black) }
  }

  final case object W extends Border {
    override def draw(dst: Img, x: Int, y: Int, dx: Int, dy: Int): Unit = (0 until dy) map { i => dst.update(x, y + i, black) }
  }

}