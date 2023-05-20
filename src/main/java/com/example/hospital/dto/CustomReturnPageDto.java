package com.example.hospital.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomReturnPageDto {
    private long size;
    private long number;
    private boolean first;
    private boolean last;
    private long numberOfElements;
    private boolean empty;
    private Object content;
    private long totalElements;

    public CustomReturnPageDto(long size, long number, boolean first, boolean last, long numberOfElements, boolean empty, Object content, long totalElements) {
        this.size = size;
        this.number = number;
        this.first = first;
        this.last = last;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
        this.content = content;
        this.totalElements = totalElements;
    }
}
