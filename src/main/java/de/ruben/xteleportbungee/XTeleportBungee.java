package de.ruben.xteleportbungee;

import de.ruben.xteleportbungee.listener.PluginMessageListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.GroupedThreadFactory;

public final class XTeleportBungee extends Plugin {

    @Override
    public void onEnable() {
        getProxy().registerChannel("xteleport:singleteleportrequest");
        getProxy().registerChannel("xteleport:singleteleportanswer");
        getProxy().registerChannel("xteleport:targettedteleportrequest");
        getProxy().registerChannel("xteleport:targettedteleportanswer");
        getProxy().registerChannel("xteleport:serverteleportrequest");
        getProxy().registerChannel("xteleport:serverteleportanswer");

        getProxy().getPluginManager().registerListener(this, new PluginMessageListener());
    }

    @Override
    public void onDisable() {
        getProxy().unregisterChannel("xteleport:singleteleportrequest");
        getProxy().unregisterChannel("xteleport:singleteleportanswer");
        getProxy().unregisterChannel("xteleport:targettedteleportrequest");
        getProxy().unregisterChannel("xteleport:targettedteleportanswer");
        getProxy().unregisterChannel("xteleport:serverteleportrequest");
        getProxy().unregisterChannel("xteleport:serverteleportanswer");
    }
}
