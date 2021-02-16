package booleancsg

import booleancsg.ops.Operator
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.border.EmptyBorder

class MainFrame : JFrame("Constructive Solid Geometry Booleans") {
    private val image = BufferedImage(960, 700, BufferedImage.TYPE_INT_ARGB_PRE)
    private val pixels = (image.raster.dataBuffer as DataBufferInt).data
    private val viewport = Viewport(image)
    private val scene = Scene()

    private fun render() {
        scene.render(pixels, image.width, image.height)
        viewport.repaint()
    }

    private fun changeOp(operator: Operator) {
        scene.operator = operator
        render()
    }

    private fun randomizeLights() {
        scene.randomizeLights()
        render()
    }

    private fun moveGeometry(x: Double, y: Double, z: Double) {
        scene.moveGeometry(x, y, z)
        render()
    }

    private fun resizeGeometry(radius: Double) {
        scene.resizeGeometry(radius)
        render()
    }

    private fun updateObserver(yaw: Double, pitch: Double) {
        scene.moveObserver(yaw, pitch)
        render()
    }

    private fun buildNorthPanel(): JPanel {
        val panel = JPanel()
        panel.border = EmptyBorder(4, 4, 4, 4)
        panel.layout = BorderLayout()

        JSlider(JSlider.HORIZONTAL, 10, 100, 50).apply { font = Font("", 0, 0); name = "Radius" }.apply {
            this.addChangeListener { resizeGeometry(this.value * 1.0) }
            panel.add(buildSliderPanel(this), BorderLayout.CENTER)
        }

        val buttonsPanel = JPanel()
        buttonsPanel.layout = BoxLayout(buttonsPanel, BoxLayout.X_AXIS)
        buttonsPanel.add(buildOperatorsPanel())
        buttonsPanel.add(Box.createHorizontalStrut(4))
        JButton(ImageIcon(loadImage("bulb.png"))).apply {
            this.isFocusPainted = false
            this.addActionListener { randomizeLights() }
            buttonsPanel.add(this, BorderLayout.WEST)
        }
        panel.add(buttonsPanel, BorderLayout.EAST)

        return panel
    }

    private fun loadImage(name: String) = ImageIO.read(this::class.java.classLoader.getResource(name))

    private fun buildOperatorsPanel(): JPanel {
        val operatorPanel = JPanel()
        operatorPanel.layout = BoxLayout(operatorPanel, BoxLayout.X_AXIS)
        val buttonGroup = ButtonGroup()
        JToggleButton(ImageIcon(loadImage("or.png"))).apply {
            this.isFocusPainted = false
            this.addActionListener { changeOp(Operator.UNION) }
            operatorPanel.add(this, BorderLayout.EAST)
            buttonGroup.add(this)
        }
        operatorPanel.add(Box.createHorizontalStrut(1))
        JToggleButton(ImageIcon(loadImage("and.png"))).apply {
            this.isFocusPainted = false
            this.addActionListener { changeOp(Operator.INTERSECTION) }
            operatorPanel.add(this, BorderLayout.EAST)
            buttonGroup.add(this)
        }
        operatorPanel.add(Box.createHorizontalStrut(1))
        JToggleButton(ImageIcon(loadImage("not.png"))).apply {
            this.isFocusPainted = false
            this.addActionListener { changeOp(Operator.DIFFERENCE) }
            operatorPanel.add(this, BorderLayout.EAST)
            buttonGroup.add(this)
        }.isSelected = true
        return operatorPanel
    }

    private fun buildAxisSliders(): JComponent {
        val xAxis = buildSlider("X-Axis")
        val yAxis = buildSlider("Y-Axis")
        val zAxis = buildSlider("Z-Axis")
        val moveSphereFn = { moveGeometry(xAxis.value * 1.0, yAxis.value * 1.0, zAxis.value * 1.0) }
        xAxis.addChangeListener { moveSphereFn() }
        yAxis.addChangeListener { moveSphereFn() }
        zAxis.addChangeListener { moveSphereFn() }

        val panel = JPanel()
        panel.border = EmptyBorder(4, 4, 4, 4)
        panel.layout = GridLayout(3, 1)
        panel.add(buildSliderPanel(xAxis))
        panel.add(buildSliderPanel(yAxis))
        panel.add(buildSliderPanel(zAxis))
        return panel
    }

    private fun buildSlider(label: String) = JSlider(JSlider.HORIZONTAL, -100, 100, 0).apply {
        majorTickSpacing = 5
        minorTickSpacing = 1
        paintTicks = false
        paintLabels = false
        font = Font("", 0, 0)
        name = label
    }

    private fun buildSliderPanel(slider: JComponent): JPanel {
        val panel = JPanel()
        panel.border = EmptyBorder(1, 8, 1, 1)
        panel.layout = BorderLayout()
        panel.add(JLabel(slider.name), BorderLayout.WEST)
        panel.add(slider, BorderLayout.CENTER)
        return panel
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        contentPane.add(buildNorthPanel(), BorderLayout.NORTH)
        contentPane.add(viewport, BorderLayout.CENTER)
        contentPane.add(buildAxisSliders(), BorderLayout.SOUTH)
        pack()
        setLocationRelativeTo(null)
        isResizable = false
        isVisible = true
        render()

        addMouseMotionListener(object : MouseAdapter() {
            private val last = Point(0, 0)
            private var yaw = 0.5
            private var pitch = 0.5

            @Override
            override fun mouseMoved(e: MouseEvent) {
                last.setLocation(e.x, e.y)
                updateObserver(yaw, pitch)
            }

            @Override
            override fun mouseDragged(e: MouseEvent) {
                yaw += (e.x - last.x) * 0.01
                pitch += (e.y - last.y) * 0.01
                pitch = pitch.coerceIn(-1.5, 1.5)
                last.setLocation(e.x, e.y)
                updateObserver(yaw, pitch)
            }
        })
    }
}
