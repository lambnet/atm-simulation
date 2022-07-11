package repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    List<T> getAll();

    Optional<T> getByAccNumber(String accNumber);
}
