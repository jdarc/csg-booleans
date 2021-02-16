package booleancsg.vecmath

import java.lang.Math.fma
import kotlin.math.sqrt

class Vector3(val x: Double, val y: Double, val z: Double) {

    val lengthSquared = fma(x, x, fma(y, y, z * z))

    val length = sqrt(lengthSquared)

    operator fun plus(rhs: Vector3) = Vector3(x + rhs.x, y + rhs.y, z + rhs.z)

    operator fun minus(rhs: Vector3) = Vector3(x - rhs.x, y - rhs.y, z - rhs.z)

    operator fun times(scalar: Double) = Vector3(x * scalar, y * scalar, z * scalar)

    operator fun div(scalar: Double) = Vector3(x / scalar, y / scalar, z / scalar)

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    companion object {
        val ZERO = Vector3(0.0, 0.0, 0.0)
        val LEFT = Vector3(-1.0, 0.0, 0.0)
        val RIGHT = Vector3(1.0, 0.0, 0.0)
        val DOWN = Vector3(0.0, -1.0, 0.0)
        val UP = Vector3(0.0, 1.0, 0.0)
        val BACK = Vector3(0.0, 0.0, -1.0)
        val FRONT = Vector3(0.0, 0.0, 1.0)

        fun dot(lhs: Vector3, rhs: Vector3) = fma(lhs.x, rhs.x, fma(lhs.y, rhs.y, lhs.z * rhs.z))

        fun normalize(vec: Vector3) = vec / vec.length
    }
}
