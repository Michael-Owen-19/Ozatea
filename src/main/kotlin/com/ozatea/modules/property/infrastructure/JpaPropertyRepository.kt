package com.ozatea.modules.property.infrastructure

import com.ozatea.modules.property.domain.Property
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaPropertyRepository : JpaRepository<Property, Long>