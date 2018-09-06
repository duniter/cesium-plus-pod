package org.duniter.elasticsearch.util.os.win.libs;

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

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import org.duniter.elasticsearch.util.os.win.handle.HANDLER_ROUTINE;

public interface Kernel32Ex extends Library {

    Kernel32Ex INSTANCE = (Kernel32Ex) Native.loadLibrary("kernel32", Kernel32Ex.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * BOOL WINAPI SetProcessShutdownParameters( _In_ DWORD dwLevel, _In_ DWORD
     * dwFlags );
     * <p/>
     * http://msdn.microsoft.com/en-us/library/windows/desktop/ms686227(v=vs.85).aspx
     */
    boolean SetProcessShutdownParameters(long dwLevel, long dwFlags);

    /**
     * BOOL WINAPI SetConsoleCtrlHandler( _In_opt_ PHANDLER_ROUTINE
     * HandlerRoutine, _In_ BOOL Add );
     * <p/>
     * http://msdn.microsoft.com/en-us/library/windows/desktop/ms686016(v=vs.85).aspx
     */
    boolean SetConsoleCtrlHandler(HANDLER_ROUTINE HandlerRoutine, boolean Add);

}
