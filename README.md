# 🎮 Fabric DiscordRPC – GameSDK Integration

### *A simple and modern Discord Rich Presence setup for Fabric mods*

> **Tested on Fabric 1.21**
> Older versions may require adjustments.

# 🔧 Gradle Setup

### **`build.gradle` – Dependencies**

```gradle
dependencies {
    modImplementation 'com.github.JnCrMx:discord-game-sdk4j:master-SNAPSHOT'
    include 'com.github.JnCrMx:discord-game-sdk4j:master-SNAPSHOT'
}
```

### **`build.gradle` – Repositories**

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io/' }
}
```

---

# 🔧 Maven Setup

### **Dependency**

```xml
<dependency>
    <groupId>com.github.JnCrMx</groupId>
    <artifactId>discord-game-sdk4j</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

### **Repository**

```xml
<repository>
    <id>sonatype-snapshots</id>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
