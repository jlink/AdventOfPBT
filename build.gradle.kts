import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
    // maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("net.jqwik:jqwik:1.6.0")
    testImplementation("net.jqwik:jqwik-kotlin:1.6.0")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    // To allow other naming conventions
    include("**/*Properties.class")
    include("**/*Examples.class")
    include("**/*Test.class")
    include("**/*Tests.class")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(
		  "-Xjsr305=strict", // Strict interpretation of nullability annotations in jqwik API
		  "-Xemit-jvm-type-annotations" // Enable nnotations on type variables
		)
        jvmTarget = "11" // 1.8 or above
        javaParameters = true // Get correct parameter names in jqwik reporting
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "7.3"
}
