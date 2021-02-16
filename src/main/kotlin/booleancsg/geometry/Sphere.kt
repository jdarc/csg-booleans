package booleancsg.geometry

import booleancsg.vecmath.MathKit.max
import booleancsg.vecmath.MathKit.min
import booleancsg.vecmath.Ray
import booleancsg.vecmath.Vector3
import kotlin.math.sqrt

class Sphere(override var position: Vector3, radius: Double) : Geometry {

    override var radius = radius
        set(value) {
            field = max(Double.MIN_VALUE, value)
        }

    @Override
    override fun intersect(ray: Ray, near: Boolean): Ray {
        val oc = ray.origin - position
        val b = 2.0 * Vector3.dot(ray.direction, oc)
        val discriminant = b * b - 4.0 * (oc.lengthSquared - radius * radius)
        if (discriminant > 0.0) {
            val d = sqrt(discriminant)
            val t1 = -b - d
            val t2 = -b + d
            val t = if (near) min(t1, t2) else max(t1, t2)
            if (t > 0.0) {
                val origin = ray.getPoint(t * 0.5)
                return Ray(origin, (origin - position) / radius)
            }
        }
        return Ray.NULL
    }

    @Override
    override fun contains(point: Vector3) = (point - position).length <= radius

}
