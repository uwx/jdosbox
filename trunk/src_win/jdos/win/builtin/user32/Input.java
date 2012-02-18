package jdos.win.builtin.user32;

import jdos.hardware.Memory;
import jdos.win.builtin.WinAPI;
import jdos.win.system.*;
import jdos.win.utils.Ptr;
import jdos.win.utils.StringUtil;

import java.util.BitSet;

public class Input extends WinAPI {
    // SHORT WINAPI GetAsyncKeyState(int nVirtKey)
    static public int GetAsyncKeyState(int nVirtKey) {
        if (WinKeyboard.keyState.get(nVirtKey))
            return 0x8000;
        else
            return 0;
    }

    // HWND WINAPI GetCapture(void)
    static public int GetCapture() {
        return StaticData.mouseCapture;
    }

    // BOOL WINAPI GetCursorPos(LPPOINT lpPoint)
    static public int GetCursorPos(int lpPoint) {
        writed(lpPoint, StaticData.currentPos.x);
        writed(lpPoint + 4, StaticData.currentPos.y);
        return TRUE;
    }

    // BOOL WINAPI GetKeyboardState(PBYTE lpKeyState)
    static public int GetKeyboardState(int lpKeyState) {
        for (int i=0;i<256;i++) {
            if (Scheduler.getCurrentThread().getKeyState().get(i))
                Memory.mem_writeb(lpKeyState + i, 0x80);
            else
                Memory.mem_writeb(lpKeyState+i, 0x0);
        }
        return WinAPI.TRUE;
    }

    static String[] keyNames = new String[] {
            null, "Esc", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Backspace", "Tab",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "Enter", "Ctrl", "A", "S",
            "D", "F", "G", "H", "J", "K", "L", ";", "'", "`", "Shift", "\\", "Z", "X", "C", "V",
            "B", "N", "M", ",", ".", "/", "Right Shift", "Num *", "Alt", "Space", "Caps Lock", "F1", "F2", "F3", "F4", "F5",
            "F6", "F7", "F8", "F9", "F10", "Pause", "Scroll Lock", "Num 7", "Num 8", "Num 9", "Num -", "Num 4", "Num 5", "Num 6", "Num +", "Num 1",
            "Num 2", "Num 3", "Num 0", "Num Del", "Sys Req", null, "\\", "F11", "F12", null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, "F13", "F14", "F15", "F16",
            "F17", "F18", "F19", "F20", "F21", "F22", "F23", "F24", null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
    };

    // int WINAPI GetKeyNameText(LONG lParam, LPTSTR lpString, int cchSize)
    static public int GetKeyNameTextA(int lParam, int lpString, int cchSize) {
        int v = (lParam >> 16) & 0xFF;
        if (keyNames[v]!=null)
            return StringUtil.strncpy(lpString, keyNames[v], cchSize);
        return 0;
    }

    // SHORT WINAPI GetKeyState(int nVirtKey)
    static public int GetKeyState(int nVirtKey) {
        if (Scheduler.getCurrentThread().getKeyState().get(nVirtKey))
            return 0x8000;
        else
            return 0;
    }

    // values just captures from English XP
    static short[] keyvscc2vk = new short[] {
            0x0, 0x1b, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0xbd, 0xbb, 0x8, 0x9,
            0x51, 0x57, 0x45, 0x52, 0x54, 0x59, 0x55, 0x49, 0x4f, 0x50, 0xdb, 0xdd, 0xd, 0x11, 0x41, 0x53,
            0x44, 0x46, 0x47, 0x48, 0x4a, 0x4b, 0x4c, 0xba, 0xde, 0xc0, 0x10, 0xdc, 0x5a, 0x58, 0x43, 0x56,
            0x42, 0x4e, 0x4d, 0xbc, 0xbe, 0xbf, 0x10, 0x6a, 0x12, 0x20, 0x14, 0x70, 0x71, 0x72, 0x73, 0x74,
            0x75, 0x76, 0x77, 0x78, 0x79, 0x90, 0x91, 0x24, 0x26, 0x21, 0x6d, 0x25, 0xc, 0x27, 0x6b, 0x23,
            0x28, 0x22, 0x2d, 0x2e, 0x2c, 0x0, 0xe2, 0x7a, 0x7b, 0xc, 0xee, 0xf1, 0xea, 0xf9, 0xf5, 0xf3,
            0x0, 0x0, 0xfb, 0x2f, 0x7c, 0x7d, 0x7e, 0x7f, 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0xed,
            0x0, 0xe9, 0x0, 0xc1, 0x0, 0x0, 0x87, 0x0, 0x0, 0x0, 0x0, 0xeb, 0x9, 0x0, 0xc2, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
    };

    static short[] keyvk2vsc = new short[] {
            0x0, 0x0, 0x0, 0x46, 0x0, 0x0, 0x0, 0x0, 0xe, 0xf, 0x0, 0x0, 0x4c, 0x1c, 0x0, 0x0,
            0x2a, 0x1d, 0x38, 0x0, 0x3a, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0,
            0x39, 0x49, 0x51, 0x4f, 0x47, 0x4b, 0x48, 0x4d, 0x50, 0x0, 0x0, 0x0, 0x54, 0x52, 0x53, 0x63,
            0xb, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x1e, 0x30, 0x2e, 0x20, 0x12, 0x21, 0x22, 0x23, 0x17, 0x24, 0x25, 0x26, 0x32, 0x31, 0x18,
            0x19, 0x10, 0x13, 0x1f, 0x14, 0x16, 0x2f, 0x11, 0x2d, 0x15, 0x2c, 0x5b, 0x5c, 0x5d, 0x0, 0x5f,
            0x52, 0x4f, 0x50, 0x51, 0x4b, 0x4c, 0x4d, 0x47, 0x48, 0x49, 0x37, 0x4e, 0x0, 0x4a, 0x53, 0x35,
            0x3b, 0x3c, 0x3d, 0x3e, 0x3f, 0x40, 0x41, 0x42, 0x43, 0x44, 0x57, 0x58, 0x64, 0x65, 0x66, 0x67,
            0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x76, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x45, 0x46, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x2a, 0x36, 0x1d, 0x1d, 0x38, 0x38, 0x6a, 0x69, 0x67, 0x68, 0x65, 0x66, 0x32, 0x20, 0x2e, 0x30,
            0x19, 0x10, 0x24, 0x22, 0x6c, 0x6d, 0x6b, 0x21, 0x0, 0x0, 0x27, 0xd, 0x33, 0xc, 0x34, 0x35,
            0x29, 0x73, 0x7e, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1a, 0x2b, 0x1b, 0x28, 0x0,
            0x0, 0x0, 0x56, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x71, 0x5c, 0x7b, 0x0, 0x6f, 0x5a, 0x0,
            0x0, 0x5b, 0x0, 0x5f, 0x0, 0x5e, 0x0, 0x0, 0x0, 0x5d, 0x0, 0x62, 0x0, 0x0, 0x0, 0x0
    };

    static short[] keyvk2char = new short[] {
            0x0, 0x0, 0x0, 0x3, 0x0, 0x0, 0x0, 0x0, 0x8, 0x9, 0x0, 0x0, 0x0, 0xd, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1b, 0x0, 0x0, 0x0, 0x0,
            0x20, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f,
            0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2a, 0x2b, 0x0, 0x2d, 0x2e, 0x2f,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x3b, 0x3d, 0x2c, 0x2d, 0x2e, 0x2f,
            0x60, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x5b, 0x5c, 0x5d, 0x27, 0x0,
            0x0, 0x0, 0x5c, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
    };

    // UINT WINAPI MapVirtualKey(UINT uCode, UINT uMapType)
    static public int MapVirtualKeyA(int uCode, int uMapType) {
        faked();
        switch (uMapType) {
            case MAPVK_VK_TO_VSC:
            case MAPVK_VK_TO_VSC_EX:
                if (uCode>=0 && uCode<keyvk2vsc.length)
                        return keyvk2vsc[uCode];
                break;
            case MAPVK_VSC_TO_VK: /* scan-code to vkey-code */
            case MAPVK_VSC_TO_VK_EX:
                if (uCode>=0 && uCode<keyvscc2vk.length)
                    return keyvscc2vk[uCode];
                break;
            case MAPVK_VK_TO_CHAR: /* vkey-code to unshifted ANSI code */
                if (uCode>=0 && uCode<keyvk2char.length)
                    return keyvk2char[uCode];
                break;
        }
        return 0;
    }

    // BOOL WINAPI ReleaseCapture(void)
    static public int ReleaseCapture() {
        StaticData.mouseCapture = 0;
        return TRUE;
    }

    // HWND WINAPI SetCapture(HWND hWnd)
    static public int SetCapture(int hWnd) {
        int result = StaticData.mouseCapture;
        StaticData.mouseCapture = hWnd;
        return result;
    }

    // BOOL WINAPI SetCursorPos(int X, int Y)
    static public int SetCursorPos(int X, int Y) {
        StaticData.currentPos.x = X;
        StaticData.currentPos.y = Y;
        return TRUE;
    }

    // called from the cpu thread
    static public void processInput() {
        while(StaticData.inputQueue.size()>0) {
            Object msg = StaticData.inputQueue.remove(0);
            if (msg instanceof MouseInput) {
                MouseInput mouseMsg = (MouseInput)msg;
                handeMouseInput(mouseMsg.msg, mouseMsg.pt, mouseMsg.wParam);
            } else if (msg instanceof KeyboardInput) {
                KeyboardInput keyboardMsg = (KeyboardInput)msg;
                handeKeyboardInput(keyboardMsg.msg, keyboardMsg.wParam, keyboardMsg.lParam, keyboardMsg.keyState);
            }
        }
    }

    static private class MouseInput {
        public MouseInput(int msg, WinPoint pt, int wParam) {
            this.msg = msg;
            this.pt = pt.copy();
            this.wParam = wParam;
        }
        int msg;
        WinPoint pt;
        int wParam;
    }

    static private class KeyboardInput {
        public KeyboardInput(int msg, int wParam, int lParam, BitSet keyState) {
            this.msg = msg;
            this.wParam = wParam;
            this.lParam = lParam;
            this.keyState = keyState;
        }
        int msg;
        int wParam;
        int lParam;
        BitSet keyState;
    }

    // called from java thread
    static public void addMouseMsg(int msg, WinPoint pt, int wParam) {
        synchronized(StaticData.inputQueueMutex) {
            StaticData.inputQueue.add(new MouseInput(msg, pt, wParam));
            StaticData.inputQueueMutex.notify();
        }
    }

    static public void addKeyboardMsg(int msg, int wParam, int lParam, BitSet keyState) {
         synchronized(StaticData.inputQueueMutex) {
            StaticData.inputQueue.add(new KeyboardInput(msg, wParam, lParam, keyState));
            StaticData.inputQueueMutex.notify();
        }
    }

    static private void handeKeyboardInput(int msg, int wParam, int lParam, BitSet keyState) {
        WinWindow window = WinWindow.get(StaticData.foregroundWindow);
        if (window != null)
            window.getThread().postMessage(window.getThread().GetGUIThreadInfo().hwndFocus, msg, wParam, lParam, keyState);
    }

    static private void handeMouseInput(int msg, WinPoint pt, int wParam) {
        WinWindow window = null;
        int hitTest = WinAPI.HTNOWHERE;
        WinPoint relWinPt = null;

        if (StaticData.mouseCapture != 0) {
            window = WinWindow.get(StaticData.mouseCapture);
            hitTest = WinWindow.HTCLIENT;
        } else {
            window = WinWindow.get(StaticData.desktopWindow).findWindowFromPoint(pt.x, pt.y);
            if (window.handle == StaticData.desktopWindow) {
                return;
            }
        }
        if (LOG) {
            System.out.println("\nMOUSE 0x"+Ptr.toString(msg)+" "+pt.toString()+" hwnd="+window.handle+"("+StaticData.mouseCapture+")");
        }
        relWinPt = pt.copy();
        window.screenToWindow(relWinPt);
        if (hitTest == WinAPI.HTNOWHERE) {
            hitTest = Message.SendMessageA(window.handle, WinAPI.WM_NCHITTEST, 0, WinAPI.MAKELONG(pt.x, pt.y));
        }
        if (msg != WinWindow.WM_MOUSEWHEEL) {
            if (hitTest != WinWindow.HTCLIENT)
                msg += WinWindow.WM_NCMOUSEMOVE - WinWindow.WM_MOUSEMOVE;
            else
                window.screenToWindow(pt);
        }
        // :TODO: double click?

        if (hitTest == WinWindow.HTERROR || hitTest == WinWindow.HTNOWHERE) {
            window.postMessage(WinWindow.WM_SETCURSOR, window.handle, hitTest | (msg >> 16));
            return;
        }

        if (StaticData.mouseCapture == 0) {
            // :TODO: WM_MOUSEACTIVATE
        }

        WinMsg m = window.getThread().getLastMessage();
        if (m != null && m.message == WinWindow.WM_MOUSEMOVE && m.message == msg) {
            m.wParam = wParam;
            m.lParam = (pt.x) | (pt.y << 16);
        } else {
            window.postMessage(WinWindow.WM_SETCURSOR, window.handle, hitTest | (msg >> 16));
            window.postMessage(msg, wParam, (pt.x) | (pt.y << 16));
        }
    }
}