package org.fhi360.lamis.model.repositories;

import org.fhi360.lamis.model.Specimen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecimenRepository extends JpaRepository<Specimen, Long> {
}
