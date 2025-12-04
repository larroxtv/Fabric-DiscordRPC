package dev.larrox.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class RPC {

    private static final Activity ACTIVITY = new Activity();
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    private static boolean isInMainMenu() {
        return minecraft.world == null;
    }

    private static boolean isOnMultiplayerServer() {
        return minecraft.world != null && !minecraft.isInSingleplayer();
    }

    public static void start() {
        new Thread(() -> {
            final CreateParams params = new CreateParams();
            params.setClientID(OH_NO_A_CLIENT_IDL); // You need to add the L there.
            params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);

            ACTIVITY.timestamps().setStart(Instant.now());

            try (final Core core = new Core(params)) {
                while (true) {

                    ACTIVITY.assets().setLargeText("A Cool Hover Text for my Large Image");
                    ACTIVITY.assets().setLargeImage("large");
                    updatePlayerHead();

                    String playerName = minecraft.getGameProfile() != null ? minecraft.getGameProfile().name() : "Minecraft-Player";
                    ACTIVITY.assets().setSmallText(playerName);

                    if (isInMainMenu()) {
                        ACTIVITY.setDetails("In Main Menu");
                    }
                    else if (isOnMultiplayerServer()) {
                        ACTIVITY.setDetails("In Multiplayer");
                    }
                    else {
                        ACTIVITY.setDetails("In Singleplayer");
                    }
                    ACTIVITY.setState("with A cool Client");

                    // Update zu Discord senden
                    core.activityManager().updateActivity(ACTIVITY);

                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException ignored) { }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void updatePlayerHead() {
        if (minecraft.getGameProfile() == null) return;
        String uuid = minecraft.getGameProfile().id().toString();
        ACTIVITY.assets().setSmallImage(getPlayerHeadURL(uuid, "head", 3));
    }

    @Contract(pure = true)
    private static @NotNull String getPlayerHeadURL(String uuid, String type, int size) {
        return "https://api.mineatar.io/" + type + "/" + uuid + "?scale=" + size;
    }
}

