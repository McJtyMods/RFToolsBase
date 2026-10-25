# RFToolsBase

Base mod for RFTools and McJty tech mods.

The `26.2_fabric` branch targets Minecraft 26.2 with Fabric, Java 25, Gradle 9.5.1 and Fabric Loom 1.17.17. It ports the 26.2 NeoForge implementation using native Fabric entrypoints, lookups, attachments, menus, networking and transactional transfer APIs. Config files use Forge Config API Port.

Build with `./gradlew build`, or run `./gradlew :RFToolsBase:build` from the sibling `../Multi_fabric` workspace. Development runs use `:workspace:runClient` and `:workspace:runServer`; resource generation uses `:RFToolsBase:runDatagen`.

Every build applies [MultiWorkSpace's shared Fabric 26.2 script](https://github.com/McJtyMods/MultiWorkSpace/blob/26.2_fabric/gradletools.gradle) directly from the remote `26.2_fabric` branch. Mod checkouts do not keep local copies.

Standalone builds resolve these Jenkins Fabric artifacts when source projects are unavailable:

- `com.github.mcjty:mcjtylib-fabric:26.2-10.0.1-fabric.4`
- `mcjty.theoneprobe:theoneprobe:26.2-15.0.1-fabric-1`

Minimum runtime mod versions omit the artifact revision suffix: McJtyLib `26.2-10.0.1-fabric` and RFToolsBase `26.2-8.0.0-fabric`. TOP remains an optional mod integration. Workspace builds use the local McJtyLib and TOP source checkouts when available.

Validation commands cover all three builds, generated resources, JUnit tests, and an integrated Fabric client/server test. From Multi_fabric, run `./gradlew -PfabricPortTests :RFToolsBase:runClientGameTest` to check native energy and inventory rollback, fluid rounding, generation, menus, facade removal and cable/antenna models. The fixture is excluded from release jars.
