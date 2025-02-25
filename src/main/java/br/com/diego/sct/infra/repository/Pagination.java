package br.com.diego.sct.infra.repository;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class Pagination<T> {
    private List<T> content = new ArrayList<>();
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private long numberOfElements;
    private int pageSize;
    private int number;


    public Pagination() {
    }

    public<R> Pagination<R> map(Function<? super T, ? extends R> mapper) {
        Pagination<R> pagination = new Pagination<>();
        pagination.content = content.stream()
            .map(mapper)
            .collect(Collectors.toList());
        pagination.totalPages = totalPages;
        pagination.totalElements = totalElements;
        pagination.last = last;
        pagination.first = first;
        pagination.numberOfElements = numberOfElements;
        pagination.pageSize = pageSize;
        pagination.number = number;
        return pagination;
    }
}
