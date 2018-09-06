package org.duniter.elasticsearch.util.os.win.wrap;

/*
 * #%L
 * Reef DB :: UI
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2015 Ifremer
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.sun.jna.WString;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.ATOM;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinUser;
import org.duniter.elasticsearch.util.os.win.handle.WNDPROC;

public class WNDCLASSEXWrap {

    WString klass;
    ATOM wcatom;
    HINSTANCE hInstance;

    public WNDCLASSEXWrap(HINSTANCE hInstance, WNDPROC WndProc, String klass) {
        this.klass = new WString(klass);
        this.hInstance = hInstance;

        WinUser.WNDCLASSEX wc = new WinUser.WNDCLASSEX();
        wc.cbSize = wc.size();
        wc.style = 0;
        wc.lpfnWndProc = WndProc;
        wc.cbClsExtra = 0;
        wc.cbWndExtra = 0;
        wc.hInstance = hInstance;
        wc.hIcon = null;
        wc.hbrBackground = null;
        wc.lpszMenuName = null;
        wc.lpszClassName = new WString(klass);

        wcatom = User32.INSTANCE.RegisterClassEx(wc);
        if (wcatom == null)
            throw new GetLastErrorException();
    }

    public void close() {
        if (wcatom != null) {
            if (!User32.INSTANCE.UnregisterClass(klass, hInstance))
                throw new GetLastErrorException();
            wcatom = null;
        }
    }

    public WString getClassName() {
        return klass;
    }

    public String getName() {
        return klass.toString();
    }

    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

}
