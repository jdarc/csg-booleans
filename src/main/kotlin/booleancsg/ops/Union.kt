package booleancsg.ops

import booleancsg.geometry.Geometry
import booleancsg.vecmath.Ray

class Union(override val lhs: Geometry, override val rhs: Geometry) : BooleanOp {

    override val type = Operator.UNION

    @Override
    override fun intersect(ray: Ray, near: Boolean): Ray {
        val lhsNear = lhs.intersect(ray, near)
        val rhsNear = rhs.intersect(ray, near)
        if (lhsNear == Ray.NULL) return rhsNear
        if (rhsNear == Ray.NULL) return lhsNear
        val lhsMin = (lhsNear.origin - ray.origin).lengthSquared
        val rhsMin = (rhsNear.origin - ray.origin).lengthSquared
        return if (lhsMin < rhsMin) lhsNear else rhsNear
    }
}
