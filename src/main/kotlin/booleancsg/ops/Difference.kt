package booleancsg.ops

import booleancsg.geometry.Geometry
import booleancsg.vecmath.Ray

class Difference(override val lhs: Geometry, override val rhs: Geometry) : BooleanOp {
    override val type = Operator.DIFFERENCE

    @Override
    override fun intersect(ray: Ray, near: Boolean): Ray {
        val lhsNear = lhs.intersect(ray, near)
        val lhsFar = lhs.intersect(ray, !near)
        val rhsNear = rhs.intersect(ray, near)
        val rhsFar = rhs.intersect(ray, !near)
        if (lhsNear == Ray.NULL) return Ray.NULL
        if (rhsNear == Ray.NULL) return lhsNear
        val lhsMin = (lhsNear.origin - ray.origin).lengthSquared
        val rhsMin = (rhsNear.origin - ray.origin).lengthSquared
        val rhsMax = (rhsFar.origin - ray.origin).lengthSquared
        if (lhsMin < rhsMin || rhsMax < lhsMin) return lhsNear
        val lhsMax = (lhsFar.origin - ray.origin).lengthSquared
        if (rhsMax < lhsMax) return Ray(rhsFar.origin, -rhsFar.direction)
        return Ray.NULL
    }
}
