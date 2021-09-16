package de.ruben.xteleportbungee.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event){

        if(event.getTag().equals("xteleport:singleteleportrequest")){
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String targetName = dataInputStream.readUTF();
                String playerUUID = dataInputStream.readUTF();

                ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetName);
                ProxiedPlayer sendPlayer = ProxyServer.getInstance().getPlayer(UUID.fromString(playerUUID));


                if(sendPlayer != null && targetPlayer != null) {
                    sendPlayer.connect(targetPlayer.getServer().getInfo());

                    ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                    dataOutput.writeUTF(sendPlayer.getUniqueId().toString());
                    dataOutput.writeUTF(targetPlayer.getUniqueId().toString());

                    targetPlayer.getServer().getInfo().sendData("xteleport:singleteleportanswer", dataOutput.toByteArray());

                    sendPlayer.sendMessage(new TextComponent("§7Du wurdest zu §b" + targetPlayer.getName() + " §7teleportiert!"));
                }else if(sendPlayer != null && targetPlayer == null){

                    sendPlayer.sendMessage(new TextComponent("§7Der Spieler §b"+targetName+" §7konnte auf dem Netzwerk nicht gefunden werden!"));

                }else if(sendPlayer == null && targetPlayer == null){

                    System.out.println("Failed to teleport "+playerUUID+" to "+targetName+" because BUNGEE returned null for both!");

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if(event.getTag().equals("xteleport:targettedteleportrequest")){
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String target1Name = dataInputStream.readUTF();
                String target2Name = dataInputStream.readUTF();
                String senderUUID = dataInputStream.readUTF();

                ProxiedPlayer target1 = ProxyServer.getInstance().getPlayer(target1Name);
                ProxiedPlayer target2 = ProxyServer.getInstance().getPlayer(target2Name);
                ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(UUID.fromString(senderUUID));

                if(target1 != null && target2 != null){

                    target1.connect(target2.getServer().getInfo());

                    ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                    dataOutput.writeUTF(target1.getUniqueId().toString());
                    dataOutput.writeUTF(target2.getUniqueId().toString());

                    target2.getServer().getInfo().sendData("xteleport:targettedteleportanswer", dataOutput.toByteArray());

                    if(sender != null) {
                        sender.sendMessage(new TextComponent("§7Du hast §b" + target1Name + " §7zu §b" + target2Name + " §7teleportier!"));
                    }
                }else if(target1 == null || target2 == null){
                    if(sender != null) {
                        sender.sendMessage(new TextComponent("§7Einer der beiden Spieler ist nicht online. Deswegen wurde §bniemand §7teleportiert!"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return;
        }

        if(event.getTag().equals("xteleport:serverteleportrequest")){
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String serverName = dataInputStream.readUTF();

                String world = dataInputStream.readUTF();
                String xString = dataInputStream.readUTF();
                String yString = dataInputStream.readUTF();
                String zString = dataInputStream.readUTF();

                String senderUUID = dataInputStream.readUTF();

                ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(UUID.fromString(senderUUID));

                if(!ProxyServer.getInstance().getServersCopy().containsKey(serverName)){

                    StringBuilder messageBuilder = new StringBuilder();

                    messageBuilder.append("§7Der Server §b"+serverName+" §7wurde nicht gefunden! Folgende Server setehen zur Auswahl: §b");

                    ProxyServer.getInstance()
                            .getServersCopy()
                            .keySet()
                            .stream()
                            .forEach(s -> messageBuilder.append(s+", "));

                    sender.sendMessage(new TextComponent(messageBuilder.substring(0, messageBuilder.length()-1)));

                    return;
                }

                sender.connect(ProxyServer.getInstance().getServerInfo(serverName));

                ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                dataOutput.writeUTF(world);
                dataOutput.writeUTF(xString);
                dataOutput.writeUTF(yString);
                dataOutput.writeUTF(zString);
                dataOutput.writeUTF(senderUUID);

                ProxyServer.getInstance().getServerInfo(serverName).sendData("xteleport:serverteleportanswer", dataOutput.toByteArray());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

    }

}
