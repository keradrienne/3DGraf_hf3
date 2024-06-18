import vision.gears.webglmath.Vec3
import vision.gears.webglmath.times

class HeliCamera(
    var target: Car,
    var springConstant: Float,
    var damping: Float,
    var angularSpringConstant: Float,
    var angularDamping: Float,
    vararg programs: Program
) : PerspectiveCamera(*programs) {
    var velocity = Vec3()
    var angularVelocity = Vec3()
    var desiredDistance = 70.0f

    override fun move(dt: Float, keysPressed: Set<String>) {
        val displacement = target.position - position
        val force = springConstant * displacement - damping * velocity

        if (target.velocity.length() > 90) {
            force *= target.velocity.length() * 3
        }

        velocity = velocity.plus(force * dt)

        val angularDisplacement = target.ahead - ahead
        val torque = angularSpringConstant * angularDisplacement - angularDamping * angularVelocity
        angularVelocity = angularVelocity.plus(torque * dt)
        ahead = ahead.plus(angularVelocity * dt)

        val desiredPosition = target.position - ahead * desiredDistance
        position += (desiredPosition - position) * dt
        super.move(dt, keysPressed)
    }
}