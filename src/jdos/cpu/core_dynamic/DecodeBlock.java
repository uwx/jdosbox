package jdos.cpu.core_dynamic;

import jdos.Dosbox;
import jdos.cpu.CPU;
import jdos.cpu.CPU_Regs;
import jdos.cpu.Core;
import jdos.cpu.Paging;
import jdos.cpu.core_share.Constants;
import jdos.debug.Debug;
import jdos.hardware.Memory;
import jdos.misc.setup.Config;

final public class DecodeBlock extends Op {
    public Op op;
    public boolean active = true;
    public int codeStart;
    public int codeLen;
    public int runCount = 0;
    static public int compileThreshold = 0;

    public static boolean smc = false;
    private boolean compiled = false;
    public CacheBlockDynRec parent;
    public Op compiledOp = null;

    public boolean throwsException() {return false;}
    public boolean accessesMemory() {return false;}
    public boolean usesEip() {return false;}
    public boolean setsEip() {return false;}

    static private byte[] getOpCode(int start, int len) {
        byte[] opCode = new byte[len];
        int src = Paging.getDirectIndexRO(start);
        if (src>=0)
            Memory.host_memcpy(opCode, 0, src, len);
        else
            Memory.MEM_BlockRead(start, opCode, len);
        return opCode;
    }

    public DecodeBlock(CacheBlockDynRec parent, Op op, int start, int len) {
        this.parent = parent;
        this.op = op;
        this.next = op; // simplifies the compiler
        this.codeStart = start;
        this.codeLen = len;
        if (Loader.isLoaded()) {
            Op o = Loader.load(codeStart, getOpCode(codeStart, codeLen));
            if (o != null) {
                this.op = o;
                this.next = o;
                this.compiled = true;
            }
        }
    }
    final public int call() {
        if (Compiler.ENABLED) {
            runCount++;
            if (runCount==compileThreshold && !compiled && Dosbox.allPrivileges) {
                jdos.cpu.core_dynamic.Compiler.compile(this);
            }
            if (compiledOp!=null) {
                parent.code = compiledOp;
                return compiledOp.call();
            }
        }
//        if ((runCount % 10000) == 0)
//            System.out.println(op.toString()+":"+runCount);
        Op o = op;
        int result;
        Core.base_ds= CPU.Segs_DSphys;
        Core.base_ss=CPU.Segs_SSphys;
        Core.base_val_ds= CPU_Regs.ds;
        while (true) {
            if (Config.DEBUG_LOG) {
                if (o.c>=0) Debug.start(Debug.TYPE_CPU, o.c);
                //System.out.println(count+":"+o.c);
            }
            result = o.call();
            if (Config.DEBUG_LOG)
                if (o.c>=0) Debug.stop(Debug.TYPE_CPU, o.c);
            if (result == Constants.BR_Normal) {
                CPU_Regs.reg_eip+=o.eip_count;
                o = o.next;
            } else
                break;
            // :TODO: this is a temporary solution, the right solution would
            // be when this is detected to changed the current running block
            // so that the next op will return BR_Jump
            if (smc) {
                smc = false;
                System.out.println("SMC");
                CPU.CPU_Cycles-=op.cycle;
                return Constants.BR_Jump;
            }
        }
        CPU.CPU_Cycles-=op.cycle;
        return result;
    }
}
