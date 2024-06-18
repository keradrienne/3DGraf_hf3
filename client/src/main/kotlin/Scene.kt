import org.w3c.dom.HTMLCanvasElement
import vision.gears.webglmath.*
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import kotlin.math.*

class Scene(
    val gl: WebGL2RenderingContext
) : UniformProvider("scene") {

    val vsTextured = Shader(gl, GL.VERTEX_SHADER, "textured-vs.glsl")
    val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
    val texturedProgram = Program(gl, vsTextured, fsTextured)

    val vsBackground = Shader(gl, GL.VERTEX_SHADER, "textured-background-vs.glsl")
    val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "textured-background-fs.glsl")
    val backgroundProgram = Program(gl, vsBackground, fsBackground)

    val vsInfinitePlane = Shader(gl, GL.VERTEX_SHADER, "infinite-plane-vs.glsl")
    val fsInfinitePlane = Shader(gl, GL.FRAGMENT_SHADER, "infinite-plane-fs.glsl")
    val infinitePlaneProgram = Program(gl, vsInfinitePlane, fsInfinitePlane)

    val vsShadow = Shader(gl, GL.VERTEX_SHADER, "shadow-vs.glsl")
    val fsShadow = Shader(gl, GL.FRAGMENT_SHADER, "shadow-fs.glsl")
    val shadowProgram = Program(gl, vsShadow, fsShadow)
    val lightDir = Vec3(1.0f, 1.0f, 1.0f).normalize()
    val shadowMatrix = Mat4().set(
        1.0f, 0.0f, 0.0f, 0.0f,
        -lightDir.x / lightDir.y, 0.5f, -lightDir.z / lightDir.y, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    )

    val InfinitePlaneGeometry = InfinitePlaneGeometry(gl)

    val texturedQuadGeometry = TexturedQuadGeometry(gl)

    // LABTODO: load geometries from the JSON file, create Meshes
    val jsonLoader = JsonLoader()
    val chevyMeshes = jsonLoader.loadMeshes(gl,
        "media/json/chevy/chassis.json",
        Material(texturedProgram).apply {
            this["colorTexture"]?.set(
                Texture2D(gl, "media/json/chevy/chevy.png")
            )
        }
    )

    val chevyWheelMeshes = jsonLoader.loadMeshes(gl,
        "media/json/chevy/wheel.json",
        Material(texturedProgram).apply {
            this["colorTexture"]?.set(
                Texture2D(gl, "media/json/chevy/chevy.png")
            )
        }
    )

    val backgroundMaterial = Material(backgroundProgram).apply {
        this["envTexture"]?.set(
            TextureCube(
                gl,
                "media/posx512.jpg",
                "media/negx512.jpg",
                "media/posy512.jpg",
                "media/negy512.jpg",
                "media/posz512.jpg",
                "media/negz512.jpg"
            )
        )
    }

    val groundMaterial = Material(infinitePlaneProgram).apply {
        this["colorTexture"]?.set(
            Texture2D(gl, "media/ground.jpg")
        )
    }

    val shadowMaterial = Material(shadowProgram)

    val chevy = Car(*chevyMeshes).apply {
        position.set(0f, 6.6f, 0f)
        needsShadow = true
        move = object : GameObject.Motion() {
            override operator fun invoke(
                dt: Float,
                t: Float,
                keysPressed: Set<String>,
                gameObjects: List<GameObject>
            ): Boolean {
                if (keysPressed.contains("ArrowUp")) {
                    acceleration = ahead * invMass * 100.0f
                    velocity = velocity.plus(acceleration * dt)

                }
                else if (keysPressed.contains("ArrowDown")) {
                    acceleration = ahead * invMass * 100.0f
                    velocity = velocity.minus(acceleration * dt)
                }
                if (keysPressed.contains("ArrowLeft") && keysPressed.contains("ArrowUp")) {
                    yaw += 0.5f * dt
                }
                else if (keysPressed.contains("ArrowLeft") && keysPressed.contains("ArrowDown")){
                    yaw -= 0.5f * dt
                }
                if (keysPressed.contains("ArrowRight") && keysPressed.contains("ArrowUp")){
                    yaw -= 0.5f * dt
                }
                else if (keysPressed.contains("ArrowRight") && keysPressed.contains("ArrowDown"))
                {
                    yaw += 0.5f * dt
                }
                else{
                    velocity = velocity * exp(-dt * invMass)
                }

                position -= velocity * dt

                return true
            }
        }
    }

    val leftBackWheel = Wheel(*chevyWheelMeshes).apply {//bal hátsó
        parent = chevy
        position.set(7.5f, -4f, -11f)
        needsShadow = true
    }

    val rightBackWheel = Wheel(*chevyWheelMeshes).apply {//jobb hátsó
        parent = chevy
        scale.set(-1f, 1f, 1f)
        position.set(-7.5f, -4f, -11f)
        needsShadow = true
    }

    val leftRearWheel = Wheel(*chevyWheelMeshes).apply {//bal első
        parent = chevy
        position.set(7.5f, -4f, 14f)
        needsShadow = true

        val car = parent as Car

        move = object : GameObject.Motion() {
            override operator fun invoke(
                dt: Float,
                t: Float,
                keysPressed: Set<String>,
                gameObjects: List<GameObject>
            ): Boolean {
                if (keysPressed.contains("ArrowLeft")) {
                    if (yaw < PI.toFloat() / 4.0f) {
                        yaw += 0.8f * dt
                    }
                }
                if (keysPressed.contains("ArrowRight")) {
                    if (yaw > -PI.toFloat() / 4.0f) {
                        yaw -= 0.8f * dt
                    }
                }

                return true
            }
        }
    }

    val rightRearWheel = Wheel(*chevyWheelMeshes).apply {//jobb első
        parent = chevy
        scale.set(-1f, 1f, 1f)
        position.set(-7.5f, -4f, 14f)
        needsShadow = true
        move = object : GameObject.Motion() {
            override operator fun invoke(
                dt: Float,
                t: Float,
                keysPressed: Set<String>,
                gameObjects: List<GameObject>
            ): Boolean {
                if (keysPressed.contains("ArrowLeft")) {
                    if (yaw > -PI.toFloat() / 4.0f) {
                        yaw -= 0.8f * dt
                    }
                }
                if (keysPressed.contains("ArrowRight")) {
                    if (yaw < PI.toFloat() / 4.0f) {
                        yaw += 0.8f * dt
                    }
                }

                return true
            }
        }
    }

    val backgroundMesh = Mesh(backgroundMaterial, texturedQuadGeometry)

    val groundMesh = Mesh(groundMaterial, InfinitePlaneGeometry)

    val gameObjects = ArrayList<GameObject>()

    init {
        gameObjects += GameObject(backgroundMesh)
        gameObjects += GameObject(groundMesh)
        gameObjects += chevy
        gameObjects += leftBackWheel
        gameObjects += leftRearWheel
        gameObjects += rightBackWheel
        gameObjects += rightRearWheel
    }

//    val camera = PerspectiveCamera(*Program.all).apply {
//        position.set(0.0f, 30.0f, -50f)
//        yaw = PI.toFloat()
//        pitch = PI.toFloat() / -10.0f
//        update()
//    }

    val camera = HeliCamera(chevy, 100.0f, 10.0f, 100.0f, 10.0f, *Program.all).apply {
        position.set(0.0f, 30.0f, -50f)
        yaw = PI.toFloat()
        pitch = PI.toFloat() / -10.0f
        update()
    }

    fun resize(canvas: HTMLCanvasElement) {
        gl.viewport(
            0,
            0,
            canvas.width,
            canvas.height
        )//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
        camera.setAspectRatio(canvas.width.toFloat() / canvas.height)
    }

    val timeAtFirstFrame = Date().getTime()
    var timeAtLastFrame = timeAtFirstFrame

    init {
        addComponentsAndGatherUniforms(*Program.all)
        gl.enable(GL.DEPTH_TEST)
    }

    @Suppress("UNUSED_PARAMETER")
    fun update(keysPressed: Set<String>) {
        val timeAtThisFrame = Date().getTime()
        val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
        val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
        timeAtLastFrame = timeAtThisFrame

        //LABTODO: move camera
        camera.move(dt, keysPressed)
        camera.update()

        gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
        gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

        gl.enable(GL.BLEND)
        gl.blendFunc(
            GL.SRC_ALPHA,
            GL.ONE_MINUS_SRC_ALPHA
        )

        gl.uniformMatrix4fv(
            gl.getUniformLocation(shadowProgram.glProgram, "shadow.shadowMatrix"),
            false, shadowMatrix.storage
        )

        gameObjects.forEach { it.move(dt, t, keysPressed, gameObjects) }

        gameObjects.forEach { it.update() }
        gameObjects.forEach { it.draw(this, camera) }

        gameObjects.forEach {
            if (it.needsShadow) { // ground, background need no shadow
                it.using(shadowMaterial).draw(this, this.camera);
            }
        }
    }
}