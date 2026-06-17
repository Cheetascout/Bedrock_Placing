/*
 * Licensed under the Apache License, Version 2.0
 * See https://www.apache.org/licenses/LICENSE-2.0
 */
package org.bedrock.placing.bedrock_placing;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.RegistryByteBuf;

public record Network() implements CustomPayload {
    public static final Id<Network> ID =
            new Id<>(Identifier.of("bedrock_placing", "disable"));

    public static final PacketCodec<RegistryByteBuf, Network> CODEC =
            PacketCodec.unit(new Network());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}