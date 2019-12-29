package com.booster;


import org.apache.bcel.generic.*;
import org.apache.bcel.Constants;


class MethodVisitor extends EmptyVisitor {

    private MethodGen mg;
    private ConstantPoolGen cp;
    private ClassVisitor cv;
    private ClassMetrics cm;

    MethodVisitor(MethodGen m, ClassVisitor c) {
        mg  = m;
        cv = c;
        cp  = mg.getConstantPool();
        cm = cv.getMetrics();
    }

    public void start() {
        if (!mg.isAbstract() && !mg.isNative()) {
            for (InstructionHandle ih = mg.getInstructionList().getStart();
                 ih != null; ih = ih.getNext()) {
                Instruction i = ih.getInstruction();

                if(!visitInstruction(i))
                    i.accept(this);
            }
            updateExceptionHandlers();
        }
    }


    private boolean visitInstruction(Instruction i) {
        short opcode = i.getOpcode();

        return ((InstructionConstants.INSTRUCTIONS[opcode] != null) &&
                !(i instanceof ConstantPushInstruction) &&
                !(i instanceof ReturnInstruction));
    }

    public void visitLocalVariableInstruction(LocalVariableInstruction i) {
        if(i.getOpcode() != Constants.IINC)
            cv.registerCoupling(i.getType(cp));
    }

    public void visitArrayInstruction(ArrayInstruction i) {
        cv.registerCoupling(i.getType(cp));
    }

    public void visitFieldInstruction(FieldInstruction i) {
        cv.registerFieldAccess(i.getClassName(cp), i.getFieldName(cp));
        cv.registerCoupling(i.getFieldType(cp));
    }

    public void visitInvokeInstruction(InvokeInstruction i) {
        Type[] argTypes   = i.getArgumentTypes(cp);
        for (int j = 0; j < argTypes.length; j++)
            cv.registerCoupling(argTypes[j]);
        cv.registerCoupling(i.getReturnType(cp));
        cv.registerMethodInvocation(i.getClassName(cp), i.getMethodName(cp), argTypes);
    }

    public void visitINSTANCEOF(INSTANCEOF i) {
        cv.registerCoupling(i.getType(cp));
    }

    public void visitCHECKCAST(CHECKCAST i) {
        cv.registerCoupling(i.getType(cp));
    }

    public void visitReturnInstruction(ReturnInstruction i) {
        cv.registerCoupling(i.getType(cp));
    }

    private void updateExceptionHandlers() {
        CodeExceptionGen[] handlers = mg.getExceptionHandlers();

        for(int i=0; i < handlers.length; i++) {
            Type t = handlers[i].getCatchType();
            if (t != null)
                cv.registerCoupling(t);
        }
    }
}

