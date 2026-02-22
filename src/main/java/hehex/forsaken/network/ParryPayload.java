package hehex.forsaken.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ParryPayload() implements CustomPayload {
    public static final CustomPayload.Id<ParryPayload> ID = new CustomPayload.Id<>(Identifier.of("forsaken-reality", "parry"));
    public static final PacketCodec<RegistryByteBuf, ParryPayload> CODEC = PacketCodec.unit(new ParryPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}