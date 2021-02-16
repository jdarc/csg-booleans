package booleancsg

import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class Viewport(private val image: BufferedImage) : JPanel() {

    @Override
    override fun paintComponent(g: Graphics) {
        g.drawImage(image, 0, 0, this)
    }

    init {
        size = Dimension(image.width, image.height)
        preferredSize = size
    }
}
