package com.rilvoo.foundation.moduleruntime;

import java.util.List;
import java.util.Set;

/** Immutable result of runtime validation: the effective, ordered, verified module set. */
public record EnabledModules(List<ModuleDescriptor> descriptors) {

    public Set<String> ids() {
        return descriptors.stream().map(ModuleDescriptor::id).collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public boolean isEnabled(String moduleId) {
        return ids().contains(moduleId);
    }
}
