package jdos.cpu;

import jdos.hardware.Memory;

public class CPU_Regs extends Flags {
    public static final int CF=	0x00000001;
    public static final int PF=	0x00000004;
    public static final int AF=	0x00000010;
    public static final int ZF=	0x00000040;
    public static final int SF=	0x00000080;
    public static final int OF=	0x00000800;

    public static final int TF=	0x00000100;
    public static final int IF=	0x00000200;
    public static final int DF=	0x00000400;

    public static final int IOPL=	0x00003000;
    public static final int NT=	0x00004000;
    public static final int VM=	0x00020000;
    public static final int AC=	0x00040000;
    public static final int ID=	0x00200000;

    public static final int FMASK_TEST	=	(CF | PF | AF | ZF | SF | OF);
    public static final int FMASK_NORMAL=	(FMASK_TEST | DF | TF | IF | AC );
    public static final int FMASK_ALL	=	(FMASK_NORMAL | IOPL | NT);

    public static void SETFLAGBIT(int flag, boolean set) {
        if (set)
            CPU_Regs.flags |= flag;
        else
            CPU_Regs.flags &=~ flag;
    }

    public static int GETFLAG(int flag) {
        return CPU_Regs.flags & flag;
    }

    public static boolean GETFLAGBOOL(int flag) {
        return ((CPU_Regs.flags & flag) != 0);
    }

    public static int GETFLAG_IOPL() {
        return (CPU_Regs.flags & IOPL) >> 12;
    }

    // SegNames
    public static final int es=0;
    public static final int cs=1;
    public static final int ss=2;
    public static final int ds=3;
    public static final int fs=4;
    public static final int gs=5;

//    public static /*PhysPt*/int SegPhys(int index) {
//        return CPU.Segs.phys[index];
//    }
//
//    public static /*Bit16u*/int SegValue(int index) {
//        return (int)(CPU.Segs.val[index] & 0xFFFFl);
//    }
//
    public static /*RealPt*/int RealMakeSegDS(/*Bit16u*/int off) {
        return Memory.RealMake(CPU.Segs_DSval,off);
    }

    public static /*RealPt*/int RealMakeSegSS(/*Bit16u*/int off) {
        return Memory.RealMake(CPU.Segs_SSval,off);
    }

    public static void SegSet16ES(/*Bit16u*/int val) {
        CPU.Segs_ESval=val;
        CPU.Segs_ESphys=val << 4;
    }

    public static void SegSet16CS(/*Bit16u*/int val) {
        CPU.Segs_CSval=val;
        CPU.Segs_CSphys=val << 4;
    }

    public static void SegSet16SS(/*Bit16u*/int val) {
        CPU.Segs_SSval=val;
        CPU.Segs_SSphys=val << 4;
    }

    public static void SegSet16DS(/*Bit16u*/int val) {
        CPU.Segs_DSval=val;
        CPU.Segs_DSphys=val << 4;
    }

    public static void SegSet16FS(/*Bit16u*/int val) {
        CPU.Segs_FSval=val;
        CPU.Segs_FSphys=val << 4;
    }

    public static void SegSet16GS(/*Bit16u*/int val) {
        CPU.Segs_GSval=val;
        CPU.Segs_GSphys=val << 4;
    }  

    // IP
    public static int reg_ip() {
        return reg_eip & 0xFFFF;
    }
    public static void reg_ip(int value) {
        reg_eip = value & 0xFFFF | (reg_eip & 0xFFFF0000);
    }

    public CPU_Regs() {
    }

    public static final class Reg {
        Reg parent;
        String name = null;

        public Reg() {
        }
        public Reg(String name) {
            this.name = name;
        }

        public Reg(Reg parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }
        public Reg getParent() {
            return parent;
        }
        public void set8(short s) {
            if (parent == null)
                low(s);
            else
                parent.high(s);
        }

        public short get8() {
            if (parent == null)
                return low();
            else
                return parent.high();
        }
        final public void dword(long l) {
            dword = (int)l;
        }
        final public void word_dec() {
            word(word()-1);
        }
        final public int word() {
            return dword & 0xFFFF;
        }
        final public void word(int value) {
            dword = (value & 0xFFFF) | (dword & 0xFFFF0000);
        }
        final public short low() {
            return (short)(dword & 0xffl);
        }
        final public void low(int value) {
            dword = (value & 0xFF) | (dword & 0xFFFFFF00);
        }

        final public short high() {
            return (short)((dword >> 8) & 0xffl);
        }
        final public void high(int value) {
            dword = ((value & 0xFF) << 8) | (dword & 0xFFFF00FF);
        }
        public int dword;
    }

    final static public Reg reg_eax = new Reg("eax");
    final static public Reg reg_ebx = new Reg("ebx");
    final static public Reg reg_ecx = new Reg("ecx");
    final static public Reg reg_edx = new Reg("edx");
    final static public Reg reg_esi = new Reg("esi");
    final static public Reg reg_edi = new Reg("edi");
    final static public Reg reg_esp = new Reg("esp");
    final static public Reg reg_ebp = new Reg("ebp");

    final static public Reg reg_ah = new Reg(reg_eax);
    final static public Reg reg_bh = new Reg(reg_ebx);
    final static public Reg reg_ch = new Reg(reg_ecx);
    final static public Reg reg_dh = new Reg(reg_edx);

    static public int reg_eip;

    static public /*Bitu*/int flags;
}
