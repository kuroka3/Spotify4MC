import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    id("fabric-loom") version "1.7.1"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("spotify4mc") {
            sourceSet("main")
            sourceSet("client")
        }
    }
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    maven(url = "https://maven.terraformersmc.com/")
    maven(url = "https://maven.isxander.dev/releases")
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("com.terraformersmc:modmenu:11.0.1")
    modImplementation("dev.isxander:yet-another-config-lib:3.5.0+1.21-fabric")
    implementation("io.javalin:javalin:6.2.0")
    include("io.javalin:javalin:6.2.0")
    include("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
    include("org.jetbrains.kotlin:kotlin-reflect:1.9.24")
    include("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.24")
    include("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
    include("org.jetbrains:annotations:24.1.0")
    include("org.eclipse.jetty:jetty-bom:11.0.21")
    include("org.eclipse.jetty.toolchain:jetty-jakarta-servlet-api:5.0.2")
    include("org.eclipse.jetty:jetty-http:11.0.21")
    include("org.eclipse.jetty:jetty-util:11.0.21")
    include("org.eclipse.jetty:jetty-server:11.0.21")
    include("org.eclipse.jetty:jetty-servlet:11.0.21")
    include("org.eclipse.jetty:jetty-security:11.0.21")
    include("org.eclipse.jetty:jetty-io:11.0.21")
    include("com.fasterxml.jackson.core:jackson-core:2.17.1")
    include("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    include("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    include("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
    include("com.aayushatharva.brotli4j:brotli4j:1.16.0")
    include("ch.qos.logback:logback-classic:1.5.6")
    include("org.slf4j:slf4j-api:2.0.13")
    include("org.slf4j:slf4j-simple:2.0.13")
    include("com.squareup.okhttp3:okhttp:4.12.0")
    include("com.squareup.okhttp3:okhttp-tls:4.12.0")
    include("io.github.hakky54:sslcontext-kickstart:8.3.6")
    include("io.github.hakky54:sslcontext-kickstart-for-jetty:8.3.6")
    include("io.github.hakky54:sslcontext-kickstart-for-pem:8.3.6")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version"),
            "kotlin_loader_version" to project.property("kotlin_loader_version")
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }


}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
