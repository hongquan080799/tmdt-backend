package com.abc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.entity.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, String>{

}
