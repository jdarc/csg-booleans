package booleancsg.geometry

import booleancsg.vecmath.Ray

interface Intersectable {
    fun intersect(ray: Ray, near: Boolean = true): Ray
}

