package legoartist

import scala.Stream
import scala.collection.mutable.ListBuffer
import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.Font

object Mosaic {

  trait Node[A] {
    def children: Stream[A with Node[A]]

    def dfs(f: A => Boolean): Stream[A] = this.children.flatMap {
      child => if (f(child)) Stream(child) else child.dfs(f)
    }
  }

  final class Layout(val tileGroup: TileGroup, val placements: List[BrickPlacement]) extends Node[Layout] {
    def children: Stream[Layout with Node[Layout]] = {
      val next = tileGroup.head
      AvailableBricks.forColor(next._3).toStream
        .filter(b => b.ps.forall(p => tileGroup.map(t => (t._1, t._2)).contains((p._1 + next._1, p._2 + next._2))))
        .map { b =>
          val tg = tileGroup.filter(t => !b.ps.map(p => (p._1 + next._1, p._2 + next._2)).contains((t._1, t._2)))
          val ps = (next._1, next._2, b, next._3) :: placements
          new Layout(tg, ps)
        }
    }
  }

  def getMosaic(src: Img, hTileCount: Int, vTileCount: Int, distanceFunction: String): Array[Array[RGBA]] = {
    val dx = src.width / hTileCount
    val dy = src.height / vTileCount
    Array.tabulate(hTileCount, vTileCount)((i, j) => AvailableColors.closestTo(
      average(src, i * dx, j * dy, dx, dy), distanceFunction))
  }

  def getBricks(mosaic: Array[Array[RGBA]]): List[BrickPlacement] = {
    val tileGroups = getTileGroups(mosaic)
    val layouts = getLayouts(tileGroups)
    val bricks = layouts.flatMap(_.placements)
    bricks.map(b => if (b._3 == AvailableBricks.B1x1) (b._1, b._2, b._3, AvailableColors.altColor.getOrElse(b._4, b._4)) else b)
  }

  @deprecated
  def drawMosaic(dst: Img, colors: Array[Array[RGBA]]): Unit = {
    val dx = dst.width / colors.length
    val dy = dst.height / colors(0).length
    colors.zipWithIndex.foreach { cs =>
      cs._1.zipWithIndex.foreach { c =>
        fill(dst, cs._2 * dx, c._2 * dy, dx, dy, c._1)
      }
    }
  }

  def drawMosaic(bricks: List[BrickPlacement], dst: Img, hTileCount: Int, vTileCount: Int): Unit = {
    val dx = dst.width / hTileCount
    val dy = dst.height / vTileCount
    bricks.foreach(b => b._3.ps.foreach(p => fill(dst, (b._1 + p._1) * dx, (b._2 + p._2) * dy, dx, dy, b._4)))
  }

  def drawBricks(bricks: List[BrickPlacement], dst: Img, hTileCount: Int, vTileCount: Int): Unit = {
    val dx = dst.width / hTileCount
    val dy = dst.height / vTileCount
    bricks.foreach(b => b._3.draw(dst, b._1, b._2, dx, dy))
  }

  def drawColorNumbers(bricks: List[BrickPlacement], dst: Img, hTileCount: Int, vTileCount: Int): Unit = {
    val dx = dst.width / hTileCount
    val dy = dst.height / vTileCount
    val rgbas = AvailableColors.all.map(_.rgba)
    bricks.foreach { b =>
      val bufferedImage = new BufferedImage(dx, dy, BufferedImage.TYPE_INT_ARGB)
      val graphics = bufferedImage.createGraphics
      //graphics.setColor(Color.BLACK)
      graphics.setPaint(Color.BLACK);
      graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
      graphics.drawString(rgbas.indexOf(b._4).toString, 2, 11)
      fill(dst, b._1 * dx, b._2 * dy, bufferedImage)
      graphics.dispose
    }
  }

  private def getTileGroups(mosaic: Array[Array[RGBA]]): List[TileGroup] = {
    val result = scala.collection.mutable.Map[Tile, ListBuffer[Tile]]()
    for (i <- mosaic.indices) {
      for (j <- mosaic(i).indices) {
        val rgba = mosaic(i)(j)
        val group = result.getOrElse((i, j - 1, rgba), result.getOrElse((i - 1, j, rgba), new ListBuffer()))
        group += ((i, j, rgba))
        result += ((i, j, rgba) -> group)
      }
    }
    result.values.map(_.toList).toList.distinct
  }

  private def getLayouts(tileGroups: List[TileGroup]): List[Layout] = {
    tileGroups.par.map(tg => new Layout(tg, Nil).dfs(_.tileGroup.isEmpty).take(1000).minBy(x =>
      //x.placements.map(p => AvailableBricks.prices(p._3)).sum
      x.placements.size + x.placements.filter(p => p._3 == AvailableBricks.B1x1).size
      //x.placements.size
      )).toList
  }

  private def fill(dst: Img, x: Int, y: Int, dx: Int, dy: Int, rgba: RGBA): Unit = {
    (0 until dx) foreach { i =>
      (0 until dy) foreach { j =>
        dst.update(x + i, y + j, rgba)
      }
    }
  }

  private def fill(dst: Img, x: Int, y: Int, bufferedImage: BufferedImage): Unit = {
    (0 until bufferedImage.getWidth) foreach { i =>
      (0 until bufferedImage.getHeight) foreach { j =>
        if (bufferedImage.getRGB(i, j) != 0)
          dst.update(x + i, y + j, bufferedImage.getRGB(i, j))
      }
    }
  }

}
