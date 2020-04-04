package me.hbj233.worldprotect.util

import cn.nukkit.math.Vector3
import kotlin.math.max
import kotlin.math.min

fun Double.isBetween(double1: Double, double2: Double): Boolean = this in min(double1, double2)..max(double1, double2)

fun Vector3.isInRange(vector3: Vector3, range: Int): Boolean = isInRange(this.x, this.z, vector3.x, vector3.z, range)

fun isInRange(x1: Double, z1: Double, x2: Double, z2: Double, range: Int): Boolean =
        x1.isBetween(x2 + range, x2 - range) && z1.isBetween(z2 + range, z2 - range)