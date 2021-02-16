package booleancsg.ops

import booleancsg.geometry.Geometry
import booleancsg.geometry.Intersectable

interface BooleanOp : Intersectable {
    val type: Operator
    val lhs: Geometry
    val rhs: Geometry
}
