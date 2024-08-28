subprojects {
    apply(plugin = "java-library")

    group = "io.github.bindglam"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
