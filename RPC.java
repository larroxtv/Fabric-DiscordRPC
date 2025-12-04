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
    
    private static String lastDetails = "";
    private static String lastState = "";
    private static String lastPlayerName = "";

    private static boolean isInMainMenu() {
        return minecraft.world == null;
    }

    private static boolean isOnMultiplayerServer() {
        return minecraft.world != null && !minecraft.isInSingleplayer();
    }

    public static void start() {
        new Thread(() -> {
            final CreateParams params = new CreateParams();
            params.setClientID(OH_NO_A_CLIENT_IDL);
            params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);

            ACTIVITY.timestamps().setStart(Instant.now());
            ACTIVITY.assets().setLargeText("A Cool Hover Text for my Large Image");
            ACTIVITY.assets().setLargeImage("large");

            try (final Core core = new Core(params)) {
                while (true) {
                    String details;
                    if (isInMainMenu()) {
                        details = "In Main Menu";
                    } else if (isOnMultiplayerServer()) {
                        details = "In Multiplayer";
                    } else {
                        details = "In Singleplayer";
                    }
                    
                    String state = "with A cool Client";
                    String playerName = minecraft.getGameProfile() != null ? 
                        minecraft.getGameProfile().name() : "Minecraft-Player";

                    boolean needsUpdate = !details.equals(lastDetails) || 
                                        !state.equals(lastState) ||
                                        !playerName.equals(lastPlayerName);

                    if (needsUpdate) {
                        ACTIVITY.setDetails(details);
                        ACTIVITY.setState(state);
                        
                        if (!playerName.equals(lastPlayerName)) {
                            updatePlayerHead();
                            ACTIVITY.assets().setSmallText(playerName);
                        }

                        core.activityManager().updateActivity(ACTIVITY);
                        
                        lastDetails = details;
                        lastState = state;
                        lastPlayerName = playerName;
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) { }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Discord-RPC").start();
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
