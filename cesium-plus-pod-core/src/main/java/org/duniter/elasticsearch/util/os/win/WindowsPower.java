package org.duniter.elasticsearch.util.os.win;

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

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSG;
import org.duniter.elasticsearch.util.DesktopPower;
import org.duniter.elasticsearch.util.os.win.handle.CWPSSTRUCT;
import org.duniter.elasticsearch.util.os.win.handle.HANDLER_ROUTINE;
import org.duniter.elasticsearch.util.os.win.handle.WNDPROC;
import org.duniter.elasticsearch.util.os.win.libs.Kernel32Ex;
import org.duniter.elasticsearch.util.os.win.wrap.GetLastErrorException;
import org.duniter.elasticsearch.util.os.win.wrap.WNDCLASSEXWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class WindowsPower extends DesktopPower {

    private static final Logger LOG = LoggerFactory.getLogger(WindowsPower.class);

    public static final int WM_QUERYENDSESSION = 17;
    public static final int WM_ENDSESSION = 22;
    public static final int WH_CALLWNDPROC = 4;

    public class MessagePump implements Runnable {
        Thread t;

        WNDCLASSEXWrap wc;
        WNDPROC WndProc;
        HWND hWnd;
        HINSTANCE hInstance;

        final Object lock = new Object();

        public MessagePump() {
            t = new Thread(this, WindowsPower.class.getSimpleName());
        }

        public void start() {
            synchronized (lock) {
                t.start();
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        void create() {
            WndProc = new WNDPROC() {
                public LRESULT callback(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam) {
                    switch (msg) {
                        case WM_ENDSESSION:
                            return new LRESULT(0);
                        case WM_QUERYENDSESSION:
                            JOptionPane.showMessageDialog(null, "exit");
                            callListeners("WM_QUERYENDSESSION callback");
                            return new LRESULT(0);
                        case User32.WM_QUIT:
                            User32.INSTANCE.PostMessage(hWnd, User32.WM_QUIT, null, null);
                            break;
                    }

                    return User32.INSTANCE.DefWindowProc(hWnd, msg, wParam, lParam);
                }
            };
            hWnd = createWindow();
        }

        // http://osdir.com/ml/java.jna.user/2008-07/msg00049.html

        HWND createWindow() {
            hInstance = Kernel32.INSTANCE.GetModuleHandle(null);

            wc = new WNDCLASSEXWrap(hInstance, WndProc, WindowsPower.class.getSimpleName());

            HWND hwnd = User32.INSTANCE.CreateWindowEx(0, wc.getClassName(), wc.getName(), User32.WS_OVERLAPPED, 0, 0,
                    0, 0, null, null, hInstance, null);

            if (hwnd == null)
                throw new GetLastErrorException();

            return hwnd;
        }

        @Override
        public void run() {
            create();

            synchronized (lock) {
                lock.notifyAll();
            }

            MSG msg = new MSG();

            while (User32.INSTANCE.GetMessage(msg, null, 0, 0) > 0) {
                User32.INSTANCE.DispatchMessage(msg);
            }

            destory();
        }

        void destory() {
            if (hWnd != null) {
                if (!User32.INSTANCE.DestroyWindow(hWnd))
                    throw new GetLastErrorException();
                hWnd = null;
            }

            if (wc != null) {
                wc.close();
                wc = null;
            }
        }

        void close() {
            User32.INSTANCE.PostQuitMessage(0);

            try {
                if (!Thread.currentThread().equals(t))
                    t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    MessagePump mp = new MessagePump();

    HANDLER_ROUTINE hr = new HANDLER_ROUTINE() {
        @Override
        public long callback(long dwCtrlType) {
            if ((dwCtrlType & HANDLER_ROUTINE.CTRL_CLOSE_EVENT) == HANDLER_ROUTINE.CTRL_CLOSE_EVENT) {
                callListeners("HANDLER_ROUTINE.CTRL_CLOSE_EVENT");
            }
            if ((dwCtrlType & HANDLER_ROUTINE.CTRL_LOGOFF_EVENT) == HANDLER_ROUTINE.CTRL_LOGOFF_EVENT) {
                callListeners("HANDLER_ROUTINE.CTRL_LOGOFF_EVENT");
            }
            if ((dwCtrlType & HANDLER_ROUTINE.CTRL_SHUTDOWN_EVENT) == HANDLER_ROUTINE.CTRL_SHUTDOWN_EVENT) {
                callListeners("HANDLER_ROUTINE.CTRL_SHUTDOWN_EVENT");
            }
            return 1;
        }
    };

    HOOKPROC hp = new HOOKPROC() {
        @SuppressWarnings("unused")
        public LRESULT callback(int nCode, WPARAM wParam, CWPSSTRUCT hookProcStruct) {
            switch (hookProcStruct.message) {
                case WM_QUERYENDSESSION:
                    callListeners("WM_QUERYENDSESSION hook");
                    break;
            }
            return new LRESULT();
        }
    };
    HHOOK hHook;
    JFrame f = new JFrame();

    public WindowsPower() {
        if (!Kernel32Ex.INSTANCE.SetProcessShutdownParameters(0x03FF, 0))
            throw new GetLastErrorException();

        mp.start();

        if (!Kernel32Ex.INSTANCE.SetConsoleCtrlHandler(hr, true))
            throw new GetLastErrorException();

        final HWND hwnd = new HWND();
        f.pack();
        hwnd.setPointer(Native.getComponentPointer(f));

        int wID = User32.INSTANCE.GetWindowThreadProcessId(hwnd, null);
        hHook = User32.INSTANCE.SetWindowsHookEx(WH_CALLWNDPROC, hp, null, wID);
        if (hHook == null)
            throw new GetLastErrorException();
    }

    @Override
    public void close() {
        if (!User32.INSTANCE.UnhookWindowsHookEx(hHook))
            throw new GetLastErrorException();

        f.dispose();
        f = null;

        mp.close();

        if (!Kernel32Ex.INSTANCE.SetConsoleCtrlHandler(hr, false))
            throw new GetLastErrorException();
    }

    protected void callListeners(String source) {

        if (LOG.isDebugEnabled()) LOG.debug("call listeners from " + source);

        for (Listener l : listeners) {
            l.quit();
        }

    }
}
