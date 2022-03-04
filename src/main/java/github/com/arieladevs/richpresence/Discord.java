package github.com.arieladevs.richpresence;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.server.CommandListPlayers;

import java.util.Collection;
import java.util.List;

public class Discord {
    public final DiscordRPC client = DiscordRPC.INSTANCE;
    public final DiscordRichPresence richpresence = new DiscordRichPresence();
    Minecraft mc = Minecraft.getMinecraft();
    public void start() {


        DiscordEventHandlers event = new DiscordEventHandlers();
        event.ready = (user) -> System.out.println("Discord Carregado");
        this.client.Discord_Initialize("949333863254081606", event, true, "0");
        this.richpresence.startTimestamp = System.currentTimeMillis()/1000;
        this.richpresence.details = "Jogando no mcNFT";
        this.richpresence.largeImageKey = "logo_2";
        this.richpresence.largeImageText = "pixelmon.mcNFT.com";
        this.richpresence.smallImageKey = "pokebola";
        this.richpresence.partySize = 0;
        this.richpresence.partyMax = 0;
        this.richpresence.smallImageText = mc.getSession().getUsername();
        this.client.Discord_UpdatePresence(richpresence);
        new Thread("RPC-Callback-Handler") {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    client.Discord_UpdatePresence(richpresence);
                    client.Discord_RunCallbacks();
                    try {
                        if (mc.isSingleplayer()) {
                            richpresence.state = "Jogo local";
                            richpresence.partySize = 0;
                            richpresence.partyMax = 0;
                            client.Discord_UpdatePresence(richpresence);
                        }
                        else if (mc.world !=null && mc.getConnection() != null) {
                            richpresence.state = "mcNFC.com";
                            richpresence.partySize = 0;
                            richpresence.partyMax = 0;
                            client.Discord_UpdatePresence(richpresence);
                        } else {
                            richpresence.state = "Menu do jogo";
                            richpresence.partySize = 0;
                            richpresence.partyMax = 0;
                            client.Discord_UpdatePresence(richpresence);
                        }
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                        client.Discord_Shutdown();
                    }
                }
            }
        }.start();
    }
}
