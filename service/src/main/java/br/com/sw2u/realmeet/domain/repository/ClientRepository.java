package br.com.sw2u.realmeet.domain.repository;

import br.com.sw2u.realmeet.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
