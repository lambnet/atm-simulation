package util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckDuplicate {
    public static <T> Set<T> duplicateList(List<T> list){
        Set<T> items = new HashSet<>();
        return list.stream()
                .filter(item -> !items.add(item))
                .collect(Collectors.toSet());
    }
}
