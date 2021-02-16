package booleancsg.geometry

import booleancsg.vecmath.Vector3

interface Geometry : Volume, Intersectable {
    var position: Vector3
    var radius: Double
}
