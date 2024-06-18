import vision.gears.webglmath.*

open class GameObject(
    vararg val meshes: Mesh
) : UniformProvider("gameObject") {

    val position = Vec3()
    var roll = 0.0f
    val scale = Vec3(1.0f, 1.0f, 1.0f)
    var needsShadow = false

    val modelMatrix by Mat4()

    var parent: GameObject? = null

    init {
        addComponentsAndGatherUniforms(*meshes)
    }

    open fun update() {
        modelMatrix.set().scale(scale).rotate(roll).translate(position)
        parent?.let { parent ->
            modelMatrix *= parent.modelMatrix
        }
    }

    open class Motion {
        open operator fun invoke(
            dt: Float = 0.016666f,
            t: Float = 0.0f,
            keysPressed: Set<String> = emptySet<String>(),
            gameObjects: List<GameObject> = emptyList<GameObject>()
        ): Boolean {
            return true;
        }
    }

    var move = Motion()

}