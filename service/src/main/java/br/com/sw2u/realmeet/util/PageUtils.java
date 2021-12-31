package br.com.sw2u.realmeet.util;

import br.com.sw2u.realmeet.exception.InvalidOrderByFieldException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class PageUtils {
    
    private PageUtils() {
    }
    
    public static Pageable newPageable(
            Integer page,
            Integer limit,
            Integer maxLimit,
            String orderBy,
            List<String> validSortableFields) {
        
        int definedPage = isNull(page) ? 0 : page;
        int definedLimit = isNull(limit) ? maxLimit : Math.min(limit, maxLimit);
        Sort definedSort = parseOrderByFields(orderBy, validSortableFields);
        
        return PageRequest.of(definedPage, definedLimit, definedSort);
    }
    
    private static Sort parseOrderByFields(String orderBy, List<String> validSortableFields) {
        if (isNull(validSortableFields) || validSortableFields.isEmpty()) {
            throw new IllegalArgumentException("No valid sortable fields where defined");
        }
        
        if (isBlank(orderBy)) {
            return Sort.unsorted();
        }
        
        return Sort.by(Stream.of(orderBy.split(","))
                               .map(
                                       f -> {
                                           String fieldName;
                                           Sort.Order order;
                            
                                           if (f.startsWith("-")) {
                                               fieldName = f.substring(1);
                                               order = Sort.Order.desc(fieldName);
                                           } else {
                                               fieldName = f;
                                               order = Sort.Order.asc(fieldName);
                                           }
                            
                                           if (!validSortableFields.contains(fieldName)) {
                                               throw new InvalidOrderByFieldException();
                                           }
                            
                                           return order;
                                       }
                               )
                               .collect(Collectors.toList()));
    }
}
