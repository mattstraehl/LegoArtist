package legoartist

import scala.Stream
import scala.collection.mutable.ListBuffer

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
      AvailableBricks.all.toStream
        .filter(b => b.ps.forall(p => tileGroup.map(t => (t._1, t._2)).contains((p._1 + next._1, p._2 + next._2))))
        .map { b =>
          val tg = tileGroup.filter(t => !b.ps.map(p => (p._1 + next._1, p._2 + next._2)).contains((t._1, t._2)))
          val ps = (next._1, next._2, b) :: placements
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
    layouts.flatMap(_.placements)
  }

  def drawMosaic(dst: Img, colors: Array[Array[RGBA]]): Unit = {
    val dx = dst.width / colors.length
    val dy = dst.height / colors(0).length
    colors.zipWithIndex.foreach { cs =>
      cs._1.zipWithIndex.foreach { c =>
        fill(dst, cs._2 * dx, c._2 * dy, dx, dy, c._1)
      }
    }
  }

  def drawBricks(bricks: List[BrickPlacement], dst: Img, hTileCount: Int, vTileCount: Int): Unit = {
    val dx = dst.width / hTileCount
    val dy = dst.height / vTileCount
    bricks.foreach(b => b._3.draw(dst, b._1, b._2, dx, dy))
  }

  private def getTileGroups(mosaic: Array[Array[RGBA]]): List[TileGroup] = {
    val result = scala.collection.mutable.Map[Tile, ListBuffer[Tile]]()
    for (i <- mosaic.indices) {
      for (j <- mosaic(i).indices) {
        val rgba = mosaic(i)(j)
        val group = result.getOrElse((i - 1, j, rgba), result.getOrElse((i, j - 1, rgba), new ListBuffer()))
        group += ((i, j, rgba))
        result += ((i, j, rgba) -> group)
      }
    }
    result.values.map(_.toList).toList.distinct
  }

  private def getLayouts(tileGroups: List[TileGroup]): List[Layout] = {
    tileGroups.par.map(tg => new Layout(tg, Nil).dfs(_.tileGroup.isEmpty).take(1000).minBy(x => x.placements.size)).toList
  }

  private def fill(dst: Img, x: Int, y: Int, dx: Int, dy: Int, rgba: RGBA): Unit = {
    (0 until dx) foreach { i =>
      (0 until dy) foreach { j =>
        dst.update(x + i, y + j, rgba)
      }
    }
  }

}
