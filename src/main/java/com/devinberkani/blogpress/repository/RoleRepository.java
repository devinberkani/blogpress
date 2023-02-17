package com.devinberkani.blogpress.repository;

import com.devinberkani.blogpress.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
