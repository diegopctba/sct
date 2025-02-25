package br.com.diego.sct.infra.repository;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SortDomain {
    String direction;
    String property;
    boolean ignoreCase;
    boolean ascending;
    String nullHandling;

    public SortDomain(){
    }
}
