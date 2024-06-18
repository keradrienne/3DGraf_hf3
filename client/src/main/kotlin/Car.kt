import vision.gears.webglmath.Mat4
import vision.gears.webglmath.Vec3

class Car(vararg meshes: Mesh) : GameObject(*meshes) {
    var ahead = Vec3(0.0f, 0.0f, -1.0f)
    var yaw = 0.0f
    var rotationMatrix = Mat4()

    var velocity = Vec3(0.0f, 0.0f, 0.0f)
    var acceleration = Vec3(0.0f, 0.0f, 0.0f)
    var invMass = 1.0f // Inverz tömeg, feltételezve, hogy a tömeg 1

    override fun update() {
        rotationMatrix.set().rotate(roll).rotate(yaw, 0.0f, 1.0f, 0.0f)
        modelMatrix.set(rotationMatrix).scale(scale).translate(position)
        ahead = (Vec3(0.0f, 0.0f, -1.0f).xyz0 * rotationMatrix).xyz
    }
}