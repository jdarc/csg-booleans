package booleancsg.geometry

import booleancsg.Scene.Companion.TOLERANCE
import booleancsg.vecmath.MathKit.max
import booleancsg.vecmath.MathKit.min
import booleancsg.vecmath.Ray
import booleancsg.vecmath.Vector3

class Box(override var position: Vector3, override var radius: Double) : Geometry {
    private val minimum get() = position - Vector3(radius, radius, radius)
    private val maximum get() = position + Vector3(radius, radius, radius)

    @Override
    override fun intersect(ray: Ray, near: Boolean): Ray {
        val minimum = this.minimum
        val maximum = this.maximum
        var tNear = -Double.MAX_VALUE
        var tFar = Double.MAX_VALUE

        when (ray.direction.x) {
            0.0 -> if (ray.origin.x < minimum.x || ray.origin.x > maximum.x) return Ray.NULL
            else -> {
                val reciprocal = 1.0 / ray.direction.x
                val t1 = (minimum.x - ray.origin.x) * reciprocal
                val t2 = (maximum.x - ray.origin.x) * reciprocal
                if (t1 < t2) {
                    tNear = max(t1, tNear)
                    tFar = min(t2, tFar)
                } else {
                    tNear = max(t2, tNear)
                    tFar = min(t1, tFar)
                }
                if (tNear > tFar || tFar < 0) return Ray.NULL
            }
        }

        when (ray.direction.y) {
            0.0 -> if (ray.origin.y < minimum.y || ray.origin.y > maximum.y) return Ray.NULL
            else -> {
                val reciprocal = 1.0 / ray.direction.y
                val t1 = (minimum.y - ray.origin.y) * reciprocal
                val t2 = (maximum.y - ray.origin.y) * reciprocal
                if (t1 < t2) {
                    tNear = max(t1, tNear)
                    tFar = min(t2, tFar)
                } else {
                    tNear = max(t2, tNear)
                    tFar = min(t1, tFar)
                }
                if (tNear > tFar || tFar < 0) {
                    return Ray.NULL
                }
            }
        }

        when (ray.direction.z) {
            0.0 -> if (ray.origin.z < minimum.z || ray.origin.z > maximum.z) return Ray.NULL
            else -> {
                val reciprocal = 1.0 / ray.direction.z
                val t1 = (minimum.z - ray.origin.z) * reciprocal
                val t2 = (maximum.z - ray.origin.z) * reciprocal
                if (t1 < t2) {
                    tNear = max(t1, tNear)
                    tFar = min(t2, tFar)
                } else {
                    tNear = max(t2, tNear)
                    tFar = min(t1, tFar)
                }
                if (tNear > tFar || tFar < 0) return Ray.NULL
            }
        }

        val boxIntersect = ray.origin + ray.direction * if (near) tNear else tFar
        val surfaceNormal = when {
            boxIntersect.x <= minimum.x + TOLERANCE -> Vector3.LEFT
            boxIntersect.x >= maximum.x - TOLERANCE -> Vector3.RIGHT
            boxIntersect.y <= minimum.y + TOLERANCE -> Vector3.DOWN
            boxIntersect.y >= maximum.y - TOLERANCE -> Vector3.UP
            boxIntersect.z <= minimum.z + TOLERANCE -> Vector3.BACK
            else -> Vector3.FRONT
        }
        return Ray.create(boxIntersect, surfaceNormal)
    }

    @Override
    override fun contains(point: Vector3): Boolean {
        val minimum = minimum
        val maximum = maximum
        val x1 = point.x > minimum.x
        val x2 = point.x < maximum.x
        val y1 = point.y > minimum.y
        val y2 = point.y < maximum.y
        val z1 = point.z > minimum.z
        val z2 = point.z < maximum.z
        return x2 && y2 && z2 && x1 && y1 && z1
    }
}

