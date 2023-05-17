package reference.sr25519.type;

import com.strategyobject.substrateclient.common.types.Bytes;
import lombok.Data;
import lombok.NonNull;

@Data
public class nByte implements Bytes {
    byte[] data;

    public nByte(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getBytes() {
        return data;
    }
}
