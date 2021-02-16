package booleancsg.vecmath

import java.lang.Math.fma
import kotlin.math.cos
import kotlin.math.sin

class Matrix4(
    val m00: Double, val m01: Double, val m02: Double, val m03: Double,
    val m10: Double, val m11: Double, val m12: Double, val m13: Double,
    val m20: Double, val m21: Double, val m22: Double, val m23: Double,
    val m30: Double, val m31: Double, val m32: Double, val m33: Double
) {

    operator fun times(rhs: Matrix4) = Matrix4(
        fma(m00, rhs.m00, fma(m01, rhs.m10, fma(m02, rhs.m20, m03 * rhs.m30))),
        fma(m00, rhs.m01, fma(m01, rhs.m11, fma(m02, rhs.m21, m03 * rhs.m31))),
        fma(m00, rhs.m02, fma(m01, rhs.m12, fma(m02, rhs.m22, m03 * rhs.m32))),
        fma(m00, rhs.m03, fma(m01, rhs.m13, fma(m02, rhs.m23, m03 * rhs.m33))),
        fma(m10, rhs.m00, fma(m11, rhs.m10, fma(m12, rhs.m20, m13 * rhs.m30))),
        fma(m10, rhs.m01, fma(m11, rhs.m11, fma(m12, rhs.m21, m13 * rhs.m31))),
        fma(m10, rhs.m02, fma(m11, rhs.m12, fma(m12, rhs.m22, m13 * rhs.m32))),
        fma(m10, rhs.m03, fma(m11, rhs.m13, fma(m12, rhs.m23, m13 * rhs.m33))),
        fma(m20, rhs.m00, fma(m21, rhs.m10, fma(m22, rhs.m20, m23 * rhs.m30))),
        fma(m20, rhs.m01, fma(m21, rhs.m11, fma(m22, rhs.m21, m23 * rhs.m31))),
        fma(m20, rhs.m02, fma(m21, rhs.m12, fma(m22, rhs.m22, m23 * rhs.m32))),
        fma(m20, rhs.m03, fma(m21, rhs.m13, fma(m22, rhs.m23, m23 * rhs.m33))),
        fma(m30, rhs.m00, fma(m31, rhs.m10, fma(m32, rhs.m20, m33 * rhs.m30))),
        fma(m30, rhs.m01, fma(m31, rhs.m11, fma(m32, rhs.m21, m33 * rhs.m31))),
        fma(m30, rhs.m02, fma(m31, rhs.m12, fma(m32, rhs.m22, m33 * rhs.m32))),
        fma(m30, rhs.m03, fma(m31, rhs.m13, fma(m32, rhs.m23, m33 * rhs.m33)))
    )

    operator fun times(v: Vector3): Vector3 {
        val x = fma(m00, v.x, fma(m10, v.y, fma(m20, v.z, m30)))
        val y = fma(m01, v.x, fma(m11, v.y, fma(m21, v.z, m31)))
        val z = fma(m02, v.x, fma(m12, v.y, fma(m22, v.z, m32)))
        val w = fma(m03, v.x, fma(m13, v.y, fma(m23, v.z, m33)))
        return Vector3(x / w, y / w, z / w)
    }

    companion object {
        fun rotationX(angle: Double): Matrix4 {
            val cos = cos(angle)
            val sin = sin(angle)
            return Matrix4(1.0, 0.0, 0.0, 0.0, 0.0, cos, sin, 0.0, 0.0, -sin, cos, 0.0, 0.0, 0.0, 0.0, 1.0)
        }

        fun rotationY(angle: Double): Matrix4 {
            val cos = cos(angle)
            val sin = sin(angle)
            return Matrix4(cos, 0.0, -sin, 0.0, 0.0, 1.0, 0.0, 0.0, sin, 0.0, cos, 0.0, 0.0, 0.0, 0.0, 1.0)
        }
    }
}
