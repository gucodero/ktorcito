# Ktorcito 🚀

<br>

Este proyecto demo fue creado para aprender a como construir un procesador de anotaciones utilizando KSP (Kotlin Symbol Processing) y Ktor. El objetivo principal es mostrar cómo se puede automatizar la generación de código utilizando estas tecnologías replicando la libreria de retrofit ahora con ktor.

<br>
<p align="center">
    <img src="./doc/ktorcito_logo.png" alt="Ktorcito">
</p>
<br>

## 🛠️ Tecnologías utilizadas

En este proyecto se utilizan las siguientes tecnologías:

- KSP (Kotlin Symbol Processing): Una biblioteca de procesamiento de símbolos en Kotlin que permite generar código en tiempo de compilación.

- Kotlin Poet: Una biblioteca de generación de código en Kotlin que simplifica la creación de código fuente en tiempo de compilación.

- Ktor: Un framework de aplicaciones web en Kotlin que facilita la construcción de servidores y clientes HTTP.

## 🌿 Ramas del proyecto

El proyecto tiene las siguientes ramas:

- `initial_project`: Esta rama contiene el proyecto base con la implementación manual de la clase `PostsApi`. Puedes usar esta rama como punto de partida para practicar el procesamiento de anotaciones con KSP y Kotlin Poet. Puedes cambiar a esta rama con el siguiente comando de Git:

    ```bash
    git checkout initial_project
    ```

- `refactor_lib`: En esta rama se realizó un refactor donde se creó el módulo Kotlin "ktorcito". Aquí se crearon y refactorizaron componentes que serán utilizados en la implementación de nuestras clases APIs. En esta etapa comenzaremos a encontrar patrones repetitivos en nuestro código, y con esta información podremos automatizar la generación de ese código. Puedes cambiar a esta rama con el siguiente comando de Git:

    ```bash
    git checkout refactor_lib
    ```

- `setup_compiler`: En esta rama se configura el módulo Kotlin "ktorcito-compiler" con KSP y Kotlin Poet. Aquí se creará nuestra primera anotación `@HelloWorld` que generará automáticamente la función de extensión `helloWorld` en las clases anotadas. Puedes cambiar a esta rama con el siguiente comando de Git:

    ```bash
    git checkout setup_compiler
    ```

- `main`: Esta es la rama final del proyecto. Aquí encontrarás el proyecto completo para estudiarlo. Puedes cambiar a esta rama con el siguiente comando de Git:

    ```bash
    git checkout main
    ```



## 💻 Funcionamiento de la aplicación demo

En esta aplicación demo, se implementa un CRUD completo utilizando la API pública [JSON Placeholder](https://jsonplaceholder.typicode.com/). Como cliente HTTP se utiliza Ktor.

La aplicación permite realizar las siguientes operaciones:

- Obtener todos los posts: Se realiza una solicitud GET a la API para obtener todos los posts disponibles.

- Obtener un post específico: Se realiza una solicitud GET a la API para obtener un post específico utilizando su ID.

- Crear un nuevo post: Se realiza una solicitud POST a la API para crear un nuevo post.

- Actualizar un post existente: Se realiza una solicitud PUT a la API para actualizar un post existente utilizando su ID.

- Eliminar un post: Se realiza una solicitud DELETE a la API para eliminar un post utilizando su ID.

A continuación, se muestra un gif que ilustra el funcionamiento de la aplicación:

<br>
<p align="center">
    <img src="./doc/ktorcito.gif" alt="Funcionamiento de la aplicación">
</p>

## 🤓 Hello world! (Autogenerado 🤖)

Aquí se muestra el paso a paso para crear un módulo de procesamiento de anotaciones utilizando KSP (Kotlin Symbol Processing).

#### 1. Creación de proyecto Android:
- Crea un nuevo proyecto Android base.
- Agrega el siguiente código al archivo `build.gradle.kts` a nivel de proyecto:

    ```kotlin
    plugins {
         id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    }
    ```

- Asegúrate de que la versión de KSP sea compatible con la versión de Kotlin que estás utilizando. Puedes ver todos los releases [aquí](https://github.com/google/ksp/releases).

#### 2. Módulo Kotlin para nuestra librería:
    
- Crea un nuevo módulo Kotlin para la librería.
- Agrega el siguiente código al archivo `build.gradle.kts` de este módulo:

    ```kotlin
    plugins {
         id("java-library")
         id("org.jetbrains.kotlin.jvm")
    }

    java {
         sourceCompatibility = JavaVersion.VERSION_1_8
         targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
         // Tus dependencias
    }

    //En caso de que tu version de JDK genere problemas puedes usar este bloque
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    ```

- En este módulo, crea tu primera anotación `@HelloWorld`:

    ```kotlin
    @Target(AnnotationTarget.CLASS)
    annotation class HelloWorld
    ```

#### 3. Módulo Kotlin para nuestro procesador de anotaciones:
    
- Crea un nuevo módulo Kotlin para el procesador de anotaciones.
- Agrega el siguiente código al archivo `build.gradle.kts` de este módulo:

    ```kotlin
    plugins {
         id("java-library")
         id("org.jetbrains.kotlin.jvm")
    }

    java {
         sourceCompatibility = JavaVersion.VERSION_1_8
         targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
         implementation(project(":my-lib"))
         implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
         implementation("com.squareup:kotlinpoet:1.14.2")
         implementation("com.squareup:kotlinpoet-ksp:1.14.2")
    }

    //En caso de que tu version de JDK genere problemas puedes usar este bloque
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    ```

#### 4. Creación del procesador de anotaciones:

- En el último módulo creado, crea la clase `MySymbolProcessor` de la siguiente forma:

    ```kotlin
    class MySymbolProcessor(
         private val codeGenerator: CodeGenerator,
         private val logger: KSPLogger
    ): SymbolProcessor {

         override fun process(resolver: Resolver): List<KSAnnotated> {
              resolver.getSymbolsWithAnnotation(
                    annotationName = HelloWorld::class.java.name
              ).filterIsInstance<KSClassDeclaration>().forEach(::processClass)
              return emptyList()
         }

         private fun processClass(clazz: KSClassDeclaration) {
              logger.warn("Processing @HelloWorld in class: ${clazz.simpleName.asString()}")
              val funSpec: FunSpec = FunSpec.builder("helloWorld")
                    .addModifiers(KModifier.PUBLIC)
                    .receiver(clazz.toClassName())
                    .returns(typeNameOf<String>())
                    .addStatement("return \"Hello, World!\"")
                    .build()
              val fileSpec: FileSpec = FileSpec
                    .builder(
                         packageName = clazz.packageName.asString(),
                         fileName = "HelloWorld_Generated"
                    )
                    .addFunction(funSpec)
                    .build()
              fileSpec.writeTo(codeGenerator, Dependencies(false))
         }
    }
    ```

- Crea la clase `MySymbolProcessorProvider`, que será el punto de entrada del procesador de anotaciones:

    ```kotlin
    class MySymbolProcessorProvider: SymbolProcessorProvider {

         override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
              return MySymbolProcessor(
                    codeGenerator = environment.codeGenerator,
                    logger = environment.logger
              )
         }
    }
    ```

- Registra los metadatos para que el compilador reconozca nuestro provider. Crea el siguiente directorio en la raíz del último módulo creado:

    ```
    src/main/resources/META-INF/services/com.google.devtools.ksp.processing.SymbolProcessorProvider
    ```

- En el archivo `com.google.devtools.ksp.processing.SymbolProcessorProvider`, agrega la ruta de nuestro provider, por ejemplo:

    ```
    com.gucodero.my_lib_compiler.MySymbolProcessorProvider
    ```

#### 5. Configuración en el módulo de la aplicación:
- Agrega las siguientes configuraciones y dependencias al archivo `build.gradle.kts` del módulo de la aplicación:

    ```kotlin
    plugins {
         // ...
         id("com.google.devtools.ksp")
    }

    // ...

    dependencies {
         implementation(project(":my-lib"))
         ksp(project(":my-lib-compiler"))
    }
    ```

#### 6. Anota una clase con `@HelloWorld` en el módulo de la aplicación para ver el procesador de anotaciones en acción:

    ```kotlin
    @HelloWorld
    class MainActivity : ComponentActivity() {

         override fun onCreate(savedInstanceState: Bundle?) {
              super.onCreate(savedInstanceState)
              // ...

              // Llamada a la función autogenerada
              helloWorld()
         }
    }
    ```

¡Ahora tienes un módulo de procesamiento de anotaciones funcionando en tu proyecto!

## 🔍 Conclusión

Los procesadores de anotaciones son una herramienta poderosa en el desarrollo de software. Permiten automatizar tareas repetitivas y generar código de manera dinámica durante el proceso de compilación. Esto brinda varios beneficios:

1. **Reducción de código repetitivo**: Los procesadores de anotaciones permiten generar automáticamente código que de otra manera tendríamos que escribir manualmente. Esto ahorra tiempo y reduce la posibilidad de cometer errores.

2. **Mejora de la legibilidad y mantenibilidad del código**: Al automatizar la generación de código, podemos separar la lógica de generación del código en sí. Esto hace que el código resultante sea más limpio, legible y fácil de mantener.

3. **Facilita la adopción de buenas prácticas**: Los procesadores de anotaciones pueden ayudarnos a aplicar automáticamente buenas prácticas de programación, como la generación de código boilerplate, la validación de parámetros o la implementación de patrones de diseño.

4. **Extensibilidad y personalización**: Los procesadores de anotaciones son altamente personalizables y extensibles. Podemos crear nuestras propias anotaciones y procesadores para adaptarlos a nuestras necesidades específicas.

En resumen, los procesadores de anotaciones son una herramienta valiosa que nos permite automatizar tareas repetitivas, mejorar la calidad del código y aumentar la productividad en el desarrollo de software.

Si tienes alguna consulta o propuesta relacionada con este tema, no dudes en contactarme en pablo.cruz@gucodero.com. Estaré encantado de ayudarte.

## License

The code in this repository is licensed under the [MIT License](https://opensource.org/licenses/MIT).
