plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.jonesmbindyo.spinwheel_sdk"
    compileSdk = 36

    defaultConfig { minSdk = 26 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }

    buildFeatures { compose = true }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

// Collect JARs produced by each pure-JVM sub-module so we can merge them into the AAR.
val subModuleJars: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        attribute(
            org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE,
            objects.named(org.gradle.api.attributes.Usage::class, org.gradle.api.attributes.Usage.JAVA_RUNTIME)
        )
    }
}

dependencies {
    // Keep api() so transitive type resolution works for consumers that also have the sub-modules.
    api(project(":core"))
    api(project(":data"))
    api(project(":domain"))
    api(project(":di"))

    // Collect the same sub-module JARs for physical merging.
    subModuleJars(project(":core"))
    subModuleJars(project(":data"))
    subModuleJars(project(":domain"))
    subModuleJars(project(":di"))

    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.koin.android)
    api(libs.koin.androidx.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

// After the release AAR is assembled, unpack sub-module JARs into the AAR's classes.jar.
val mergeSubModulesIntoAar by tasks.registering {
    dependsOn("bundleReleaseAar")

    val aarFile = layout.buildDirectory.file("outputs/aar/spinwheel-sdk-release.aar")
    val subJars = subModuleJars

    inputs.files(subJars)
    inputs.file(aarFile)
    outputs.file(aarFile)

    doLast {
        val aar = aarFile.get().asFile
        require(aar.exists()) { "AAR not found: ${aar.absolutePath}" }

        val tmpDir = layout.buildDirectory.dir("tmp/fat-aar-merge").get().asFile
        tmpDir.deleteRecursively()
        tmpDir.mkdirs()

        // Unpack the existing AAR.
        val aarUnpack = File(tmpDir, "aar-unpacked")
        aarUnpack.mkdirs()
        ant.withGroovyBuilder {
            "unzip"("src" to aar.absolutePath, "dest" to aarUnpack.absolutePath)
        }

        // Unpack the existing classes.jar from the AAR.
        val classesJar = File(aarUnpack, "classes.jar")
        val classesDir = File(tmpDir, "merged-classes")
        classesDir.mkdirs()
        ant.withGroovyBuilder {
            "unzip"("src" to classesJar.absolutePath, "dest" to classesDir.absolutePath)
        }

        // Unpack each sub-module JAR into the same classes directory.
        subJars.resolvedConfiguration.resolvedArtifacts
            .filter { it.file.extension == "jar" }
            .forEach { artifact ->
                logger.lifecycle("Merging sub-module JAR: ${artifact.file.name}")
                ant.withGroovyBuilder {
                    "unzip"(
                        "src" to artifact.file.absolutePath,
                        "dest" to classesDir.absolutePath,
                        "overwrite" to "true"
                    )
                }
            }

        // Repack the merged classes back into classes.jar inside the AAR unpack dir.
        classesJar.delete()
        ant.withGroovyBuilder {
            "zip"(
                "destfile" to classesJar.absolutePath,
                "basedir" to classesDir.absolutePath
            )
        }

        // Repack the AAR.
        val fatAar = File(tmpDir, "spinwheel-sdk-fat.aar")
        ant.withGroovyBuilder {
            "zip"(
                "destfile" to fatAar.absolutePath,
                "basedir" to aarUnpack.absolutePath
            )
        }

        // Replace the original AAR with the fat AAR.
        aar.delete()
        fatAar.copyTo(aar)
        logger.lifecycle("Fat AAR written to: ${aar.absolutePath}")
    }
}

afterEvaluate {
    // Wire assembleRelease to produce the fat AAR automatically.
    tasks.named("assembleRelease") {
        finalizedBy(mergeSubModulesIntoAar)
    }

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.spinwheel"
                artifactId = "spinwheel-sdk"
                version = "1.0.2"
            }
        }
    }
}

// Lazily wire all publish tasks to depend on the fat-aar merge.
// Uses matching() so it fires whenever matching tasks are created, regardless of order.
tasks.matching { it.name.startsWith("publish") || it.name.startsWith("generateMetadata") || it.name.startsWith("generatePom") }.configureEach {
    dependsOn(mergeSubModulesIntoAar)
}