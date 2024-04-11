package org.airway.airwaybackend.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponse <T>{
    private List<T> pagedList;
    private int pageNo;
    private int pageSize;
    private int totalSize;
}
