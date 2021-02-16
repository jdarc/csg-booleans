package booleancsg.ops

import booleancsg.geometry.Geometry
import booleancsg.vecmath.Ray

class Intersection(override val lhs: Geometry, override val rhs: Geometry) : BooleanOp {

    override val type = Operator.INTERSECTION

    @Override
    override fun intersect(ray: Ray, near: Boolean): Ray {
        val lhsNear = lhs.intersect(ray, near)
        if (lhsNear != Ray.NULL) {
            val rhsNear = rhs.intersect(ray, near)
            if (rhsNear != Ray.NULL) {
                val lhsFar = lhs.intersect(ray, !near)
                val rhsFar = rhs.intersect(ray, !near)
                val lhsMin = (lhsNear.origin - ray.origin).lengthSquared
                val lhsMax = (lhsFar.origin - ray.origin).lengthSquared
                val rhsMin = (rhsNear.origin - ray.origin).lengthSquared
                if (lhsMin < rhsMin && lhsMax > rhsMin) return rhsNear
                val rhsMax = (rhsFar.origin - ray.origin).lengthSquared
                if (rhsMin < lhsMin && rhsMax > lhsMin) return lhsNear
                return Ray.NULL
            }
        }
        return Ray.NULL
    }
}
