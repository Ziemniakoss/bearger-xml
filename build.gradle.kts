import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.21"
	application
}

group = "pl.ziemniakoss"
version = "0.66"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(kotlin("test"))
	implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.3")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.test {
	useJUnitPlatform()
}
tasks.jar {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	manifest {
		attributes["Main-Class"] = "MainKt"
	}
	configurations["compileClasspath"].forEach { file: File ->
		from(zipTree(file.absoluteFile))
	}
}
tasks.withType<KotlinCompile>() {
	kotlinOptions.jvmTarget = "1.8"
}

application {
	mainClassName = "MainKt"
}