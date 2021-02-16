package booleancsg.vecmath

import java.lang.Math.fma

class Ray(val origin: Vector3, val direction: Vector3) {

    fun getPoint(t: Double): Vector3 {
        val x = fma(direction.x, t, origin.x)
        val y = fma(direction.y, t, origin.y)
        val z = fma(direction.z, t, origin.z)
        return Vector3(x, y, z)
    }

    companion object {
        val NULL = Ray(Vector3.ZERO, Vector3.ZERO)

        fun create(origin: Vector3, direction: Vector3) = Ray(origin, Vector3.normalize(direction))
    }
}
