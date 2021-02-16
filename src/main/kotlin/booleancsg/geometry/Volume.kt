package booleancsg.geometry

import booleancsg.vecmath.Vector3

interface Volume {
    fun contains(point: Vector3): Boolean
}

