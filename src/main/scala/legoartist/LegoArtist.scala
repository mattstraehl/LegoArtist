package legoartist

import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.SpinnerNumberModel
import javax.swing.UIManager
import javax.swing.border

object LegoArtist {

  class LegoArtistFrame extends JFrame("LegoArtist") {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(1200, 900)
    setLayout(new BorderLayout)

    val rightpanel = new JPanel
    rightpanel.setBorder(BorderFactory.createEtchedBorder(border.EtchedBorder.LOWERED))
    rightpanel.setLayout(new BorderLayout)
    add(rightpanel, BorderLayout.EAST)

    val controls = new JPanel
    controls.setLayout(new GridLayout(0, 2))
    rightpanel.add(controls, BorderLayout.NORTH)

    val distanceFunctionLabel = new JLabel("Distance Function")
    controls.add(distanceFunctionLabel)

    val distanceFunctionCombo = new JComboBox(Array(
      "euclidean-distance",
      "manhattan-distance"))
    controls.add(distanceFunctionCombo)

    val hTileCountLabel = new JLabel("Horizontal Tile Count")
    controls.add(hTileCountLabel)

    val hTileCountSpinner = new JSpinner(new SpinnerNumberModel(48, 2, 1024, 1))
    controls.add(hTileCountSpinner)

    val vTileCountLabel = new JLabel("Vertical Tile Count")
    controls.add(vTileCountLabel)

    val vTileCountSpinner = new JSpinner(new SpinnerNumberModel(48, 2, 1024, 1))
    controls.add(vTileCountSpinner)

    val stepbutton = new JButton("Convert")
    stepbutton.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        canvas.convert(getDistanceFunction, getHorizontalTileCount, getVerticalTileCount)
      }
    })
    controls.add(stepbutton)

    val clearButton = new JButton("Reload")
    clearButton.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        canvas.reload()
      }
    })
    controls.add(clearButton)

    val mainMenuBar = new JMenuBar()

    val fileMenu = new JMenu("File")
    val openMenuItem = new JMenuItem("Open...")
    openMenuItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        val fc = new JFileChooser()
        if (fc.showOpenDialog(LegoArtistFrame.this) == JFileChooser.APPROVE_OPTION) {
          canvas.loadFile(fc.getSelectedFile.getPath)
        }
      }
    })
    fileMenu.add(openMenuItem)
    val saveMenuItem = new JMenuItem("Save...")
    saveMenuItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        val fc = new JFileChooser()
        if (fc.showSaveDialog(LegoArtistFrame.this) == JFileChooser.APPROVE_OPTION) {
          canvas.saveFile(fc.getSelectedFile.getPath)
        }
      }
    })
    fileMenu.add(saveMenuItem)
    val exitMenuItem = new JMenuItem("Exit")
    exitMenuItem.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        sys.exit(0)
      }
    })
    fileMenu.add(exitMenuItem)

    mainMenuBar.add(fileMenu)

    setJMenuBar(mainMenuBar)

    val canvas = new PhotoCanvas

    val scrollPane = new JScrollPane(canvas)

    add(scrollPane, BorderLayout.CENTER)
    setVisible(true)

    def getHorizontalTileCount: Int = hTileCountSpinner.getValue.asInstanceOf[Int]

    def getVerticalTileCount: Int = vTileCountSpinner.getValue.asInstanceOf[Int]

    def getDistanceFunction: String = {
      distanceFunctionCombo.getSelectedItem.asInstanceOf[String]
    }

  }

  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  } catch {
    case _: Exception => println("Failed to set look and feel.")
  }

  val frame = new LegoArtistFrame

  def main(args: Array[String]) {
    frame.repaint()
  }

}
