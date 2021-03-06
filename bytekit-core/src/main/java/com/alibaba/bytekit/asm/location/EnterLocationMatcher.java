package com.alibaba.bytekit.asm.location;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.arthas.deps.org.objectweb.asm.tree.AbstractInsnNode;
import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.location.Location.EnterLocation;
import com.alibaba.bytekit.asm.location.filter.LocationFilter;

public class EnterLocationMatcher implements LocationMatcher {

    @Override
    public List<Location> match(MethodProcessor methodProcessor) {
        List<Location> locations = new ArrayList<Location>();
        AbstractInsnNode enterInsnNode = methodProcessor.getEnterInsnNode();

        LocationFilter locationFilter = methodProcessor.getLocationFilter();
        if (locationFilter.allow(enterInsnNode, LocationType.ENTER, true)) {
            EnterLocation enterLocation = new EnterLocation(enterInsnNode);
            locations.add(enterLocation);
        }
        return locations;
    }
}
