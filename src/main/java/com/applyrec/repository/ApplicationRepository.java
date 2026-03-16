package com.applyrec.repository;

import com.applyrec.entity.Application;
import com.applyrec.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("""
            SELECT a FROM Application a
            WHERE a.user = :user
              AND (:status IS NULL OR a.status = :status)
              AND (:role IS NULL OR a.role = :role)
              AND (:minSalary IS NULL OR a.salary >= :minSalary)
              AND (:maxSalary IS NULL OR a.salary <= :maxSalary)
            ORDER BY a.appliedDate DESC
            """)
    List<Application> findByUserWithFilters(
            @Param("user") User user,
            @Param("status") String status,
            @Param("role") String role,
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary
    );

    Optional<Application> findByIdAndUser(Long id, User user);

    @Query("SELECT DISTINCT a.role FROM Application a WHERE a.user = :user ORDER BY a.role")
    List<String> findDistinctRolesByUser(@Param("user") User user);
}

