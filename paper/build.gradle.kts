plugins {
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("com.gradleup.shadow") version "8.3.0"
}

repositories {
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    api(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.shadowJar {
    archiveFileName = "ClassyCutscene-${version}.jar"

    dependencies {
        dependency(":api")
    }
}

tasks.test {
    useJUnitPlatform()
}