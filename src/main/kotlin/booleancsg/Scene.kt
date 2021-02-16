package booleancsg

import booleancsg.geometry.Box
import booleancsg.geometry.Sphere
import booleancsg.ops.*
import booleancsg.vecmath.Matrix4
import booleancsg.vecmath.Ray
import booleancsg.vecmath.Vector3
import java.awt.Color
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.floor
import kotlin.math.min

class Scene {
    private val tasks = mutableListOf<Callable<Any>>()
    private var rendering = false
    private var transform = Matrix4.rotationX(0.5) * Matrix4.rotationY(0.5)
    private var backColor = Vector3(40.0, 30.0, 50.0)
    private var observer = transform * Vector3(0.0, 0.0, -120.0)
    private val box = Box(Vector3(0.0, 0.0, 0.0), 40.0)
    private val sphere = Sphere(Vector3(0.0, 0.0, 0.0), 50.0)
    private var op: BooleanOp = Difference(box, sphere)
    private val lights = randomLights()

    var operator
        get() = op.type
        set(value) {
            op = when (value) {
                Operator.DIFFERENCE -> Difference(op.lhs, op.rhs)
                Operator.INTERSECTION -> Intersection(op.lhs, op.rhs)
                else -> Union(op.lhs, op.rhs)
            }
        }

    fun moveObserver(yaw: Double, pitch: Double) {
        this.transform = Matrix4.rotationX(pitch) * Matrix4.rotationY(yaw)
        observer = transform * Vector3(0.0, 0.0, -120.0)
        lights.first().position = observer
    }

    fun moveGeometry(x: Double, y: Double, z: Double) {
        op.rhs.position = Vector3(x, y, z)
    }

    fun resizeGeometry(radius: Double) {
        op.rhs.radius = radius
    }

    fun render(pixels: IntArray, width: Int, height: Int) {
        if (rendering) return
        rendering = true
        if (tasks.isEmpty()) buildTasks(pixels, width, height)
        ForkJoinPool.commonPool().invokeAll(tasks)
        rendering = false
    }

    fun randomizeLights() {
        lights.clear()
        lights.addAll(randomLights())
    }

    private fun randomLights(): MutableList<Light> {
        val lights = mutableListOf<Light>()
        lights.add(Light(observer, randomColor(64, 128)))
        for (i in 1 until 5) lights.add(Light(randomVector(), randomColor(32, 200)))
        return lights
    }

    private fun randomVector(): Vector3 {
        val rnd = ThreadLocalRandom.current()
        val x = rnd.nextDouble(-1.0, 1.0)
        val y = rnd.nextDouble(-1.0, 1.0)
        val z = rnd.nextDouble(-1.0, 1.0)
        val v = Vector3.normalize(Vector3(x, y, z))
        return v * rnd.nextDouble(200.0, 300.0)
    }

    private fun randomColor(min: Int, max: Int = 255): Color {
        val rnd = ThreadLocalRandom.current()
        val red = rnd.nextInt(min, max).coerceIn(0, 255)
        val grn = rnd.nextInt(min, max).coerceIn(0, 255)
        val blu = rnd.nextInt(min, max).coerceIn(0, 255)
        return Color(red, grn, blu)
    }

    private fun buildTasks(pixels: IntArray, width: Int, height: Int) {

        val hW = width / 2.0
        val hH = height / 2.0
        for (j in 0 until height step BLOCK_SIZE) {
            for (i in 0 until width step BLOCK_SIZE) {
                tasks.add(Executors.callable {
                    for (y in j until min(height, j + BLOCK_SIZE)) {
                        for (x in i until min(width, i + BLOCK_SIZE)) {
                            var red = 0.0
                            var grn = 0.0
                            var blu = 0.0
                            val direction = transform * Vector3(x - hW, hH - y, 500.0)
                            val intersection = op.intersect(Ray.create(observer, direction))
                            if (intersection == Ray.NULL) {
                                red = backColor.x
                                grn = backColor.y
                                blu = backColor.z
                            } else {
                                lights.forEach { light ->
                                    val toLight = Vector3.normalize(light.position - intersection.origin)
                                    val intensity = Vector3.dot(toLight, intersection.direction)
                                    if (intensity > 0.0) {
                                        red += intensity * (255 and light.color.rgb.shr(16))
                                        grn += intensity * (255 and light.color.rgb.shr(8))
                                        blu += intensity * (255 and light.color.rgb)
                                    }
                                }
                            }
                            val r = floor(red).toInt().coerceIn(0, 255)
                            val g = floor(grn).toInt().coerceIn(0, 255)
                            val b = floor(blu).toInt().coerceIn(0, 255)
                            pixels[y * width + x] = 255 shl 24 or (r shl 16) or (g shl 8) or b
                        }
                    }
                })
            }
        }
    }

    companion object {
        const val BLOCK_SIZE = 32
        const val TOLERANCE = 0.000000001
    }
}
