Published using JitPack

# How to import
## Gradle

```gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation 'com.github.CAPS123987:AdvancedTextDisplay:Tag'
}
```
## Maven
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
    <groupId>com.github.CAPS123987</groupId>
    <artifactId>AdvancedTextDisplay</artifactId>
    <version>Tag</version>
</dependency>
```

## JitPack
[here](https://jitpack.io/#CAPS123987/AdvancedTextDisplay/)
