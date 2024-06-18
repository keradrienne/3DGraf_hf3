import vision.gears.webglmath.Mat4
import vision.gears.webglmath.Vec3
import kotlin.js.Date

class Wheel (vararg meshes: Mesh) : GameObject(*meshes) {
    var pitch = 0.0f
    var yaw = 0.0f
    var ahead = Vec3(0.0f, 0.0f, -1.0f)

    val rotationMatrix = Mat4()

    override fun update() {
        rotationMatrix.set().rotate(roll).rotate(pitch, 1.0f, 0.0f, 0.0f).rotate(yaw, 0.0f, 1.0f, 0.0f)
        modelMatrix.set(rotationMatrix).scale(scale).translate(position)
        parent?.let { parent ->
            modelMatrix *= parent.modelMatrix
        }

        if((parent as Car).velocity.z < 0.0f) {
            pitch += (parent as Car).velocity.length() * 0.001f
        }
        else {
            pitch -= (parent as Car).velocity.length() * 0.001f
        }


        ahead = (Vec3(0.0f, 0.0f, -1.0f).xyz0 * rotationMatrix).xyz
    }
}