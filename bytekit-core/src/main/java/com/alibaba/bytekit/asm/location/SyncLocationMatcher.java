package com.alibaba.bytekit.asm.location;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.arthas.deps.org.objectweb.asm.Opcodes;
import com.alibaba.arthas.deps.org.objectweb.asm.tree.AbstractInsnNode;
import com.alibaba.arthas.deps.org.objectweb.asm.tree.InsnNode;
import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.location.Location.SyncEnterLocation;

public class SyncLocationMatcher implements LocationMatcher {

    private int count;
    
    boolean whenComplete;

    int opcode;
    
    public SyncLocationMatcher(int opcode, int count, boolean whenComplete) {
        if (!(Opcodes.MONITORENTER == opcode || Opcodes.MONITOREXIT == opcode)) {
            throw new IllegalArgumentException(
                    "SyncLocationMatcher only support Opcodes.MONITORENTER or Opcodes.MONITOREXIT.");
        }
        this.opcode = opcode;
        this.count = count;
        this.whenComplete = whenComplete;
    }

    @Override
    public List<Location> match(MethodProcessor methodProcessor) {
        List<Location> locations = new ArrayList<Location>();
        AbstractInsnNode insnNode = methodProcessor.getEnterInsnNode();

        int matchedCount = 0;
        while (insnNode != null) {
            if (insnNode instanceof InsnNode) {
                InsnNode node = (InsnNode) insnNode;
                if (node.getOpcode() == opcode) {
                    ++matchedCount;
                    if (count <= 0 || count == matchedCount) {
                        SyncEnterLocation location = new SyncEnterLocation(node, matchedCount, whenComplete);
                        locations.add(location);
                    }
                }
            }
            insnNode = insnNode.getNext();
        }

        return locations;
    }
}
