package legoartist

import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

import javax.imageio.ImageIO
import javax.swing.JComponent

class PhotoCanvas extends JComponent {

  var imagePath: Option[String] = None

  var image = loadScalaImage()

  override def getPreferredSize = {
    new Dimension(image.width, image.height)
  }

  private def loadScalaImage(): Img = {
    val stream = this.getClass.getResourceAsStream("/legoartist/bolt.jpg")
    try {
      loadImage(stream)
    } finally {
      stream.close()
    }
  }

  private def loadFileImage(path: String): Img = {
    val stream = new FileInputStream(path)
    try {
      loadImage(stream)
    } finally {
      stream.close()
    }
  }

  private def loadImage(inputStream: InputStream): Img = {
    val bufferedImage = ImageIO.read(inputStream)
    val width = bufferedImage.getWidth
    val height = bufferedImage.getHeight
    val img = new Img(width, height)
    for (x <- 0 until width; y <- 0 until height) img(x, y) = bufferedImage.getRGB(x, y)
    img
  }

  def reload(): Unit = {
    image = imagePath match {
      case Some(path) => loadFileImage(path)
      case None       => loadScalaImage()
    }
    repaint()
  }

  def loadFile(path: String): Unit = {
    imagePath = Some(path)
    reload()
  }

  def saveFile(path: String): Unit = {
    val stream = new FileOutputStream(path)
    val bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
    for (x <- 0 until image.width; y <- 0 until image.height) bufferedImage.setRGB(x, y, image(x, y))
    ImageIO.write(bufferedImage, "png", stream)
  }

  def convert(distanceFunction: String, hTileCount: Int, vTileCount: Int) {
    reload()
    val dst = new Img(hTileCount * (image.width / hTileCount) + 1,
      vTileCount * (image.height / vTileCount) + 1)
    val mosaic = Mosaic.getMosaic(image, hTileCount, vTileCount, distanceFunction)
    val bricks = Mosaic.getBricks(mosaic)
    Mosaic.drawMosaic(bricks, dst, hTileCount, vTileCount)
    Mosaic.drawBricks(bricks, dst, hTileCount, vTileCount)
    Mosaic.drawColorNumbers(bricks, dst, hTileCount, vTileCount)

    val grouped = bricks.groupBy(b => (b._3, AvailableColors.all.find(c => c.rgba == b._4).get))
    val brickShapes = List(
      List(AvailableBricks.B1x1),
      List(AvailableBricks.B2x2),
      List(AvailableBricks.B1x2, AvailableBricks.B2x1),
      List(AvailableBricks.B1x3, AvailableBricks.B3x1),
      List(AvailableBricks.B2x3, AvailableBricks.B3x2),
      List(AvailableBricks.C1, AvailableBricks.C2, AvailableBricks.C3))
    var total = 0
    brickShapes.foreach { bs =>
      println(bs)
      AvailableColors.all.foreach { c =>
        val count = bs.map(s => grouped.getOrElse((s, c), Nil).size).sum
        if (count > 0) {
          println("\t%s: %d".format(c, count))
          total += count
        }
      }
      println
    }
    println("Total number of pieces: %d".format(total))

    image = dst
    repaint()
  }

  override def paintComponent(gcan: Graphics) = {
    super.paintComponent(gcan)
    val width = image.width
    val height = image.height
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for (x <- 0 until width; y <- 0 until height) bufferedImage.setRGB(x, y, image(x, y))
    gcan.drawImage(bufferedImage, 0, 0, null)
  }

}
