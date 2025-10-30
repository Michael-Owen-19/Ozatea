package com.ozatea.modules.media.infrastructure

import com.ozatea.modules.media.domain.Media
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaMediaRepository : JpaRepository<Media, Long>
