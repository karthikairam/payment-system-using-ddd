package com.skiply.system.receipt.domain.model;

public enum ReceiptStatus {
    PENDING, // Only Payment information is available and Student information is yet to be received
    COMPLETED // Student information is also populated
}
