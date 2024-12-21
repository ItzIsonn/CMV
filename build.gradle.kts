plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.10.2"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("com.google.code.gson:gson:2.11.0")
}

group = "me.itzisonn_.meazy"
version = "2.0"
description = "Meazy"
java.sourceCompatibility = JavaVersion.VERSION_21

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}