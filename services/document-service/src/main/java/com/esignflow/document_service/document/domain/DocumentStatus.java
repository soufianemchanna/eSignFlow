package com.esignflow.document_service.document.domain;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum DocumentStatus {
    DRAFT, PENDING_SIGNATURE, COMPLETED, DECLINED, CANCELLED;

    private static final Map<DocumentStatus, Set<DocumentStatus>> ALLOWED = Map.of(
            DRAFT, EnumSet.of(PENDING_SIGNATURE, CANCELLED),
            PENDING_SIGNATURE, EnumSet.of(COMPLETED, DECLINED, CANCELLED),
            COMPLETED, EnumSet.noneOf(DocumentStatus.class),
            DECLINED, EnumSet.noneOf(DocumentStatus.class),
            CANCELLED, EnumSet.noneOf(DocumentStatus.class)
    );

    public boolean canTransitionTo(DocumentStatus target){
        return ALLOWED.get(this).contains(target);
    }

    public boolean isTerminal(){
        return ALLOWED.get(this).isEmpty();
    }
}
