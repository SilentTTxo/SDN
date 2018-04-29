package com.sdn.core.SocketInterface;

import java.io.OutputStream;

public interface SocketResponseInterface {
    public OutputStream out = null;
    public boolean isContinue = true;

    public void close();
}
